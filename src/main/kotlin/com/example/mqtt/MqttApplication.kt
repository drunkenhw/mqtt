package com.example.mqtt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MqttApplication

fun main(args: Array<String>) {
    runApplication<MqttApplication>(*args)
}
