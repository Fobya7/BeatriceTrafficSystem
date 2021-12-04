package com.s452635.beatrice

import androidx.compose.runtime.mutableStateOf
import com.s452635.beatrice.simulation.*
import java.lang.Thread.sleep
import kotlin.random.Random

class GraphicController (
    private val lightTickShortLength : Int = 500,
    private val lightTickLongLength : Int = 1500,
    trafficTickLength : Int = 1000,
    carSpeed : Int = 400
    )
{
    // region values

    val west  = Section( LightState.Red, carSpeed )
    val north = Section( LightState.Red, carSpeed )
    val east  = Section( LightState.Red, carSpeed )
    val south = Section( LightState.Red, carSpeed )

    private val systemState = mutableStateOf( TrafficState.R_VR )
    val horTraffic = mutableStateOf( TrafficFlow.None )
    val verTraffic = mutableStateOf( TrafficFlow.None )

    val isRunning = mutableStateOf( false )
    val lightTickKind = mutableStateOf( TickKind.Short )
    val lightTickPercent = mutableStateOf( 0 )
    val trafficTickPercent = mutableStateOf( 0 )

    // endregion

    // region callable UI functions
    fun changeHorTraffic(trafficFlow : TrafficFlow )
    {
        horTraffic.value = trafficFlow
    }
    fun changeVerTraffic(trafficFlow : TrafficFlow )
    {
        verTraffic.value = trafficFlow
    }
    // endregion

    // region threads

    private val tickCars = Thread {
        while( isRunning.value )
        {
            sleep( 10 )
            onCarTick()
        }
        }
    private val tickLights = Thread {
        while( isRunning.value )
        {
            when( lightTickKind.value )
            {
                TickKind.Short ->
                {
                    sleep(( lightTickShortLength/4 ).toLong())
                    lightTickPercent.value += 25
                }
                TickKind.Long ->
                {
                    sleep(( lightTickLongLength/10 ).toLong())
                    lightTickPercent.value += 10
                }
            }

            if( lightTickPercent.value == 100 )
            {
                lightTickPercent.value = 0
                onLightTick()
            }
        } }
    private val tickTraffic = Thread {
        while( isRunning.value ) {
            sleep(( trafficTickLength/10 ).toLong())
            trafficTickPercent.value += 10

            if( trafficTickPercent.value == 100 )
            {
                trafficTickPercent.value = 0
                onTrafficTick()
            }
        } }
    val simulation = Thread {
        isRunning.value = true
        tickLights.start()
        tickTraffic.start()
        tickCars.start()
    }

    // for testing graphics-controller connection
    @Suppress( "unused" )
    val pretendToBeBroken = Thread {
        val changeLights = Thread {
            while( isRunning.value )
            {
                sleep(200)
                west.lightState.value = LightState.values()[Random.nextInt( 0, 3 )]
                north.lightState.value = LightState.values()[Random.nextInt( 0, 3 )]
                east.lightState.value = LightState.values()[Random.nextInt( 0, 3 )]
                south.lightState.value = LightState.values()[Random.nextInt( 0, 3 )]
            } }
        val flashPlate = Thread {
            while( isRunning.value )
            {
                sleep(100)
                west.platePowered.value = !west.platePowered.value
            } }
        val changePlates = Thread {
            while( isRunning.value )
            {
                sleep(324)
                north.platePowered.value = Random.nextBoolean()
                east.platePowered.value = Random.nextBoolean()
                south.platePowered.value = Random.nextBoolean()
            } }
        val flashArrows = Thread {
            while( isRunning.value )
            {
                if( isRunning.value ) { sleep( 500 ) }
                south.hideArrow()
                west.showArrow()
                if( isRunning.value ) { sleep( 500 ) }
                west.hideArrow()
                north.showArrow()
                if( isRunning.value ) { sleep( 500 ) }
                north.hideArrow()
                east.showArrow()
                if( isRunning.value ) { sleep( 500 ) }
                east.hideArrow()
                south.showArrow()
            } }

        isRunning.value = true
        changeLights.start()
        flashPlate.start()
        changePlates.start()
        flashArrows.start()
    }

    // endregion

    private fun onLightTick()
    {
        fun isVerticalFree() : Boolean
        {
            return north.platePowered.value || south.platePowered.value
        }
        fun isHorizontalFree() : Boolean
        {
            return east.platePowered.value || west.platePowered.value
        }

        systemState.value = when( systemState.value )
        {
            TrafficState.G1_R -> {
                systemState.value.nextState( isVerticalFree() )
                }
            TrafficState.R_G1 -> {
                systemState.value.nextState( isHorizontalFree() )
                }
            else -> {
                systemState.value.nextState( null )
                }
        }

        lightTickKind.value = systemState.value.tickKind

        systemState.value.getLights().let {
            west.lightState.value = it.first
            east.lightState.value = it.first
            north.lightState.value = it.second
            south.lightState.value = it.second
        }

    }

    private fun onTrafficTick()
    {
        west.addCarToQueue( generateCars( horTraffic.value ) )
        east.addCarToQueue( generateCars( horTraffic.value ) )

        north.addCarToQueue( generateCars( verTraffic.value ) )
        south.addCarToQueue( generateCars( verTraffic.value ) )
    }

    private fun onCarTick()
    {
        west.tryDrive()
        north.tryDrive()
        east.tryDrive()
        south.tryDrive()
    }
}

class Section (
    lightState : LightState,
    private val carSpeed : Int
    )
{
    val lightState = mutableStateOf( lightState )
    val carQueue = mutableStateOf( 0 )
    val platePowered = mutableStateOf( false )
    val arrowVisible = mutableStateOf( false )

    private fun checkPlate()
    {
        platePowered.value = ( carQueue.value != 0 )
    }

    fun addCarToQueue(
        carAmount : Int = 1
        )
    {
        carQueue.value = minOf( 99, carQueue.value + carAmount )
        checkPlate()
    }
    fun tryDrive()
    {
        val driveCar = Thread {
            showArrow()
            sleep( carSpeed.toLong() )
            hideArrow()
            }

        if (
            lightState.value == LightState.Green &&
            carQueue.value > 0 &&
            !arrowVisible.value
        )
        {
            carQueue.value -=1
            driveCar.start()
        }

        checkPlate()
    }

    fun showArrow() { arrowVisible.value = true }
    fun hideArrow() { arrowVisible.value = false }
}