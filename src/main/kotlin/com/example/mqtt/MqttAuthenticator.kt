package com.example.mqtt

import org.springframework.stereotype.Service
import software.amazon.awssdk.services.iot.IotClient
import software.amazon.awssdk.services.iot.model.AddThingToThingGroupRequest
import software.amazon.awssdk.services.iot.model.AttachPolicyRequest
import software.amazon.awssdk.services.iot.model.AttachThingPrincipalRequest
import software.amazon.awssdk.services.iot.model.CreateKeysAndCertificateRequest
import software.amazon.awssdk.services.iot.model.CreateKeysAndCertificateResponse
import software.amazon.awssdk.services.iot.model.CreateThingRequest
import software.amazon.awssdk.services.iot.model.DeleteCertificateRequest
import software.amazon.awssdk.services.iot.model.ListAuthorizersRequest

@Service
class MqttAuthenticator(
    private val iotClient: IotClient
) {

    fun authenticate(): CreateKeysAndCertificateResponse =
        iotClient.createKeysAndCertificate { it.setAsActive(true) }

    fun jitp(deviceId: String): Map<String, String> {
        val keyAndCert = iotClient.createKeysAndCertificate {
            it.setAsActive(true)
        }

        val certificateArn = keyAndCert.certificateArn()
        val certificatePem = keyAndCert.certificatePem()
        val privateKey = keyAndCert.keyPair().privateKey()

        // 사물 생성
        iotClient.createThing { it.thingName(deviceId) }

        // 사물 그룹 연결
        iotClient.addThingToThingGroup {
            it.thingName(deviceId).thingGroupName("boxster-group")
        }

        // 정책 연결
        val policyName = "boxster-jitp"
        iotClient.attachPolicy {
            it.policyName(policyName).target(certificateArn)
        }

        // 사물에 인증서 연결
        iotClient.attachThingPrincipal {
            it.thingName(deviceId).principal(certificateArn)
        }


        return mapOf("certPem" to certificatePem, "privateKey" to privateKey)
    }
}
