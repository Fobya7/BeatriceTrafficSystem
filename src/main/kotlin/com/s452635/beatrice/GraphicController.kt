package com.s452635.beatrice

import androidx.compose.runtime.mutableStateOf
import com.s452635.beatrice.simulation.LightState
import com.s452635.beatrice.simulation.TickKind
import com.s452635.beatrice.simulation.TrafficState
import com.s452635.beatrice.simulation.generateCars
import java.lang.Thread.sleep
import kotlin.random.Random

class GraphicController (
    lightTickShortLength : Int = 500,
    lightTickLongLength : Int = 1500,
    trafficTickLength : Int = 1000,
    carSpeed : Int = 200
    )
{
    // region values

    val west  = Section( LightState.Green, carSpeed )
    val north = Section( LightState.Red, carSpeed )
    val east  = Section( LightState.Green, carSpeed )
    val south = Section( LightState.Yellow, carSpeed )

    val horTraffic = mutableStateOf( TrafficState.None )
    val verTraffic = mutableStateOf( TrafficState.None )

    val isRunning = mutableStateOf( false )
    val lightTickKind = mutableStateOf( TickKind.Long )
    val lightTickPercent = mutableStateOf( 0 )
    val trafficTickPercent = mutableStateOf( 0 )

    // endregion

    // region callable UI functions
    fun changeHorTraffic( trafficState : TrafficState )
    {
        horTraffic.value = trafficState
    }
    fun changeVerTraffic( trafficState : TrafficState )
    {
        verTraffic.value = trafficState
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
                lightTickKind.value = when( lightTickKind.value )
                {
                    TickKind.Short -> TickKind.Long
                    TickKind.Long -> TickKind.Short
                }
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
        // TODO : light automata reference
    }

    private fun onTrafficTick()
    {
        west.carQueue.value += generateCars( horTraffic.value )
        east.carQueue.value += generateCars( horTraffic.value )

        north.carQueue.value += generateCars( verTraffic.value )
        south.carQueue.value += generateCars( verTraffic.value )
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