package com.example.pocketrocket.components

import com.example.pocketrocket.entity.EidType

class ParentComponent(
    var parentEid: EidType = -1,
    var relativePosition: ChildPositionType = ChildPositionType.NONE
) : IGameComponent by Companion {
    companion object : GameComponentCompanion()
    enum class ChildPositionType {
        NONE,
        INSIDE,
        OFFSET
    }
}