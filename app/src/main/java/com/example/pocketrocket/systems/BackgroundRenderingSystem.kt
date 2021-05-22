package com.example.pocketrocket.systems

import android.graphics.*
import android.graphics.Paint.DITHER_FLAG
import com.example.pocketrocket.components.*
import com.example.pocketrocket.managers.ECSCallback
import java.util.*

enum class GradientType {
    LINEAR,
    RADIAL,
    SWEEP
}

class BackgroundRenderingSystem(callback: ECSCallback) : GameSystem(callback) {
    override fun appliesToSignature(signature: BitSet): Boolean {
        return signature.get(BackgroundComponent.componentID) &&
                (signature.get(ColorComponent.componentID) || signature.get(BitmapComponent.componentID))
    }

    private val backgroundPaint: Paint = Paint(DITHER_FLAG).also {
        it.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
    }

    fun drawBackground(canvas: Canvas) {
        for (eid in entityList) {
            val bkgComp = callback.getComponent<BackgroundComponent>(eid, BackgroundComponent.componentID)
            val bitmapComp = callback.getComponentOrNull<BitmapComponent>(eid, BitmapComponent.componentID)
            if (bitmapComp != null)
                canvas.drawBitmap(bitmapComp.bitmap!!, 0f, 0f, backgroundPaint)
            val color = callback.getComponentOrNull<ColorComponent>(eid, ColorComponent.componentID)
            if (color != null)
                canvas.drawColor(color.color, bkgComp.drawMode)
        }
    }

    fun createGradientBitmap(size: Rect, colors: Collection<Int>, type: GradientType): Bitmap {
        val bitmap = Bitmap.createBitmap(size.width(), size.height(), Bitmap.Config.ARGB_8888)
        backgroundPaint.shader = when (type) {
            GradientType.LINEAR -> LinearGradient(
                0f, 0f,
                callback.getScreenProperties().width.toFloat(), callback.getScreenProperties().height.toFloat(),
                colors.toIntArray(), null,
                Shader.TileMode.CLAMP
            )
            GradientType.RADIAL -> RadialGradient(
                callback.getScreenProperties().width * 0.5f, callback.getScreenProperties().height * 0.5f,
                1f,
                colors.toIntArray(), null,
                Shader.TileMode.CLAMP
            )
            GradientType.SWEEP -> SweepGradient(
                callback.getScreenProperties().width * 0.5f, callback.getScreenProperties().height * 0.5f,
                colors.toIntArray(), null
            )
        }
        val canvas = Canvas(bitmap)
        canvas.drawRect(0f, 0f, size.width().toFloat(), size.height().toFloat(), backgroundPaint)
        return bitmap
    }
}