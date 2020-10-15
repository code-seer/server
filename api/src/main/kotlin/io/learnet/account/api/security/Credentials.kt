package io.learnet.account.api.security

import com.google.firebase.auth.FirebaseToken

data class Credentials(
        private var type: CredentialType,
        private var decodedToken: FirebaseToken,
        private var idToken: String

) {
    enum class CredentialType {
        ID_TOKEN, SESSION
    }
}
