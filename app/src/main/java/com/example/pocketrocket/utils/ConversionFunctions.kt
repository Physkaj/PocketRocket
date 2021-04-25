package com.example.pocketrocket.utils

fun FPSToMillisPerFrame(FPS: Long): Float = 1000f / FPS
fun millisPerFrameToFPS(ns: Long): Float = 1000f / ns
fun millisToSec(ns: Long): Float = ns * 0.001f
fun secToMillis(sec: Long): Long = sec * 1000L
fun secToMillis(sec: Float): Float = sec * 1000f

fun FPSToNsPerFrame(FPS: Long): Long = 1000000000L / FPS
fun nsPerFrameToFPS(ns: Long): Float = 1000000000f / ns
fun nsToSec(ns: Long): Float = ns * 0.000000001f
fun secToNs(sec: Long): Long = sec * 1000000000L
fun secToNs(sec: Float): Long = (sec * 1000000000L).toLong()
