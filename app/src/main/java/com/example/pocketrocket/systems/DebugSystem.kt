package com.example.pocketrocket.systems

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import com.example.pocketrocket.components.*
import com.example.pocketrocket.entity.EidType
import com.example.pocketrocket.managers.ECSCallback
import java.util.*

class DebugSystem(callback: ECSCallback) : GameSystem(callback) {
    val debugInfoEntity: EidType = callback.createEntity()

    init {
        // Debug info
        with(callback) {
            addComponent<DebugComponent>(debugInfoEntity, DebugComponent.componentID).debug = true
            addComponent<TextComponent>(debugInfoEntity, TextComponent.componentID).let {
                it.text = "FPS"
                it.textSize = 50f
                it.textColor = Color.GREEN
                it.useRelativeCoordinates = false
                it.textPosition.x = -getScreenProperties().width / getScreenProperties().height.toFloat()
                it.textPosition.y = 0.9f
                it.typeface = Typeface.MONOSPACE
            }
        }
    }

    override fun appliesToSignature(signature: BitSet): Boolean {
        return signature.get(DebugComponent.componentID)
    }

    fun doDebugStuff() {
        for (eid in entityList) {
            if (eid == debugInfoEntity) {
                val textComp = callback.getComponent<TextComponent>(debugInfoEntity, TextComponent.componentID)
                textComp.textPosition.x = -callback.getScreenProperties().width / callback.getScreenProperties().height.toFloat()
                textComp.textPosition.y = 0.9f
                val (fps, ups, _) = callback.getThreadProperties()
                textComp.text = "FPS: $fps\nUPS: $ups"
            }
        }
    }
}