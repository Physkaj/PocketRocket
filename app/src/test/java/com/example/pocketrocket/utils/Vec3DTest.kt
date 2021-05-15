package com.example.pocketrocket.utils

import junit.framework.TestCase
import org.junit.Assert
import kotlin.math.*

class Vec3DTest : TestCase() {
    var v0 = Vec3D(0f, 0f, 0f)
    var v1 = Vec3D(0f, 3f, 4f)
    var v2 = Vec3D(5f, 6f, 8f)

    fun testGetR() {
        assertEquals(v0.r, 0f)
        assertEquals(v1.r, 5f)
        assertEquals(v2.r, 11.1803398875f)
    }

    fun testGetR2() {
        assertEquals(v0.r2, 0f)
        assertEquals(v1.r2, 25f)
        assertEquals(v2.r2, 125f)
    }

    fun testGetRho() {
        assertEquals(v0.rho, 0f)
        assertEquals(v1.rho, 3f)
        assertEquals(v2.rho, 7.81024967591f)
    }

    fun testGetRho2() {
        assertEquals(v0.rho2, 0f)
        assertEquals(v1.rho2, 9f)
        assertEquals(v2.rho2, 61f)
    }

    fun testGetAzimuth() {
        assertEquals(v0.azimuth, 0f)
        assertEquals(v1.azimuth, onePI / 2f)
        assertEquals(v2.azimuth, 0.876058050598f)
    }

    fun testGetInclination() {
        assertEquals(v0.inclination, 0f)
        assertEquals(v1.inclination, acos(4.0 / 5.0).toFloat())
        Assert.assertEquals(v2.inclination, 0.773397011099f, 1e-5f)
    }

    fun testPlus() {
        assertEquals(v0 + v1, v1)
        assertEquals(v2 + v1, Vec3D(5f, 9f, 12f))
    }

    fun testMinus() {
        assertEquals(v2 - v1, Vec3D(5f, 3f, 4f))
    }

    fun testTimes() {
        assertEquals(v1 * 5f, Vec3D(0f, 15f, 20f))
    }

    fun testDot() {
        assertEquals(v0 * v1, 0f)
        assertEquals(v2 * v1, 50f)
        assertEquals(Vec3D(1f, 1f, 0f) * Vec3D(0f, 0f, 100f), 0f)
    }

    fun testCross() {
        assertEquals(Vec3D(1f, 0f, 0f).cross(Vec3D(0f, 1f, 0f)), Vec3D(0f, 0f, 1f))
        assertEquals(Vec3D(0f, 1f, 0f).cross(Vec3D(0f, 0f, 1f)), Vec3D(1f, 0f, 0f))
        assertEquals(Vec3D(0f, 1f, 0f).cross(Vec3D(1f, 0f, 0f)), Vec3D(0f, 0f, -1f))
    }
}