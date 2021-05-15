package com.example.pocketrocket.managers

import android.graphics.Canvas
import android.graphics.Color
import com.example.pocketrocket.components.*
import com.example.pocketrocket.systems.BackgroundRenderingSystem
import com.example.pocketrocket.systems.OrbitalSystem
import com.example.pocketrocket.systems.ShapeRenderingSystem
import com.example.pocketrocket.systems.StarSpawningSystem

class MainMenuECS(callbackGameManger: GameManager) : ECSManager(callbackGameManger) {
    private var backgroundRenderingSystem: BackgroundRenderingSystem
    private var starSpawningSystem: StarSpawningSystem
    private var orbitalSystem: OrbitalSystem
    private var shapeRenderingSystem: ShapeRenderingSystem

    init {
        registerComponent(BackgroundComponent::class)
        registerComponent(ColorComponent::class)
        registerComponent(OrbitComponent::class)
        registerComponent(ParentComponent::class)
        registerComponent(PositionComponent::class)
        registerComponent(ShapeComponent::class)
        registerComponent(StarSpawnComponent::class)
        registerComponent(TextComponent::class)
        registerComponent(VelocityComponent::class)

        // Background
        createEntity().apply {
            addComponent<BackgroundComponent>(this, BackgroundComponent.componentID)
            addComponent<ColorComponent>(this, ColorComponent.componentID).let {
                it.color = Color.DKGRAY
            }
        }

        // Galaxy centre
        val starSpawnerComponent: StarSpawnComponent
        createEntity().apply {
            addComponent<PositionComponent>(this, PositionComponent.componentID)
            addComponent<OrbitComponent>(this, OrbitComponent.componentID).let {
                it.t0 = 0f
                it.r0 = 0.3f
                it.vr = 0f
                it.va = 0.05f
            }
            addComponent<StarSpawnComponent>(this, StarSpawnComponent.componentID).let {
                it.nextSpawnMinSec = 0.1f
                it.nextSpawnMaxSec = 1f
                it.nextSpawn = 0f
                it.starRadialVelocity = 0.01f
                it.starAngularVelocity = 0.5f
                it.starRadius = 0.01f
                starSpawnerComponent = it
            }
        }

        backgroundRenderingSystem = BackgroundRenderingSystem(this).also {
            addSystem(it)
        }
        orbitalSystem = OrbitalSystem(this).also {
            addSystem(it)
        }
        starSpawningSystem = StarSpawningSystem(this).also {
            addSystem(it)
            it.retroactiveStarCreation(2f / starSpawnerComponent.starRadialVelocity)
        }
        shapeRenderingSystem = ShapeRenderingSystem(this).also {
            addSystem(it)
        }
    }

    override fun update(t: Float, dt: Float) {
        starSpawningSystem.spawn(t)
        orbitalSystem.updateOrbits(t)
        orbitalSystem.deOrbit(13f)
    }

    override fun draw(canvas: Canvas) {
        backgroundRenderingSystem.drawBackground(canvas)
        shapeRenderingSystem.activate(canvas)
    }
}