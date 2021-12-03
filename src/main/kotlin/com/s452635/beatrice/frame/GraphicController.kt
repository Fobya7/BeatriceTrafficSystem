package com.s452635.beatrice.frame

import androidx.compose.runtime.mutableStateOf
import com.s452635.beatrice.simulation.LightState
import com.s452635.beatrice.simulation.TickKind
import com.s452635.beatrice.simulation.TrafficState
import java.lang.Thread.sleep
import kotlin.random.Random

class GraphicController (
    lightTickShortLength : Int = 500,
    lightTickLongLength : Int = 1500,
    trafficTickLength : Int = 1000
    )
{
    // region values

    val west  = Section( LightState.Green )
    val north = Section( LightState.Green )
    val east  = Section( LightState.Green )
    val south = Section( LightState.Green )

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
        println( "light tick" )
    }

    private fun onTrafficTick()
    {
        // TODO : traffic generation
        println( "traffic tick" )
    }
}

class Section (
    lightState : LightState,
    )
{
    val lightState = mutableStateOf( lightState )
    val platePowered = mutableStateOf( false )

    val carQueue = mutableStateOf( 0 )
    fun addCarToQueue()
    {
        if( carQueue.value < 99 ) { carQueue.value +=1 }
    }
    fun removeCarFromQueue()
    {
        if( carQueue.value > 0 ) { carQueue.value -=1 }
    }

    val arrowVisible = mutableStateOf( false )
    fun showArrow() { arrowVisible.value = true }
    fun hideArrow() { arrowVisible.value = false }
}