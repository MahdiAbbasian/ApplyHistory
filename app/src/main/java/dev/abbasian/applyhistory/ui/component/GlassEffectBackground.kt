package dev.abbasian.applyhistory.ui.component

import android.graphics.Bitmap
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import dev.abbasian.applyhistory.R
import dev.abbasian.applyhistory.ui.theme.SetBackground

@Composable
fun GlassEffectBackground(content: @Composable () -> Unit) {
    Box(modifier = Modifier.wrapContentSize()) {
        SetBackground()
        val source = ImageBitmap.imageResource(id = R.drawable.apply_history).asAndroidBitmap()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            val blurredBitmap = legacyBlurImage(
                source = source,
                blurRadius = 25f
            )
            BlurImage(
                bitmap = blurredBitmap,
                modifier = Modifier
                    .wrapContentSize()
                    .graphicsLayer(
                        scaleX = 1.0f,
                        scaleY = 1.0f
                    )
            )
        } else {
            BlurImage(
                bitmap = source,
                modifier = Modifier
                    .blur(radius = 10.dp, edgeTreatment = BlurredEdgeTreatment.Rectangle)
                    .wrapContentSize()
                    .graphicsLayer(
                        scaleX = 1.0f,
                        scaleY = 1.0f
                    )
            )
        }

        Box(modifier = Modifier.wrapContentSize()) {
            content()
        }
    }
}

@Suppress("DEPRECATION")
@Composable
private fun legacyBlurImage(
    source: Bitmap,
    blurRadius: Float = 25f,
    blurLayer: Int = 1,
): Bitmap {
    val bitmap = source.copy(Bitmap.Config.ARGB_8888, true)
    val renderScript = RenderScript.create(LocalContext.current)
    for (i in 0 until blurLayer) {
        val bitmapAlloc = Allocation.createFromBitmap(renderScript, bitmap)
        ScriptIntrinsicBlur.create(renderScript, bitmapAlloc.element).apply {
            setRadius(blurRadius)
            setInput(bitmapAlloc)
            forEach(bitmapAlloc)
        }
        bitmapAlloc.copyTo(bitmap)
    }
    renderScript.destroy()
    return bitmap
}

@Composable
private fun BlurImage(
    modifier: Modifier = Modifier,
    bitmap: Bitmap,
) {
    val noiseBitmap = ImageBitmap.imageResource(R.drawable.noise)
    val cornerRadius = 24.dp
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        modifier = modifier
            .padding(4.dp)
            .clip(CurvedBorder(cornerRadius = cornerRadius))
            .drawWithCache {
                onDrawWithContent {
                    // Todo: for simple blur, remove all except "this.drawContent()"
                    // Content
                    this.drawContent()
                    // Noise filter
                    drawImage(
                        image = noiseBitmap,
                        alpha = .01f
                    )
                    // Light filter
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0x65F3F0F0), Color(0x19BBBABA)),
                            center = Offset(
                                100f,
                                size.height
                                    .div(2)
                                    .plus(100f)
                            ),
                            radius = size.height
                        ),
                        blendMode = BlendMode.Luminosity,
                    )
                    // Border
                    drawPath(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0x80FFFFFF), Color(0x65AAA7A7)),
                            start = Offset(1000f, 100f),
                            end = Offset.Infinite
                        ),
                        path = blurPath(size, cornerRadius.toPx()),
                        style = Stroke(1.5.dp.toPx()),
                        blendMode = BlendMode.Luminosity
                    )
                }
            }
    )
}

fun blurPath(size: Size, cornerRadius: Float): Path {
    return Path().apply {
        moveTo(cornerRadius, size.height.div(2))
        lineTo(size.width - cornerRadius, size.height.div(2))
        arcTo(
            rect = Rect(
                left = size.width - cornerRadius * 2,
                top = size.height.div(2),
                right = size.width,
                bottom = size.height.div(2) + cornerRadius * 2
            ),
            startAngleDegrees = 270f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )
        lineTo(size.width, size.height - cornerRadius)
        arcTo(
            rect = Rect(
                left = size.width - cornerRadius * 2,
                top = size.height - cornerRadius * 2,
                right = size.width,
                bottom = size.height
            ),
            startAngleDegrees = 0f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )
        lineTo(cornerRadius, size.height)
        arcTo(
            rect = Rect(
                left = 0f,
                top = size.height - cornerRadius * 2,
                right = cornerRadius * 2,
                bottom = size.height
            ),
            startAngleDegrees = 90f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )
        lineTo(0f, size.height.div(2) + cornerRadius)
        arcTo(
            rect = Rect(
                left = 0f,
                top = size.height.div(2),
                right = cornerRadius * 2,
                bottom = size.height.div(2) + cornerRadius * 2
            ),
            startAngleDegrees = 180f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )
        close()
    }
}