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

            val screen = callback.getScreenProperties()
            // Transform into screen coordinates
            val (x, y) = screen.screenCoordinates(position.x, position.y)
            // Draw shape
            when (shape.shapeType) {
                ShapeComponent.ShapeType.CIRCLE -> {
                    val r = screen.screenRadius(shape.r)
                    canvas.drawCircle(x, y, r, paint)
                }
                ShapeComponent.ShapeType.LINE -> {
                    val (x1, y1) = screen.screenCoordinates(shape.x, shape.y)
                    canvas.drawLine(x, y, x1, y1, paint)
                }
                ShapeComponent.ShapeType.RECTANGLE -> {
                    val (x1, y1) = screen.screenCoordinates(shape.x, shape.y)
                    canvas.drawRect(x, y, x1, y1, paint)
                }
            }
        }
    }
}
