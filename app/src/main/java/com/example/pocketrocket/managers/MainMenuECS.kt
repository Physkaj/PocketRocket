package com.example.pocketrocket.managers

import android.graphics.Canvas
import android.graphics.Color
import com.example.pocketrocket.components.*
import com.example.pocketrocket.systems.OrbitalMotion
import com.example.pocketrocket.systems.ShapeRenderingSystem
import com.example.pocketrocket.systems.StarSpawningSystem

class MainMenuECS : ECSManager() {
    private var starSpawningSystem: StarSpawningSystem
    private var orbitalMotion: OrbitalMotion
    private var shapeRenderingSystem: ShapeRenderingSystem

    init {
        registerComponent(PositionComponent::class)
        registerComponent(VelocityComponent::class)
        registerComponent(OrbitComponent::class)
        registerComponent(ColorComponent::class)
        registerComponent(ShapeComponent::class)
        registerComponent(StarSpawnComponent::class)
        registerComponent(ParentComponent::class)
        registerComponent(TextComponent::class)

        // Background
        createEntity().apply {
            addComponent<PositionComponent>(this, PositionComponent.componentID) // Default is (0,0)
            addComponent<ShapeComponent>(this, ShapeComponent.componentID).let {
                it.shapeType = ShapeComponent.ShapeType.RECTANGLE
                it.x = 1f
                it.y = 1f
            }
            addComponent<ColorComponent>(this, ColorComponent.componentID).let {
                it.color = Color.argb(255, 128, 128, 255)
            }
        }

        // Galaxy centre
        createEntity().apply {
            addComponent<PositionComponent>(this, PositionComponent.componentID)
            addComponent<OrbitComponent>(this, OrbitComponent.componentID).let {
                it.t0 = 0f
                it.r0 = 0.05f
                it.vr = 0f
                it.va = 0.001f
            }
            addComponent<StarSpawnComponent>(this, StarSpawnComponent.componentID).let {
                it.spawnProbabilityPerSec = 23f // about 90% chance of spawning within 0,1 s
                it.nextSpawn = 0f
            }
        }

        orbitalMotion = OrbitalMotion(this).also { addSystem(it) }
        starSpawningSystem = StarSpawningSystem(this).also { addSystem(it) }
        shapeRenderingSystem = ShapeRenderingSystem(this).also { addSystem(it) }
    }

    override fun update(t: Float, dt: Float) {
        starSpawningSystem.activate(t)
        orbitalMotion.activate(t)
    }

    override fun draw(canvas: Canvas) {
        shapeRenderingSystem.activate(canvas)
    }
}