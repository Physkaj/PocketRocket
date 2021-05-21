package com.example.pocketrocket.systems

import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import com.example.pocketrocket.components.BackgroundComponent
import com.example.pocketrocket.components.ColorComponent
import com.example.pocketrocket.components.GradientComponent
import com.example.pocketrocket.components.GradientType
import com.example.pocketrocket.managers.ECSCallback
import java.util.*

class BackgroundRenderingSystem(callback: ECSCallback) : GameSystem(callback) {
    override fun appliesToSignature(signature: BitSet): Boolean {
        return signature.get(BackgroundComponent.componentID)
    }

    private val backgroundPaint: Paint = Paint()

    fun drawBackground(canvas: Canvas) {
        for (eid in entityList) {
            val background = callback.getComponent<BackgroundComponent>(eid, BackgroundComponent.componentID)
            val gradient = callback.getComponentOrNull<GradientComponent>(eid, GradientComponent.componentID)
            if (gradient != null) {

                if (gradient.shader == null)
                    gradient.shader = createNewShader(gradient)
                backgroundPaint.shader = gradient.shader
                canvas.drawRect(0f, 0f, callback.getScreenProperties().width, callback.getScreenProperties().height, backgroundPaint)
            }
            val color = callback.getComponentOrNull<ColorComponent>(eid, ColorComponent.componentID)
            if (color != null) {
                canvas.drawColor(color.color, background.drawMode)
            }
        }
    }

    private fun createNewShader(gradientComp: GradientComponent): Shader {
        return when (gradientComp.gradientType) {
            GradientType.LINEAR -> LinearGradient(
                0f, 0f,
                callback.getScreenProperties().width, callback.getScreenProperties().height,
                gradientComp.colors.toIntArray(), null,
                Shader.TileMode.CLAMP
            )
            GradientType.RADIAL -> RadialGradient(
                callback.getScreenProperties().width * 0.5f, callback.getScreenProperties().height * 0.5f,
                1f,
                gradientComp.colors.toIntArray(), null,
                Shader.TileMode.CLAMP
            )
            GradientType.SWEEP -> SweepGradient(
                callback.getScreenProperties().width * 0.5f, callback.getScreenProperties().height * 0.5f,
                gradientComp.colors.toIntArray(), null
            )
        }
    }
}