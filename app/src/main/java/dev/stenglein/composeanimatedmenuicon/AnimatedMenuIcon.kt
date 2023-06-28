package dev.stenglein.composeanimatedmenuicon

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


/**
 * Animated menu icon that animates between open and closed state.
 *
 * The icon consists of three bars that are animated to form an X when the menu is opened.
 *
 * @param isMenuOpened Whether the menu is open or closed.
 * @param modifier Modifier for the menu icon. Use the [size] parameter to set the size.
 * @param sizeInDp Size of the menu icon in dp.
 * @param foregroundColor Color of the menu icon.
 */
@Composable
fun AnimatedMenuIcon(
    isMenuOpened: Boolean,
    modifier: Modifier = Modifier,
    sizeInDp: Dp = 24.dp,
    foregroundColor: Color = LocalContentColor.current
) {
    // Shared transition between the opened and closed state
    val transition = updateTransition(targetState = isMenuOpened, label = "MenuTransition")

    // Shared tween spec for the transition
    @Composable
    fun tweenSpec(): @Composable (Transition.Segment<Boolean>.() -> FiniteAnimationSpec<Float>) =
        { tween(300) }

    // Calculate size in pixels
    val size = with(LocalDensity.current) { sizeInDp.toPx() }

    // Animated alpha for middle bar
    val middleBarAlpha by transition.animateFloat(
        transitionSpec = tweenSpec(), label = "Middle menu bar animation"
    ) { isOpen ->
        if (isOpen) 0f else 1f
    }

    // Density to convert dp to px
    val density = LocalDensity.current

    // Height of the bars
    val barHeight = with(density) { (sizeInDp / 12).toPx() }

    // Half the height of the bars (used for calculating the y coordinates)
    val halfBarHeight = barHeight / 2

    // Bars take up 3 * barHeight
    // -> Remaining spacing is size - barHeight * 3
    // Add double the padding above the top and below the bottom bar, and add single padding between
    // the bars -> Divide by 6 -> (size - barHeight * 3) / 6 = size / 6 - barHeight / 2
    val spacing = size / 6 - barHeight / 2

    // y coordinates of the bars in closed state
    val yFirst = halfBarHeight + 2 * spacing
    val ySecond = barHeight + halfBarHeight + 3 * spacing
    val yThird = 2 * barHeight + halfBarHeight + 4 * spacing

    // Horizontal spacing to left and right of the bars (calculated to make it look similar to the
    // original material menu icon)
    val horizontalSpacing = spacing + barHeight / 2

    // Start x position of the outer bars (closed menu icon is wider than open menu icon)
    val xOuterBarsStart by transition.animateFloat(
        transitionSpec = tweenSpec(), label = "Outer menu bars start x animation"
    ) { isOpen ->
        // Use yFirst in open state to make icon quadratic
        if (isOpen) yFirst else horizontalSpacing
    }

    // End x position of the outer bars (closed menu icon is wider than open menu icon)
    val xOuterBarsEnd by transition.animateFloat(
        transitionSpec = tweenSpec(), label = "Outer menu bars end x animation"
    ) { isOpen ->
        // Use size - yFirst in open state to make icon quadratic
        if (isOpen) size - yFirst else size - horizontalSpacing
    }

    // Start x position of the middle bar
    val xMiddleStart by transition.animateFloat(
        transitionSpec = tweenSpec(), label = "Middle menu bar start x animation"
    ) { isOpen ->
        if (isOpen) size / 2 else horizontalSpacing
    }

    // End x position of the middle bar
    val xMiddleEnd by transition.animateFloat(
        transitionSpec = tweenSpec(), label = "Middle menu bar end x animation"
    ) { isOpen ->
        if (isOpen) size / 2 else size - horizontalSpacing
    }

    // End y position of the first bar
    val yFirstBarEnd by transition.animateFloat(
        transitionSpec = tweenSpec(), label = "First menu bar end y animation"
    ) { isOpen ->
        if (isOpen) yThird else yFirst
    }

    // End y position of the third bar
    val yThirdBarEnd by transition.animateFloat(
        transitionSpec = tweenSpec(), label = "Third menu bar end y animation"
    ) { isOpen ->
        if (isOpen) yFirst else yThird
    }

    // Starts and ends of the bars
    val xStart = listOf(xOuterBarsStart, xMiddleStart, xOuterBarsStart)
    val xEnd = listOf(xOuterBarsEnd, xMiddleEnd, xOuterBarsEnd)
    val yStart = listOf(yFirst, ySecond, yThird)
    val yEnds = listOf(yFirstBarEnd, ySecond, yThirdBarEnd)

    // Alpha values of the bars
    val alpha = listOf(1f, middleBarAlpha, 1f)

    // Draw the menu icon
    Canvas(
        modifier = Modifier
            .size(sizeInDp)
            .then(modifier)  // Apply the modifier passed to the composable
    ) {
        // Draw the bars
        for (i in 0..2) {
            drawLine(
                color = foregroundColor,
                start = Offset(xStart[i], yStart[i]),
                end = Offset(xEnd[i], yEnds[i]),
                strokeWidth = barHeight,
                cap = StrokeCap.Round,
                alpha = alpha[i]
            )
        }
    }
}


@Preview
@Composable
fun AnimatedMenuIconPreview() {
    AnimatedMenuIcon(isMenuOpened = true, sizeInDp = 192.dp)
}