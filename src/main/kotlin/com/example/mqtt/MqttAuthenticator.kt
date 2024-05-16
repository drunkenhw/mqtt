package com.example.mqtt

import org.springframework.stereotype.Service
import software.amazon.awssdk.services.iot.IotClient
import software.amazon.awssdk.services.iot.model.AddThingToThingGroupRequest
import software.amazon.awssdk.services.iot.model.AttachPolicyRequest
import software.amazon.awssdk.services.iot.model.AttachThingPrincipalRequest
import software.amazon.awssdk.services.iot.model.CreateKeysAndCertificateRequest
import software.amazon.awssdk.services.iot.model.CreateKeysAndCertificateResponse
import software.amazon.awssdk.services.iot.model.CreateThingRequest

@Service
class MqttAuthenticator(
    private val iotClient: IotClient
) {

    fun authenticate(): CreateKeysAndCertificateResponse =
        iotClient.createKeysAndCertificate { it.setAsActive(true) }

    fun jitp(deviceId: String): Map<String, String> {
        val keyAndCert = iotClient.createKeysAndCertificate(
            CreateKeysAndCertificateRequest.builder().setAsActive(true).build()
        )

        val certificateArn = keyAndCert.certificateArn()
        val certificatePem = keyAndCert.certificatePem()
        val privateKey = keyAndCert.keyPair().privateKey()

        // 사물 생성
        iotClient.createThing(CreateThingRequest.builder().thingName(deviceId).build())

        // 정책 연결
        val policyName = "boxster-jitp"
        iotClient.attachPolicy(
            AttachPolicyRequest.builder().policyName(policyName).target(certificateArn).build()
        )

        // 사물에 인증서 연결
        iotClient.attachThingPrincipal(
            AttachThingPrincipalRequest.builder().thingName(deviceId).principal(certificateArn).build()
        )
        iotClient.addThingToThingGroup(
            AddThingToThingGroupRequest.builder().thingGroupName("boxster-group").thingName(deviceId).build()
        )

        return mapOf("certPem" to certificatePem, "privateKey" to privateKey)
    }
}
