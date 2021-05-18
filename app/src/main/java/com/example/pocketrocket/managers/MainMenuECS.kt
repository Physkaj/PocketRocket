package com.example.pocketrocket.managers

import android.graphics.Canvas
import android.graphics.Color
import com.example.pocketrocket.components.*
import com.example.pocketrocket.systems.*
import com.example.pocketrocket.utils.SpiralGalaxy
import kotlin.math.*

class MainMenuECS(callbackGameManger: GameManager) : ECSManager(callbackGameManger) {
    private lateinit var backgroundRenderingSystem: BackgroundRenderingSystem
    private lateinit var shapeRenderingSystem: ShapeRenderingSystem
    private lateinit var gravitySystem: GravitySystem
    private lateinit var orbitalMotionSystem: OrbitalMotionSystem
    private lateinit var eulerMotionSystem: EulerMotionSystem
    private lateinit var resetAccelerationSystem: ResetAccelerationSystem

    init {
        setupComponents()
        setupEntities()
        setupSystems()
    }

    override fun update(t: Float, dt: Float) {
        resetAccelerationSystem.reset()
        gravitySystem.gravitate()
        orbitalMotionSystem.updateOrbits(dt)
        eulerMotionSystem.activate(dt)
    }

    override fun draw(canvas: Canvas) {
        backgroundRenderingSystem.drawBackground(canvas)
        shapeRenderingSystem.activate(canvas)
    }

    private fun setupComponents() {
        registerComponent(BackgroundComponent::class)
        registerComponent(ColorComponent::class)
        registerComponent(GravityComponent::class)
        registerComponent(OrbitComponent::class)
        registerComponent(PhysicalBodyComponent::class)
        registerComponent(ParentComponent::class)
        registerComponent(PositionComponent::class)
        registerComponent(ShapeComponent::class)

        growComponentPoolSize(ColorComponent.componentID, 1000)
        growComponentPoolSize(OrbitComponent.componentID, 1000)
        growComponentPoolSize(ParentComponent.componentID, 1000)
        growComponentPoolSize(PositionComponent.componentID, 1000)
        growComponentPoolSize(ShapeComponent.componentID, 1000)
    }

    private fun setupEntities() {
        // Background
        createEntity().apply {
            addComponent<BackgroundComponent>(this, BackgroundComponent.componentID)
            addComponent<ColorComponent>(this, ColorComponent.componentID).let {
                it.color = Color.BLACK
            }
        }

        // Galaxy centre
        val centreMass: Float = 0.1f
        val centreEntity = createEntity().apply {
            addComponent<PositionComponent>(this, PositionComponent.componentID).pos.clear()
            addComponent<GravityComponent>(this, GravityComponent.componentID).mass = centreMass
            addComponent<ColorComponent>(this, ColorComponent.componentID).color = Color.GREEN
            addComponent<ShapeComponent>(this, ShapeComponent.componentID).let {
                it.shapeType = ShapeComponent.ShapeType.CIRCLE
                it.r = 0.01f
            }
        }

        // Generate stars
        val nStars = 5000
        for (i in 0 until nStars) {
            createEntity().apply {
                val positionComponent = addComponent<PositionComponent>(this, PositionComponent.componentID)
                val orbitComponent = addComponent<OrbitComponent>(this, OrbitComponent.componentID)
                SpiralGalaxy.setupStar(
                    positionComponent,
                    orbitComponent
                )
                // Color it
                val factor = (orbitComponent.apoapsis - SpiralGalaxy.maxApoapsis) / (SpiralGalaxy.maxApoapsis - SpiralGalaxy.minApoapsis)
                val cR = factor
                val cG = 0
                val cB = 1.0 - factor
                addComponent<ColorComponent>(this, ColorComponent.componentID).color =
                    (0xff shl 24) or ((cR * 0xff).toInt() shl 16) or ((cG * 0xff).toInt() shl 8) or (cB * 0xff).toInt()

                addComponent<ParentComponent>(this, ParentComponent.componentID).parentEid = centreEntity
                addComponent<ShapeComponent>(this, ShapeComponent.componentID).let {
                    it.shapeType = ShapeComponent.ShapeType.CIRCLE
                    it.r = 0.005f
                }
            }
        }
    }

    private fun setupSystems() {
        resetAccelerationSystem = ResetAccelerationSystem(this).also {
            addSystem(it)
        }
        gravitySystem = GravitySystem(this).also {
            it.significantMassLimit = 0.001f
            addSystem(it)
        }
        eulerMotionSystem = EulerMotionSystem(this).also {
            addSystem(it)
        }
        orbitalMotionSystem = OrbitalMotionSystem(this).also {
            addSystem(it)
        }
        backgroundRenderingSystem = BackgroundRenderingSystem(this).also {
            addSystem(it)
        }
        shapeRenderingSystem = ShapeRenderingSystem(this).also {
            addSystem(it)
        }
    }
}
