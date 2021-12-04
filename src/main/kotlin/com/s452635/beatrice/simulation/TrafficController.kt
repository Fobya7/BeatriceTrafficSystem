package com.s452635.beatrice.simulation

enum class LightState { Green, Yellow, Red, RedYellow }
enum class TrafficFlow { None, Some, Mild, Busy, Heavy }
enum class TickKind { Short, Long }

enum class TrafficState( val tickKind : TickKind )
{
    R_VR( TickKind.Short ) {
        override fun nextState( boolean : Boolean? )
            : TrafficState { return RY_R }
        override fun getLights() : Pair<LightState, LightState>
            { return Pair( LightState.Red, LightState.Red ) }
        },
    RY_R( TickKind.Short ) {
        override fun nextState( boolean : Boolean? )
            : TrafficState { return G1_R }
        override fun getLights() : Pair<LightState, LightState>
            { return Pair( LightState.RedYellow, LightState.Red ) }
        },

    G1_R( TickKind.Long ) {
        override fun nextState( boolean : Boolean? )
            : TrafficState { return if( boolean!! ) G2_R else G1_R }
        override fun getLights() : Pair<LightState, LightState>
            { return Pair( LightState.Green, LightState.Red ) }
        },
    G2_R( TickKind.Long ) {
        override fun nextState( boolean : Boolean? )
            : TrafficState { return Y_R }
        override fun getLights() : Pair<LightState, LightState>
            { return Pair( LightState.Green, LightState.Red ) }
        },

    Y_R( TickKind.Short ) {
        override fun nextState( boolean : Boolean? )
            : TrafficState { return HR_R }
        override fun getLights() : Pair<LightState, LightState>
            { return Pair( LightState.Yellow, LightState.Red ) }
        },
    HR_R( TickKind.Short ) {
        override fun nextState( boolean : Boolean? )
            : TrafficState { return R_RY }
        override fun getLights() : Pair<LightState, LightState>
            { return Pair( LightState.Red, LightState.Red ) }
        },
    R_RY( TickKind.Short ) {
        override fun nextState( boolean : Boolean? )
            : TrafficState { return R_G1 }
        override fun getLights() : Pair<LightState, LightState>
            { return Pair( LightState.Red, LightState.RedYellow ) }
        },

    R_G1( TickKind.Long ) {
        override fun nextState( boolean : Boolean? )
            : TrafficState { return if( boolean!! ) R_Y else R_G2 }
        override fun getLights() : Pair<LightState, LightState>
            { return Pair( LightState.Red, LightState.Green ) }
        },
    R_G2( TickKind.Long ) {
        override fun nextState( boolean : Boolean? )
            : TrafficState { return R_Y }
        override fun getLights() : Pair<LightState, LightState>
            { return Pair( LightState.Red, LightState.Green ) }
        },

    R_Y( TickKind.Short ) {
        override fun nextState( boolean : Boolean? )
            : TrafficState { return R_VR }
        override fun getLights() : Pair<LightState, LightState>
            { return Pair( LightState.Red, LightState.Yellow ) }
        };

    abstract fun nextState( boolean : Boolean? ) : TrafficState
    abstract fun getLights() : Pair<LightState,LightState>
}