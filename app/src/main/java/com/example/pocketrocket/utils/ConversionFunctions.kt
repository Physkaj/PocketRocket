package com.example.pocketrocket.utils

fun FPSToMillisPerFrame(FPS: Long): Float = 1000f / FPS
fun MillisPerFrameToFPS(ns: Long): Float = 1000f / ns
fun MillisToSec(ns: Long): Float = ns * 0.001f
fun SecToMillis(sec: Long): Long = sec * 1000L
fun SecToMillis(sec: Float): Float = sec * 1000f

fun FPSToNsPerFrame(FPS: Long): Long = 1000000000L / FPS
fun NsPerFrameToFPS(ns: Long): Float = 1000000000f / ns
fun NsToSec(ns: Long): Float = ns * 0.000000001f
fun SecToNs(sec: Long): Long = sec * 1000000000L
fun SecToNs(sec: Float): Long = (sec * 1000000000L).toLong()
