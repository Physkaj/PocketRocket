package com.example.pocketrocket.managers

import android.graphics.Canvas
import android.graphics.Color
import com.example.pocketrocket.components.*
import com.example.pocketrocket.systems.*
import com.example.pocketrocket.utils.SpiralGalaxy
import kotlin.math.*


class MainMenuECS(callbackGameManger: GameManager) : ECSManager(callbackGameManger) {
    private lateinit var backgroundRenderingSystem: BackgroundRenderingSystem
    private lateinit var debugSystem: DebugSystem
    private lateinit var shapeRenderingSystem: ShapeRenderingSystem
    private lateinit var textRenderingSystem: TextRenderingSystem
    private lateinit var gravitySystem: GravitySystem
    private lateinit var orbitalMotionSystem: OrbitalMotionSystem
    private lateinit var eulerMotionSystem: EulerMotionSystem
    private lateinit var resetAccelerationSystem: ResetAccelerationSystem

    init {
        setupComponents()
        setupSystems()
        setupEntities()
    }

    override fun update(t: Float, dt: Float) {
        resetAccelerationSystem.reset()
        gravitySystem.gravitate()
        orbitalMotionSystem.updateOrbits(dt)
        eulerMotionSystem.activate(dt)
        debugSystem.doDebugStuff()
    }

    override fun draw(canvas: Canvas) {
        backgroundRenderingSystem.drawBackground(canvas)
        shapeRenderingSystem.drawShapes(canvas)
        textRenderingSystem.drawTexts(canvas)
    }

    override fun onResize(width: Int, height: Int) {
        backgroundRenderingSystem.resizeBitmaps(width, height)
    }

    private fun setupComponents() {
        registerComponent(BackgroundComponent::class)
        registerComponent(BitmapComponent::class)
        registerComponent(ColorComponent::class)
        registerComponent(DebugComponent::class)
        registerComponent(GradientComponent::class)
        registerComponent(GravityComponent::class)
        registerComponent(OrbitComponent::class)
        registerComponent(PhysicalBodyComponent::class)
        registerComponent(ParentComponent::class)
        registerComponent(PositionComponent::class)
        registerComponent(ShapeComponent::class)
        registerComponent(TextComponent::class)

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
            addComponent<BitmapComponent>(this, BitmapComponent.componentID).bitmap = null
            addComponent<GradientComponent>(this, GradientComponent.componentID).let {
                it.bitmapWidth = getScreenProperties().width
                it.bitmapHeight = getScreenProperties().height
                it.gradientFrom.setValues(0f, 0f)
                it.gradientTo.setValues(0.0f, 1.5f)
                it.colors = listOf(
                    Color.BLACK,
                    Color.parseColor("#FFFFFF"),
                    Color.parseColor("#FFA540"),
                    Color.parseColor("#0000A0"),
                    Color.BLACK
                )
                it.colorPositions = listOf(0.1f, 0.1f, 0.2f, 0.7f, 0.9f)
                it.gradientType = GradientType.RADIAL
            }
        }

        // Galaxy centre
        val centreMass: Float = 0.1f
        val centreEntity = createEntity().apply {
            addComponent<PositionComponent>(this, PositionComponent.componentID).pos.clear()
            addComponent<GravityComponent>(this, GravityComponent.componentID).mass = centreMass
        }

        // Generate stars
        val nStars = 1000
        for (i in 0 until nStars) {
            createEntity().apply {
                val positionComponent = addComponent<PositionComponent>(this, PositionComponent.componentID)
                val orbitComponent = addComponent<OrbitComponent>(this, OrbitComponent.componentID)
                SpiralGalaxy.nArms = 2
                SpiralGalaxy.maxApoapsis = 1.0f
                SpiralGalaxy.minApoapsis = 0.3f
                SpiralGalaxy.minPeriapsis = 0.15f
                SpiralGalaxy.maxPeriapsis = 0.6f
                SpiralGalaxy.maxArg = 2 * PI.toFloat()
                SpiralGalaxy.minArg = 0f
                SpiralGalaxy.setupStar(
                    positionComponent,
                    orbitComponent
                )
                // Color it
                val rFactor = 1f - (orbitComponent.apoapsis - SpiralGalaxy.minApoapsis) / (SpiralGalaxy.maxApoapsis - SpiralGalaxy.minApoapsis)
                val gFactor = 0f
                val bFactor =
                    1f - (orbitComponent.e - 0.5 * (SpiralGalaxy.minEccentricity + SpiralGalaxy.maxEccentricity)).absoluteValue / (SpiralGalaxy.maxEccentricity - SpiralGalaxy.minEccentricity)
                addComponent<ColorComponent>(this, ColorComponent.componentID).color =
                    (0xff shl 24) or ((rFactor * 0xff).toInt() shl 16) or ((gFactor * 0xff).toInt() shl 8) or (bFactor * 0xff).toInt()

                addComponent<ParentComponent>(this, ParentComponent.componentID).parentEid = centreEntity
                addComponent<ShapeComponent>(this, ShapeComponent.componentID).let {
                    it.shapeType = ShapeComponent.ShapeType.CIRCLE
                    it.r = 0.006f
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
        textRenderingSystem = TextRenderingSystem(this).also {
            addSystem(it)
        }
        debugSystem = DebugSystem(this).also {
            addSystem(it)
        }
    }
}
