package com.example.pocketrocket.systems

import android.graphics.Canvas
import android.graphics.Paint
import com.example.pocketrocket.components.*
import com.example.pocketrocket.managers.ECSCallback
import java.util.*


class TextRenderingSystem(callback: ECSCallback) : GameSystem(callback) {
    override fun appliesToSignature(signature: BitSet): Boolean {
        return signature.get(TextComponent.componentID)
    }

    private val textPaint: Paint = Paint()

    fun drawTexts(canvas: Canvas) {
        for (eid in entityList) {
            val textComp = callback.getComponent<TextComponent>(eid, TextComponent.componentID)
            var position = textComp.textPosition.copy()
            if (textComp.useRelativeCoordinates) {
                val parentEid = callback.getComponent<ParentComponent>(eid, ParentComponent.componentID).parentEid
                position.plusAssign(callback.getComponent<PositionComponent>(parentEid, PositionComponent.componentID).pos.to2D())
                // Need parent width and height to do this properly
            }

            // Setup paint
            textPaint.color = textComp.textColor
            textPaint.textSize = textComp.textSize
            textPaint.typeface = textComp.typeface

            position = callback.getScreenProperties().screenCoordinates(position)

            for (line in textComp.text.split("\n")) {
                canvas.drawText(line, position.x, position.y, textPaint)
                position.y += (textPaint.descent() - textPaint.ascent()) * textComp.lineSpacing
            }
        }
    }
}