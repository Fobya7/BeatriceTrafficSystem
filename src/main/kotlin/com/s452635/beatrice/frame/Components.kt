package com.s452635.beatrice.frame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.s452635.beatrice.simulation.TrafficFlow

@Suppress( "unused" )
@Composable
fun DescText( text : String ) { DescText( AnnotatedString(text) ) }
@Composable
fun DescText(
    text : AnnotatedString
)
{
    Text(
        text = text,
        color = MixedColors.OffWhite,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        letterSpacing = 1.sp
        )
}

@Composable
fun TrafficSlider(
    name : String,
    trafficFlow : TrafficFlow,
    onValueSet : (TrafficFlow) -> Unit
    )
{
    var percent by remember { mutableStateOf(0.0f) }
    fun percentToTraffic() : TrafficFlow
    {
        return when( percent.toInt() )
        {
            0    -> TrafficFlow.None
            25   -> TrafficFlow.Some
            50   -> TrafficFlow.Mild
            75   -> TrafficFlow.Busy
            100  -> TrafficFlow.Heavy
            else -> TrafficFlow.None
        }
    }
    var text by remember { mutableStateOf(AnnotatedString("")) }
    fun updateText()
    {
        fun getColor() : Color
        {
            return when( trafficFlow )
            {
                TrafficFlow.None -> Color.White
                TrafficFlow.Some -> Color.Yellow
                TrafficFlow.Mild -> MixedColors.Orange
                TrafficFlow.Busy -> MixedColors.ReddishOrange
                TrafficFlow.Heavy -> Color.Red
            }
        }

        text = buildAnnotatedString {
            append( "$name: " )
            pushStyle( SpanStyle( color = getColor() ) )
            append( trafficFlow.name.lowercase() )
        }
    }

    updateText()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width( 150.dp )
        )
    {
        DescText (
            text = text,
            )
        Spacer( Modifier.height( 2.dp ) )
        Slider (
            value = percent,
            onValueChange = { percent = it },
            onValueChangeFinished = {
                onValueSet(percentToTraffic())
                updateText()
                },
            steps = 3,
            valueRange = 0.0f..100.0f,
            colors = SliderDefaults.colors(
                thumbColor = MixedColors.AlmostBlack,
                activeTrackColor = MixedColors.OffWhite,
                inactiveTrackColor = MixedColors.OffWhite,
                activeTickColor = MixedColors.AlmostBlack,
                inactiveTickColor = MixedColors.AlmostBlack
                ),
            modifier = Modifier
                .background(
                    color = MixedColors.OffWhite,
                    shape = RoundedCornerShape( 12.dp )
                    )
                .padding( 2.dp )
                .height( 20.dp )
            )
    }
}

@Composable
fun CarButton(
    queueCount : Int,
    offset : Pair<Dp,Dp>,
    onClick : () -> Unit
    )
{
    fun getText() : AnnotatedString
    {
        return buildAnnotatedString {
            append( "queue: " )
            pushStyle( SpanStyle( color = Color(
                red = 255,
                green = if( queueCount <= 5 ) 255
                    else maxOf( 0, 255-(queueCount-5)*10 ),
                blue = if( queueCount <= 5 ) 255 else 0
                ) ) )
            append( "$queueCount" )
            pop()
        }
    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .offset( offset.first, offset.second )
            .wrapContentHeight()
            .width( IntrinsicSize.Max )
            .background(
                color = MixedColors.TooltipBackgroundBlack,
                shape = RoundedCornerShape( 12.dp, 12.dp, 0.dp, 0.dp )
                )
        )
    {
        Button (
            content = { Text(
                text = "SEND CAR",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = MixedColors.AlmostBlack,
                letterSpacing = 1.sp
                ) },
            onClick = onClick,
            shape = RoundedCornerShape( 14.dp ),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MixedColors.OffWhite
                ),
            contentPadding = PaddingValues( 0.dp ),
            modifier = Modifier
                .height( 26.dp )
                .width( 90.dp )
            )
        Text (
            text = getText(),
            color = MixedColors.OffWhite,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            letterSpacing = 1.sp,
            modifier = Modifier
                .padding( 0.dp, 3.dp, 0.dp, 5.dp )
            )
    }
}

@Composable
fun SeeThroughPane(
    offset : Pair<Dp, Dp>,
    content : @Composable (ColumnScope.() -> Unit)
    )
{
    Column (
        content = content,
        modifier = Modifier
            .offset( offset.first, offset.second )
            .background(
                color = MixedColors.TooltipBackgroundBlack,
                shape = RoundedCornerShape( 5.dp )
                )
            .padding( 10.dp )
        )
}

object MixedColors
{
    val OffWhite = Color( 204, 204, 204 )
    val AlmostBlack = Color( 51, 51, 51 )
    val TooltipBackgroundBlack =
        Color( 10, 10, 10, 130 )
    val Orange = Color( 255, 173, 41 )
    val ReddishOrange = Color( 255, 123, 41 )
}