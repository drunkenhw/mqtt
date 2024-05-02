package com.example.mqtt

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MqttController(
    private val mqttSender: MqttSender,
    private val mqttAuthenticator: MqttAuthenticator
) {

    @GetMapping("/mqtt")
    fun mqtt(): String {
        mqttSender.send()
        return "mqtt"
    }

    @GetMapping("/mqtt2")
    fun mqtt2(@RequestBody content: Map<String, String>): String {
        mqttSender.sendAwsIotMessage(content)
        return "mqtt"
    }

    @GetMapping("/mqtt3")
    fun mqtt3(): String? {
        return mqttAuthenticator.authenticate()?.keyPair()?.privateKey()
    }

}
