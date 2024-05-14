package com.example.mqtt

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.crt.mqtt5.Mqtt5Client
import software.amazon.awssdk.iot.AwsIotMqtt5ClientBuilder
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.iot.IotClient
import software.amazon.awssdk.services.iot.endpoints.internal.DefaultIotEndpointProvider
import software.amazon.awssdk.services.iot.model.CreateKeysAndCertificateRequest


@Configuration
class MqttConfig {

    @Value("\${mqtt.client-id}")
    private lateinit var clientId: String

    @Value("\${mqtt.end-point}")
    private lateinit var endPoint: String

    @Bean
    fun mqtt5Client(iotClient: IotClient): Mqtt5Client {
        val key = iotClient.createKeysAndCertificate(CreateKeysAndCertificateRequest.builder().setAsActive(true).build())
        val client = AwsIotMqtt5ClientBuilder.newDirectMqttBuilderWithMtlsFromMemory(
            endPoint, key.certificatePem(), key.keyPair().privateKey()
        ).build()
        log.info("MQTT client created")
        log.info("client : $client")
        client.start()
        return client
    }

    @Bean
    fun iotClient(): IotClient {
        val iotClient = IotClient.builder()
            .region(Region.AP_NORTHEAST_2)
            .endpointProvider(DefaultIotEndpointProvider())
            .build()
        log.info("Iot client created")
        return iotClient
    }

    companion object {
        private val log = LoggerFactory.getLogger(MqttConfig::class.java)
    }
}
