package com.example.pocketrocket.systems

import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import com.example.pocketrocket.components.ColorComponent
import com.example.pocketrocket.components.PositionComponent
import com.example.pocketrocket.components.ShapeComponent
import com.example.pocketrocket.managers.ECSCallback
import java.util.*

class ShapeRenderingSystem(callback: ECSCallback) : GameSystem(callback) {
    private val paint: Paint = Paint()
    override fun appliesToSignature(signature: BitSet): Boolean {
        return signature.get(PositionComponent.componentID) &&
                signature.get(ShapeComponent.componentID) &&
                signature.get(ColorComponent.componentID)
    }

    val linearGradient = LinearGradient(0f, 0f, 1f, 1f, IntArray(2) { it }, null, Shader.TileMode.CLAMP)

    fun activate(canvas: Canvas) {
        for (eid in entityList) {
            val position = callback.getComponent<PositionComponent>(eid, PositionComponent.componentID)
            val shape = callback.getComponent<ShapeComponent>(eid, ShapeComponent.componentID)
            val color = callback.getComponent<ColorComponent>(eid, ColorComponent.componentID)

            // Setup paint
            paint.color = color.color

            // Transform into screen coordinates
            val (x, y) = position.screenCoordinates(canvas)
            // Draw shape
            when (shape.shapeType) {
                ShapeComponent.ShapeType.CIRCLE -> {
                    val r = shape.screenRadius(canvas)
                    canvas.drawCircle(x, y, r, paint)
                }
                ShapeComponent.ShapeType.LINE -> {
                    val (x1, y1) = shape.screenCoordinates(canvas)
                    canvas.drawLine(x, y, x1, y1, paint)
                }
                ShapeComponent.ShapeType.RECTANGLE -> {
                    val (x1, y1) = shape.screenCoordinates(canvas)
                    canvas.drawRect(x, y, x1, y1, paint)
                }
            }
        }
    }
}

fun screenCoordinates(x: Float, y: Float, canvas: Canvas) =
    Pair<Float, Float>(canvas.width * 0.5f * (1f + x), canvas.height * 0.5f * (1f - y))

fun PositionComponent.screenCoordinates(canvas: Canvas) =
    Pair<Float, Float>(canvas.width * 0.5f * (1f + this.x), canvas.height * 0.5f * (1f - this.y))

fun ShapeComponent.screenCoordinates(canvas: Canvas) =
    Pair<Float, Float>(canvas.width * 0.5f * (1f + this.x), canvas.height * 0.5f * (1f - this.y))

fun screenRadius(r: Float, canvas: Canvas) = canvas.height * 0.5f * r
fun ShapeComponent.screenRadius(canvas: Canvas) = canvas.height * 0.5f * this.r