package com.lms.modern.starter.api.security

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.io.IOException


@Configuration
class FirebaseConfig(
        @Value("\${firebase.type:}")
        open var serviceType: String,

        @Value("\${firebase.project_id:}")
        open var projectId: String,

        @Value("\${firebase.private_key_id:}")
        open var privateKeyId: String,

        @Value("\${firebase.private_key:}")
        open var privateKey: String,

        @Value("\${firebase.client_email:}")
        open var clientEmail: String,

        @Value("\${firebase.client_id:}")
        open var clientId: String,

        @Value("\${firebase.auth_uri:}")
        open var authUri: String,

        @Value("\${firebase.token_uri:}")
        open var tokenUri: String,

        @Value("\${firebase.auth_provider_x509_cert_url:}")
        open var authProviderCertUrl: String,

        @Value("\${firebase.client_x509_cert_url:}")
        open var clientCertUrl: String
) {

    @Primary
    @Bean
    @Throws(IOException::class)
    fun firebaseInit() {
        val options: FirebaseOptions = FirebaseOptions.builder()
                .setCredentials(getFirebaseCredentials()).build()
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options)
        }
    }

     fun getFirebaseCredentials(): GoogleCredentials {
        val jsonObject = JSONObject();
        jsonObject.put("type", serviceType)
        jsonObject.put("project_id", projectId)
        jsonObject.put("private_key_id", privateKeyId)
        jsonObject.put("private_key", privateKey)
        jsonObject.put("client_email", clientEmail)
        jsonObject.put("client_id", clientId)
        jsonObject.put("auth_uri", authUri)
        jsonObject.put("token_uri", tokenUri)
        jsonObject.put("auth_provider_x509_cert_url", authProviderCertUrl)
        jsonObject.put("client_x509_cert_url", clientCertUrl)
        val inputStream = jsonObject.toString().byteInputStream(Charsets.UTF_8)
        return GoogleCredentials.fromStream(inputStream)
    }
}