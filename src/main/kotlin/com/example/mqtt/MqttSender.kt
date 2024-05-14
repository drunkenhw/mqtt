package com.example.mqtt

import com.amazonaws.services.iot.client.AWSIotMessage
import com.amazonaws.services.iot.client.AWSIotMqttClient
import com.amazonaws.services.iot.client.AWSIotQos
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import software.amazon.awssdk.crt.mqtt5.Mqtt5Client
import software.amazon.awssdk.crt.mqtt5.packets.PublishPacket
import software.amazon.awssdk.crt.mqtt5.packets.PublishPacket.PublishPacketBuilder

@Service
class MqttSender(
    private val mqtt5Client: Mqtt5Client,
    private val objectMapper: ObjectMapper
) {

    fun send() {
        val message = publishPacket("/topic","rch")
        mqtt5Client.publish(message)
    }

    private fun publishPacket(topic: String, message: String): PublishPacket {
        return PublishPacketBuilder().withTopic(topic)
            .withPayload(message.toByteArray())
            .build()
    }

    fun sendAwsIotMessage(content: Map<String, String>) {
        val json = objectMapper.writeValueAsString(content)
        mqtt5Client.publish(publishPacket("hello", json))
    }
}
