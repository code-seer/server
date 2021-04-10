package io.learnet.util

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.learnet.util.properties.FirebaseProps
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.io.IOException


@Configuration
class FirebaseConfig {

    @Autowired
    private lateinit var firebaseProps: FirebaseProps

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
        jsonObject.put("type", firebaseProps.type)
        jsonObject.put("project_id", firebaseProps.projectId)
        jsonObject.put("private_key_id", firebaseProps.privateKeyId)
        jsonObject.put("private_key", firebaseProps.privateKey)
        jsonObject.put("client_email", firebaseProps.clientEmail)
        jsonObject.put("client_id", firebaseProps.clientId)
        jsonObject.put("auth_uri", firebaseProps.authUri)
        jsonObject.put("token_uri", firebaseProps.tokenUri)
        jsonObject.put("auth_provider_x509_cert_url", firebaseProps.authProviderX509CertUrl)
        jsonObject.put("client_x509_cert_url", firebaseProps.clientX509CertUrl)
        val inputStream = jsonObject.toString().byteInputStream(Charsets.UTF_8)
        return GoogleCredentials.fromStream(inputStream)
    }
}
