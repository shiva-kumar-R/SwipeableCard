package com.example.swipeablecard

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp


@Composable
fun rememberSwipeableCardState(): SwipeableCardState {
    val screenWidth = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }

    val screenHeight = with(LocalDensity.current) {
        LocalConfiguration.current.screenHeightDp.dp.toPx()
    }

    return remember {
        SwipeableCardState(screenWidth, screenHeight)
    }
}


class SwipeableCardState(
    internal val width: Float,
    internal val height: Float
) {
    val offset = Animatable(offset(0f, 0f), Offset.VectorConverter)

    var swipedDirection: Direction? by mutableStateOf(null)
        private set

    suspend fun swipe(direction: Direction, animationSpec: AnimationSpec<Offset> = tween(500)) {
        val endX = width * 1.5F
        val endY = height

        when (direction) {
            Direction.Up -> offset.animateTo(offset(y = endY), animationSpec)
            Direction.Down -> offset.animateTo(offset(y = -endY), animationSpec)
            Direction.Left -> offset.animateTo(offset(x = -endX), animationSpec)
            Direction.Right -> offset.animateTo(offset(x = endX), animationSpec)
        }
        swipedDirection = direction
    }

    internal suspend fun reset() = offset.animateTo(offset(0f, 0f), tween(500))

    internal suspend fun drag(x: Float, y: Float) = offset.animateTo(offset(x, y))

    private fun offset(x: Float = offset.value.x, y: Float = offset.value.y): Offset = Offset(x, y)
}