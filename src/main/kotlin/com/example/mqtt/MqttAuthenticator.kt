package com.example.mqtt

import org.springframework.stereotype.Service
import software.amazon.awssdk.services.iot.IotClient
import software.amazon.awssdk.services.iot.model.CreateKeysAndCertificateResponse

@Service
class MqttAuthenticator(
    private val iotClient: IotClient
) {

    fun authenticate(): CreateKeysAndCertificateResponse? {
        val createKeysAndCertificate = iotClient.createKeysAndCertificate()
        return createKeysAndCertificate
    }
}
