package com.s452635.beatrice.frame

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.s452635.beatrice.simulation.LightState

@Composable
fun LightSprite (
    lightState : LightState,
    offset : Pair<Dp,Dp>
    )
{
    fun getLightImage(): ImageBitmap
    {
        return when( lightState )
        {
            LightState.Yellow ->
                useResource( "light-yellow.png", ::loadImageBitmap )
            LightState.Green ->
                useResource( "light-green.png", ::loadImageBitmap )
            LightState.RedYellow ->
                useResource( "light-red-yellow.png", ::loadImageBitmap )
            LightState.Red ->
                useResource( "light-red.png", ::loadImageBitmap )
        }
    }

    Image(
        bitmap = getLightImage(),
        contentDescription = null,
        modifier = Modifier
            .offset( offset.first, offset.second )
            .height( 54.dp )
            .width( 24.dp )
    )
}

@Composable
fun PlateSprite (
    powered : Boolean,
    offset : Pair<Dp,Dp>,
    rotation : Float
    )
{
    fun getPlateImage() : ImageBitmap
    {
        return when( powered )
        {
            true ->
                useResource( "plate-on.png", ::loadImageBitmap )
            false ->
                useResource( "plate-off.png", ::loadImageBitmap )
        }
    }

    Image(
        bitmap = getPlateImage(),
        contentDescription = null,
        modifier = Modifier
            .offset( offset.first, offset.second )
            .rotate( rotation )
        )
}

@Composable
fun ArrowSprite (
    visible : Boolean,
    offset : Pair<Dp, Dp>,
    rotation : Float
    )
{
    Image(
        bitmap = useResource( "arrow.png", ::loadImageBitmap ),
        contentDescription = null,
        alpha = if( visible ) 1.0f else 0.0f,
        modifier = Modifier
            .offset( offset.first, offset.second )
            .rotate( rotation )
        )
}
