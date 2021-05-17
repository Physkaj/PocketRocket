package com.example.pocketrocket.managers

import android.graphics.Canvas
import android.graphics.Color
import com.example.pocketrocket.components.*
import com.example.pocketrocket.systems.*
import kotlin.math.*
import kotlin.random.Random

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
        growComponentPoolSize(GravityComponent.componentID, 1000)
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
                it.color = Color.DKGRAY
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
        val nStars = 1000
        for (i in 0 until nStars) {
            createEntity().apply {
                setupStar(
                    addComponent<PositionComponent>(this, PositionComponent.componentID),
                    addComponent<OrbitComponent>(this, OrbitComponent.componentID),
                    addComponent<ColorComponent>(this, ColorComponent.componentID)
                )
                addComponent<ParentComponent>(this, ParentComponent.componentID).parentEid = centreEntity
                addComponent<ShapeComponent>(this, ShapeComponent.componentID).let {
                    it.shapeType = ShapeComponent.ShapeType.CIRCLE
                    it.r = 0.01f
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

    private fun setupStar(posComp: PositionComponent, orbComp: OrbitComponent, colorComp: ColorComponent) {
        val shortestApoapsis = 0.2
        val longestApoapsis = 1f
        val minEcc = 0.5
        val maxEcc = 0.51
        val minAngle = 0.0
        val maxAngle = 4 * PI
        val k1 = shortestApoapsis
        val k2 = ln(longestApoapsis / shortestApoapsis) / maxAngle

        // Apoapsis r = a+c = a + ecc*a = a*(1+ecc)
        val argApoapsis = Random.nextDouble(minAngle, maxAngle)
        val r = k1 * exp(argApoapsis * k2) // Logarithmic spiral equation
        // Eccentricity
        val ecc = Random.nextDouble(minEcc, maxEcc)
        // Semi-major axis
        val a = r / (1 + ecc)
        // Semi-minor axis
        val b = sqrt(a * a * (1 - ecc * ecc))
        // Linear eccentricity
        val c = a * ecc

        // Periapsis in y-direction and apoapsis in x-direction
        val arg = Random.nextDouble(0.0, 2 * PI)
        val randX = a * cos(arg) + c
        val randY = b * sin(arg)
        // Rotate according to the spiral position
        posComp.pos.x = (cos(argApoapsis) * randX + sin(argApoapsis) * randY).toFloat()
        posComp.pos.y = (-sin(argApoapsis) * randX + cos(argApoapsis) * randY).toFloat()
        // Orbital parameters
        orbComp.a = a.toFloat()
        orbComp.b = b.toFloat()
        orbComp.inclination = 0f
        orbComp.argApoapsis = argApoapsis.toFloat()
        orbComp.arg = arg.toFloat()

        // Color it
        val factor = (argApoapsis.absoluteValue - minAngle) / (maxAngle - minAngle)
        val cR = 1.0
        val cG = 1.0 - factor
        val cB = 1.0 - factor
        colorComp.color = (0xff shl 24) or ((cR * 0xff).toInt() shl 16) or ((cG * 0xff).toInt() shl 8) or (cB * 0xff).toInt()
    }
}
