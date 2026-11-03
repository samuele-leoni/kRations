package com.example.krations

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform