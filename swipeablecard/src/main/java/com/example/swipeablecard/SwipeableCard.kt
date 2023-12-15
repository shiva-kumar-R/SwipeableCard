package com.example.swipeablecard

import androidx.compose.ui.Modifier
import kotlinx.coroutines.coroutineScope
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * Have Tinder like gestures
 * @param state current state of the card
 * @param onSwiped will be called when swipe is completed with direction
 * @param onSwipeCancelled will be called if swipe is not reached minimum offset
 * @param blockedDirections list of directions where swipe is to be blocked
 */

fun Modifier.swipeableCard(
    state: SwipeableCardState,
    onSwiped: (Direction) -> Unit,
    onSwipeCancelled: () -> Unit,
    blockedDirections: List<Direction> = listOf(Direction.Up, Direction.Down)
) = pointerInput(Unit) {
    coroutineScope {
        detectDragGestures(
            onDragEnd = {
                launch {
                    if (notSwiped(state, blockedDirections)) {
                        state.reset()
                        onSwipeCancelled()
                    } else {
                        val hRef = abs(state.offset.targetValue.x)
                        val vRef = abs(state.offset.targetValue.y)

                        if (hRef > vRef) {
                            if (state.offset.targetValue.x > 0) {
                                state.swipe(Direction.Right)
                                onSwiped(Direction.Right)
                            } else {
                                state.swipe(Direction.Left)
                                onSwiped(Direction.Left)
                            }
                        } else {
                            if (state.offset.targetValue.y > 0) {
                                state.swipe(Direction.Down)
                                onSwiped(Direction.Down)
                            } else {
                                state.swipe(Direction.Up)
                                onSwiped(Direction.Up)
                            }
                        }
                    }
                }
            },
            onDragCancel = {
                launch {
                    state.reset()
                    onSwipeCancelled()
                }
            },
            onDrag = { change, dragAmount ->
                launch {
                    val original = state.offset.targetValue
                    val newOffset = original + dragAmount
                    val newValue = Offset(
                        x = newOffset.x.coerceIn(
                            -state.width, state.width
                        ),
                        y = newOffset.y.coerceIn(
                            -state.height, state.height
                        )
                    )
                    if (change.positionChange() != Offset.Zero) change.consume()
                    state.drag(newValue.x, newValue.y)
                }
            }
        )
    }
}.graphicsLayer {
    translationX = state.offset.value.x
    translationY = state.offset.value.y
    rotationZ = (state.offset.value.x / 60).coerceIn(-40f, 40f)
}

private fun Offset.coerceIn(
    blockedDirections: List<Direction>,
    maxHeight: Float,
    maxWidth: Float
) = copy(
    x = x.coerceIn(
        if (blockedDirections.contains(Direction.Left)) 0f else -maxWidth,
        if (blockedDirections.contains(Direction.Right)) 0f else maxWidth
    ),
    y = y.coerceIn(
        if (blockedDirections.contains(Direction.Up)) 0f else -maxHeight,
        if (blockedDirections.contains(Direction.Down)) 0f else maxHeight
    )
)

private fun notSwiped(state: SwipeableCardState, blockedDirections: List<Direction>): Boolean {
    val coercedOffSet =
        state.offset.targetValue.coerceIn(blockedDirections, state.height, state.width)

    return abs(coercedOffSet.x) < state.width / 4 &&
            abs(coercedOffSet.y) < state.height / 4
}


