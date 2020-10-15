package io.learnet.account.util.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("firebase")
class FirebaseProps {
    lateinit var type: String
    lateinit var projectId: String
    lateinit var privateKeyId: String
    lateinit var privateKey: String
    lateinit var clientEmail: String
    lateinit var clientId: String
    lateinit var authUri: String
    lateinit var tokenUri: String
    lateinit var clientApiKey: String
    lateinit var customTokenVerificationUrl: String
    lateinit var clientX509CertUrl: String
    lateinit var authProviderX509CertUrl: String
}
