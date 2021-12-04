package com.s452635.beatrice.simulation

import kotlin.random.Random

fun generateCars(trafficFlow : TrafficFlow ) : Int
{
    fun summonCar( chance : Int ) : Int
    {
        if( chance !in 0..100 ) { error( "Wrong chance." ) }

        return if ( Random.nextInt( 0, 100 ) < chance )
            1 else 0
    }

    return when( trafficFlow )
    {
        TrafficFlow.None -> 0
        TrafficFlow.Some ->
            summonCar( 10 )
        TrafficFlow.Mild ->
            summonCar( 30 )
        TrafficFlow.Busy ->
            summonCar( 40 ) +
            summonCar( 40 )
        TrafficFlow.Heavy ->
            summonCar( 80 ) +
            summonCar( 40 )
    }
}