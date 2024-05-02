package com.example.mqtt

import com.amazonaws.services.iot.client.AWSIotMqttClient
import com.amazonaws.services.iot.client.auth.Credentials
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.iot.IotClient
import software.amazon.awssdk.services.iot.endpoints.internal.DefaultIotEndpointProvider


@Configuration
class MqttConfig {

    @Value("\${mqtt.client-id}")
    private lateinit var clientId: String

    @Value("\${mqtt.end-point}")
    private lateinit var endPoint: String

    @Bean
    fun awsIotMqttClient(): AWSIotMqttClient {
        val region = "ap-northeast-2"
        val resolveCredentials = DefaultCredentialsProvider.create().resolveCredentials()

        val awsIotMqttClient = AWSIotMqttClient(
            endPoint,
            clientId,
            { Credentials(resolveCredentials.accessKeyId(), resolveCredentials.secretAccessKey()) },
            region,
        )
        awsIotMqttClient.connect()
        return awsIotMqttClient
    }

    @Bean
    fun iotClient(): IotClient {
        val iotClient = IotClient.builder()
            .region(Region.AP_NORTHEAST_2)
            .endpointProvider(DefaultIotEndpointProvider())
            .build()
        return iotClient
    }
}
