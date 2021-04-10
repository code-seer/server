package io.learnet.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.learnet.util.properties.FirebaseProps
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.io.IOException


@Configuration
class FirebaseConfig {

    @Autowired
    private lateinit var firebaseProps: FirebaseProps

    @Autowired
    @Qualifier("jacksonObjectMapper")
    lateinit var objectMapper: ObjectMapper

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
         val objectNode = objectMapper.createObjectNode()
         objectNode.put("type", firebaseProps.type)
         objectNode.put("project_id", firebaseProps.projectId)
         objectNode.put("private_key_id", firebaseProps.privateKeyId)
         objectNode.put("private_key", firebaseProps.privateKey)
         objectNode.put("client_email", firebaseProps.clientEmail)
         objectNode.put("client_id", firebaseProps.clientId)
         objectNode.put("auth_uri", firebaseProps.authUri)
         objectNode.put("token_uri", firebaseProps.tokenUri)
         objectNode.put("auth_provider_x509_cert_url", firebaseProps.authProviderX509CertUrl)
         objectNode.put("client_x509_cert_url", firebaseProps.clientX509CertUrl)
         val inputStream = objectMapper.writeValueAsString(objectNode).byteInputStream(Charsets.UTF_8)
        return GoogleCredentials.fromStream(inputStream)
    }
}
