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
        registerComponent(PhysicalBodyComponent::class)
        registerComponent(PositionComponent::class)
        registerComponent(ShapeComponent::class)

        growComponentPoolSize(ColorComponent.componentID, 1000)
        growComponentPoolSize(GravityComponent.componentID, 1000)
        growComponentPoolSize(PhysicalBodyComponent.componentID, 1000)
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
        val centreMass: Float = 1f
        createEntity().apply {
            addComponent<PositionComponent>(this, PositionComponent.componentID).let {
                it.pos.x = 0f
                it.pos.y = 0f
            }
            addComponent<GravityComponent>(this, GravityComponent.componentID).let {
                it.mass = centreMass
            }
            addComponent<ColorComponent>(this, ColorComponent.componentID).let {
                it.color = Color.GREEN
            }
            addComponent<ShapeComponent>(this, ShapeComponent.componentID).let {
                it.shapeType = ShapeComponent.ShapeType.CIRCLE
                it.r = 0.01f
            }
        }

        // Generate stars
        val starMass = 0.00001f
        val nStars = 10
        for (i in 0 until nStars) {
            createEntity().apply {
                setupStar(
                    addComponent<PositionComponent>(this, PositionComponent.componentID),
                    addComponent<PhysicalBodyComponent>(this, PhysicalBodyComponent.componentID),
                    centreMass + starMass,
                    addComponent<ColorComponent>(this, ColorComponent.componentID)
                )
                addComponent<GravityComponent>(this, GravityComponent.componentID).let {
                    it.mass = starMass
                }
                addComponent<ShapeComponent>(this, ShapeComponent.componentID).let {
                    it.shapeType = ShapeComponent.ShapeType.CIRCLE
                    it.r = 0.01f
                }
            }
        }
    }

    private fun setupSystems() {
        gravitySystem = GravitySystem(this).also {
            addSystem(it)
        }
        eulerMotionSystem = EulerMotionSystem(this).also {
            addSystem(it)
        }
        resetAccelerationSystem = ResetAccelerationSystem(this).also {
            addSystem(it)
        }
        backgroundRenderingSystem = BackgroundRenderingSystem(this).also {
            addSystem(it)
        }
        shapeRenderingSystem = ShapeRenderingSystem(this).also {
            addSystem(it)
        }
    }

    private fun setupStar(posComp: PositionComponent, physComp: PhysicalBodyComponent, combinedMass: Float, colorComp: ColorComponent) {

        val shortestApoapsis = 0.1
        val longestApoapsis = 1.0
        val shortestPeriapsis = 0.1
        val longestPeriapsis = 1.0
        val minEcc = 0.25
        val maxEcc = 0.45
        val minAngle = 0.0
        val maxAngle = 2 * PI
        val k1 = shortestApoapsis
        val k2 = ln(longestApoapsis / shortestApoapsis) / maxAngle

        // Apoapsis r = a+c = a + ecc*a = a*(1+ecc)
        val angle = Random.nextDouble(minAngle, maxAngle)
        val r = k1 * exp(angle * k2) // Logarithmic spiral equation
        // Eccentricity
        val ecc = Random.nextDouble(minEcc, maxEcc)
        // Semi-major axis
        val a = r / (1 + ecc)
        // Semi-minor axis
        val b = sqrt(a * a * (1 - ecc * ecc))
        // Linear eccentricity
        val c = a * ecc

        // Periapsis in y-direction and apoapsis in x-direction
        val t = Random.nextDouble(0.0, 2 * PI)
        val randX = a * cos(t) + c
        val randY = b * sin(t)
        // Rotate according to the spiral position
        posComp.pos.x = (cos(angle) * randX + sin(angle) * randY).toFloat()
        posComp.pos.y = (sin(angle) * randX + cos(angle) * randY).toFloat()
        // Orbital speed
        val v = sqrt(combinedMass * (2 / posComp.pos.r - 1 / a))
        physComp.vel.x = (v * posComp.pos.y / r).toFloat()
        physComp.vel.y = (-v * posComp.pos.x / r).toFloat()
        physComp.vel *= 2 * Random.nextInt(0, 1) - 1

        // Color it
        val factor = (angle - minAngle) / (maxAngle - minAngle)
        val cR = 1.0
        val cG = 1.0 - factor
        val cB = 1.0 - factor
        colorComp.color = (0xff shl 24) or ((cR * 0xff).toInt() shl 16) or ((cG * 0xff).toInt() shl 8) or (cB * 0xff).toInt()

        // Let gravity system handle the acceleration
        physComp.acc.x = 0f
        physComp.acc.y = 0f
    }
}
