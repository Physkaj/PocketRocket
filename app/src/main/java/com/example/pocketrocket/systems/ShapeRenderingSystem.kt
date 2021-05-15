package com.example.pocketrocket.systems

import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import com.example.pocketrocket.components.*
import com.example.pocketrocket.managers.ECSCallback
import java.util.*

class ShapeRenderingSystem(callback: ECSCallback) : GameSystem(callback) {
    private val paint: Paint = Paint()
    override fun appliesToSignature(signature: BitSet): Boolean {
        return signature.get(PositionComponent.componentID) &&
                signature.get(ShapeComponent.componentID) &&
                signature.get(ColorComponent.componentID)
    }

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
    Pair<Float, Float>(canvas.width * 0.5f + canvas.height * 0.5f * x, canvas.height * 0.5f * (1f - y))

fun screenRadius(r: Float, canvas: Canvas) = canvas.height * 0.5f * r
fun ShapeComponent.screenCoordinates(canvas: Canvas) =
    com.example.pocketrocket.systems.screenCoordinates(this.x, this.y, canvas)

fun ShapeComponent.screenRadius(canvas: Canvas) =
    com.example.pocketrocket.systems.screenRadius(this.r, canvas)

fun PositionComponent.screenCoordinates(canvas: Canvas) =
    com.example.pocketrocket.systems.screenCoordinates(this.x, this.y, canvas)
