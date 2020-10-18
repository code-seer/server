package io.learnet.account.api.security

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserRecord
import io.learnet.account.util.properties.DemoUserProps
import io.learnet.account.util.properties.FirebaseProps
import io.learnet.account.model.UserRole
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.springframework.boot.configurationprocessor.json.JSONObject
import java.util.HashMap

fun createUser(demoUserProps: DemoUserProps): UserRecord {
    val request: UserRecord.CreateRequest = UserRecord.CreateRequest()
            .setEmail(demoUserProps.email)
            .setEmailVerified(false)
            .setPassword(demoUserProps.password)
            .setPhoneNumber(demoUserProps.phoneNumber)
            .setDisplayName(demoUserProps.displayName)
            .setPhotoUrl(demoUserProps.photoUrl)
            .setDisabled(false)
    return FirebaseAuth.getInstance().createUser(request)
}

fun deleteUser(email: String) {
    try {
        FirebaseAuth.getInstance().deleteUser(FirebaseAuth.getInstance().getUserByEmail(email).uid)
    } catch (e: FirebaseAuthException) {
        println(e.message)
    }
}

fun createClaims(userRecord: UserRecord, removeClaim: Boolean) {
    val claims =  hashMapOf<String, Boolean>()
    claims[UserRole.ADMIN.value] = true
    claims[UserRole.TEST.value] = true
    if (!removeClaim) {
        claims[UserRole.DEMO_USER_READ.value] = true
    }
    FirebaseAuth.getInstance().setCustomUserClaims(userRecord.uid, claims as Map<String, Any>?)
}

/**
 * Login the newly created user. Firebase gives us the option of handling login on the client side
 * by calling the Firebase Client SDK directly or by generating a custom token on the server side.
 * We weill generate a custom token here and set the header with it. We will then send the request object
 * with the Authorization header when making our http request. At this point we claim that the user
 * is logged in. The setting of the header with the custom token completes the login process. Typically
 * this would involve getting login credentials from the UI and sending those along to Firebase in the
 * client code. But since there is no Firebase Client SDK for Java (only Android), we cannot perform
 * the routine login steps here. Instead, we bypass the UI part of the process.
 *
 * Once Spring Boot receives the request, its security filter should call the Firebase
 * Admin SDK and verify the token and decode the claims.
 */
fun login(firebaseProps: FirebaseProps, objectMapper: ObjectMapper, email: String): String? {
    val userRecord = FirebaseAuth.getInstance().getUserByEmail(email)
    val customToken = FirebaseAuth.getInstance().createCustomToken(userRecord.uid)
    val httpClient = HttpClients.createDefault()
    val json = JSONObject()
    json.put("token", customToken)
    json.put("returnSecureToken", true)
    val params = StringEntity(json.toString())
    val request = HttpPost("${firebaseProps.customTokenVerificationUrl}?key=${firebaseProps.clientApiKey}")
    request.addHeader("content-type", "application/json")
    request.entity = params;
    val typeRef: TypeReference<HashMap<String, Any>> = object : TypeReference<HashMap<String, Any>>() {}
    val map = objectMapper.readValue(httpClient.execute(request).entity.content, typeRef)
    httpClient.close()
    return map["idToken"] as String?
}
