package com.example.mqtt

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.services.iot.client.AWSIotMqttClient
import com.amazonaws.services.iot.client.auth.Credentials
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.ContainerCredentialsProvider
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
        val awsCredentials = DefaultAWSCredentialsProviderChain.getInstance().credentials

        log.info("awsCredentials: ${awsCredentials.awsAccessKeyId}, ${awsCredentials.awsSecretKey}")
        val awsIotMqttClient = AWSIotMqttClient(
            endPoint,
            clientId,
            { Credentials(awsCredentials.awsAccessKeyId, awsCredentials.awsSecretKey) },
            region,
        )
        awsIotMqttClient.connect()
        return awsIotMqttClient
    }

    @Bean
    fun iotClient(): IotClient {
        val iotClient = IotClient.builder()
            .region(Region.AP_NORTHEAST_2)
            .credentialsProvider(ContainerCredentialsProvider.builder().build())
            .endpointProvider(DefaultIotEndpointProvider())
            .build()
        return iotClient
    }

    companion object {
        private val log = LoggerFactory.getLogger(MqttConfig::class.java)
    }
}
