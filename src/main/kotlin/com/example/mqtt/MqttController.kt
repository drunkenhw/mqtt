package com.example.mqtt

import org.slf4j.LoggerFactory
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
        log.info("mqtt")
        mqttSender.send()
        return "mqtt"
    }

    @GetMapping("/mqtt2")
    fun mqtt2(@RequestBody content: Map<String, String>): String {
        log.info("mqtt2")
        mqttSender.sendAwsIotMessage(content)
        return "mqtt"
    }

    @GetMapping("/mqtt3")
    fun mqtt3(): String? {
        log.info("mqtt3")
        return mqttAuthenticator.authenticate()?.keyPair()?.privateKey()
    }

    companion object {
        private val log = LoggerFactory.getLogger(MqttController::class.java)
    }
}
