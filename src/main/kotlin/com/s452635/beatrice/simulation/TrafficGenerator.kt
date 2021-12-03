package com.s452635.beatrice.simulation

import kotlin.random.Random

fun generateCars( trafficState : TrafficState ) : Int
{
    fun summonCar( chance : Int ) : Int
    {
        if( chance !in 0..100 ) { error( "Wrong chance." ) }

        return if ( Random.nextInt( 0, 100 ) < chance )
            1 else 0
    }

    return when( trafficState )
    {
        TrafficState.None -> 0
        TrafficState.Some ->
            summonCar( 10 )
        TrafficState.Mild ->
            summonCar( 30 )
        TrafficState.Busy ->
            summonCar( 40 ) +
            summonCar( 40 )
        TrafficState.Heavy ->
            summonCar( 80 ) +
            summonCar( 40 )
    }
}