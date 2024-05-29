package dev.abbasian.applyhistory.ui.component

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

class CurvedBorder(private val cornerRadius: Dp) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val cornerRadius = with(density) { cornerRadius.toPx() }
        return Outline.Generic(
            path = blurPath(size = size, cornerRadius = cornerRadius)
        )
    }
}