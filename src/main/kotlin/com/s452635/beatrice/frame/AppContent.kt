package com.s452635.beatrice.frame

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import com.s452635.beatrice.simulation.LightState

@Composable
@Preview
fun AppContent()
{
    MaterialTheme {
        Image( // background
            bitmap = useResource( "background.png", ::loadImageBitmap ),
            contentDescription = null
            )

        // region lights
        LightSprite( // West
            lightState = LightState.Red,
            offset = Pair( 164.dp, 312.dp )
            )
        LightSprite( // North
            lightState = LightState.Yellow,
            offset = Pair( 246.dp, 148.dp )
            )
        LightSprite( // East
            lightState = LightState.RedYellow,
            offset = Pair( 407.dp, 230.dp )
            )
        LightSprite( // South
            lightState = LightState.Green,
            offset = Pair( 326.dp, 392.dp )
            )
        // endregion

        // region plates
        PlateSprite( // West
            powered = true,
            offset = Pair( 50.dp, 309.dp ),
            rotation = 0.0f
            )
        PlateSprite( // North
            powered = true,
            offset = Pair( 214.dp, 46.dp ),
            rotation = 90.0f
            )
        PlateSprite( // East
            powered = true,
            offset = Pair( 458.dp, 228.dp ),
            rotation = 180.0f
            )
        PlateSprite( // South
            powered = true,
            offset = Pair( 295.dp, 488.dp ),
            rotation = 270.0f
            )
        // endregion

        // region arrows
        ArrowSprite( // West
            visible = true,
            offset = Pair( 268.dp, 325.dp ),
            rotation = 0.0f
            )
        ArrowSprite( // North
            visible = true,
            offset = Pair( 227.dp, 286.dp ),
            rotation = 90.0f
            )
        ArrowSprite( // East
            visible = true,
            offset = Pair( 268.dp, 245.dp ),
            rotation = 180.0f
            )
        ArrowSprite( // South
            visible = true,
            offset = Pair( 309.dp, 285.dp ),
            rotation = 270.0f
            )
        // endregion
    }
}