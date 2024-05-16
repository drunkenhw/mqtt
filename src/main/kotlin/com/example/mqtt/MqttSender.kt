package com.example.mqtt

import com.amazonaws.services.iot.client.AWSIotMessage
import com.amazonaws.services.iot.client.AWSIotMqttClient
import com.amazonaws.services.iot.client.AWSIotQos
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service

@Service
class MqttSender(
    private val awsIotMqttClient: AWSIotMqttClient,
    private val objectMapper: ObjectMapper
) {

    fun send() {
        awsIotMqttClient.publish("/topic", "message", 3000)
    }

    fun sendAwsIotMessage(content: Map<String, String>) {
        val json = objectMapper.writeValueAsString(content)
        val message = Message("/hello", json)
        awsIotMqttClient.publish(message, 3000)
    }
}

class Message(topic: String, payload: String) : AWSIotMessage(topic, AWSIotQos.QOS0, payload) {
    override fun onFailure() {
        println("fail")
    }

    override fun onSuccess() {
        println("success")
    }
}
