package com.s452635.beatrice.frame

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.s452635.beatrice.GraphicController

@Composable
@Preview
fun AppContent(
    gc : GraphicController
    )
= MaterialTheme {

    Image( // background
        bitmap = useResource( "background.png", ::loadImageBitmap ),
        contentDescription = null
        )

    // region lights
    LightSprite( // West
        lightState = gc.west.lightState.value,
        offset = Pair( 164.dp, 312.dp )
        )
    LightSprite( // North
        lightState = gc.north.lightState.value,
        offset = Pair( 246.dp, 148.dp )
        )
    LightSprite( // East
        lightState = gc.east.lightState.value,
        offset = Pair( 407.dp, 230.dp )
        )
    LightSprite( // South
        lightState = gc.south.lightState.value,
        offset = Pair( 326.dp, 392.dp )
        )
    // endregion

    // region plates
    PlateSprite( // West
        powered = gc.west.platePowered.value,
        offset = Pair( 50.dp, 309.dp ),
        rotation = 0.0f
        )
    PlateSprite( // North
        powered = gc.north.platePowered.value,
        offset = Pair( 214.dp, 46.dp ),
        rotation = 90.0f
        )
    PlateSprite( // East
        powered = gc.east.platePowered.value,
        offset = Pair( 458.dp, 228.dp ),
        rotation = 180.0f
        )
    PlateSprite( // South
        powered = gc.south.platePowered.value,
        offset = Pair( 295.dp, 488.dp ),
        rotation = 270.0f
        )
    // endregion

    // region arrows
    ArrowSprite( // West
        visible = gc.west.arrowVisible.value,
        offset = Pair( 268.dp, 325.dp ),
        rotation = 0.0f
        )
    ArrowSprite( // North
        visible = gc.north.arrowVisible.value,
        offset = Pair( 227.dp, 286.dp ),
        rotation = 90.0f
        )
    ArrowSprite( // East
        visible = gc.east.arrowVisible.value,
        offset = Pair( 268.dp, 245.dp ),
        rotation = 180.0f
        )
    ArrowSprite( // South
        visible = gc.south.arrowVisible.value,
        offset = Pair( 309.dp, 285.dp ),
        rotation = 270.0f
        )
    // endregion

    // region car buttons
    CarButton( // West
        queueCount = gc.west.carQueue.value,
        offset = Pair( 49.dp, 396.dp ),
        onClick = { gc.west.addCarToQueue() }
        )
    CarButton( // North
        queueCount = gc.north.carQueue.value,
        offset = Pair( 110.dp, 54.dp ),
        onClick = { gc.north.addCarToQueue() }
        )
    CarButton( // East
        queueCount = gc.east.carQueue.value,
        offset = Pair( 456.dp, 150.dp ),
        onClick = { gc.east.addCarToQueue() }
        )
    CarButton( // South
        queueCount = gc.south.carQueue.value,
        offset = Pair( 396.dp, 497.dp ),
        onClick = { gc.south.addCarToQueue() }
        )
    // endregion

    // region traffic sliders
    SeeThroughPane (
        offset = Pair( 22.dp, 470.dp )
        )
    {
        TrafficSlider (
            name = "hor. traffic",
            trafficState = gc.horTraffic.value,
            onValueSet = gc::changeHorTraffic
            )
        Spacer( Modifier.height( 5.dp ) )
        TrafficSlider(
            name = "ver. traffic",
            trafficState = gc.verTraffic.value,
            onValueSet = gc::changeVerTraffic
            )
    }
    // endregion

    // region tick pane
    SeeThroughPane (
        offset = Pair( 435.dp, 20.dp )
        )
    {
        fun getText(
            desc : String,
            text : String
            ) : AnnotatedString
        {
            return buildAnnotatedString {
                append( "$desc: " )
                pushStyle( SpanStyle( color = Color.White ) )
                append( text )
            }
        }

        Column( Modifier.width( 120.dp ) )
        {
            DescText( getText(
                desc = "traffic tick",
                text = "${gc.trafficTickPercent.value}%"
                ) )
            Spacer( Modifier.height( 4.dp ) )
            DescText( getText(
                desc = "lights tick",
                text = "${gc.lightTickPercent.value}%"
                ) )
            DescText( getText(
                desc = "tick kind",
                text = gc.lightTickKind.value.toString().lowercase()
                ) )
        }
    }
    // endregion
}