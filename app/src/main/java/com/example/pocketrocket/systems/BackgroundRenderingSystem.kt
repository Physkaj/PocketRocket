package com.example.pocketrocket.systems

import android.graphics.*
import com.example.pocketrocket.components.*
import com.example.pocketrocket.entity.EidType
import com.example.pocketrocket.managers.ECSCallback
import java.lang.RuntimeException
import java.util.*


class BackgroundRenderingSystem(callback: ECSCallback) : GameSystem(callback) {
    override fun appliesToSignature(signature: BitSet): Boolean {
        return signature.get(BackgroundComponent.componentID) &&
                (signature.get(ColorComponent.componentID) || signature.get(BitmapComponent.componentID))
    }

    private val backgroundPaint: Paint = Paint().also {
        // Ignore transparency
        it.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
    }

    fun drawBackground(canvas: Canvas) {
        for (eid in entityList) {
            val bkgComp = callback.getComponent<BackgroundComponent>(eid, BackgroundComponent.componentID)
            val bitmapComp = callback.getComponentOrNull<BitmapComponent>(eid, BitmapComponent.componentID)
            if (bitmapComp != null) {
                if (bitmapComp.bitmap == null) {
                    bitmapComp.bitmap = generateBackgroundBitmap(eid)
                }
                canvas.drawBitmap(bitmapComp.bitmap!!, 0f, 0f, backgroundPaint)
            }
            val color = callback.getComponentOrNull<ColorComponent>(eid, ColorComponent.componentID)
            if (color != null)
                canvas.drawColor(color.color, bkgComp.drawMode)
        }
    }

    private fun generateBackgroundBitmap(eid: EidType): Bitmap {
        val gradientComp = callback.getComponentOrNull<GradientComponent>(eid, GradientComponent.componentID)
        if (gradientComp != null)
            return createGradientBitmap(gradientComp)
        throw(RuntimeException("No available component to generate bitmap from"))
    }

    fun resizeBitmaps(width: Int, height: Int) {
        for (eid in entityList) {
            callback.getComponentOrNull<BitmapComponent>(eid, BitmapComponent.componentID)?.let { bmC ->
                bmC.bitmap = null // It will be regenerated next tick
                callback.getComponentOrNull<GradientComponent>(eid, GradientComponent.componentID)?.let { gC ->
                    gC.bitmapWidth = width
                    gC.bitmapHeight = height
                }
            }
        }
    }

    private fun createGradientBitmap(gC: GradientComponent): Bitmap {
        val bitmap = Bitmap.createBitmap(gC.bitmapWidth, gC.bitmapHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val screen = callback.getScreenProperties()
        val bitmapRect = Rect(0, 0, gC.bitmapWidth, gC.bitmapHeight)
        val from = screen.screenCoordinates(gC.gradientFrom, bitmapRect)
        when (gC.gradientType) {
            GradientType.LINEAR -> {
                val to = screen.screenCoordinates(gC.gradientTo, bitmapRect)
                backgroundPaint.shader = LinearGradient(
                    from.x, from.y, to.x, to.y,
                    gC.colors.toIntArray(), gC.colorPositions?.toFloatArray(),
                    Shader.TileMode.CLAMP
                )
                canvas.drawRect(bitmapRect, backgroundPaint)
            }
            GradientType.RADIAL -> {
                val r0 = screen.screenRadius(gC.gradientR[0], bitmapRect)
                val r1 = screen.screenRadius(gC.gradientR[1], bitmapRect)
                backgroundPaint.shader = RadialGradient(
                    from.x, from.y, r1,
                    gC.colors.toIntArray(), gC.colorPositions?.toFloatArray(),
                    Shader.TileMode.CLAMP
                )
                canvas.drawRect(bitmapRect, backgroundPaint)
                // Save mode
                val xfer = backgroundPaint.xfermode
                backgroundPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                canvas.drawCircle(from.x, from.y, r0, backgroundPaint)
                // Restore mode
                backgroundPaint.xfermode = xfer
            }
            GradientType.SWEEP -> {
                val r0 = screen.screenRadius(gC.gradientR[0], bitmapRect)
                backgroundPaint.shader = SweepGradient(
                    from.x, from.y,
                    gC.colors.toIntArray(), gC.colorPositions?.toFloatArray()
                )
                canvas.drawRect(bitmapRect, backgroundPaint)
                if (r0 > 0f) {
                    // Save mode
                    val xfer = backgroundPaint.xfermode
                    backgroundPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

                    canvas.drawCircle(from.x, from.y, r0, backgroundPaint)
                    // Restore mode
                    backgroundPaint.xfermode = xfer
                }
            }
        }
        return bitmap
    }
}