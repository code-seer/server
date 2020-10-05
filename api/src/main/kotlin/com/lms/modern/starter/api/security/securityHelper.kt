package com.lms.modern.starter.api.security

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserRecord
import com.lms.modern.starter.model.UserRole
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.springframework.boot.configurationprocessor.json.JSONObject
import java.util.HashMap

const val displayName = "LMS Demo User"
const val email = "bizmelesse@gmail.com"
const val password = "demouser"
const val phoneNumber = "+11234567890"
const val photoUrl = "https://avatars0.githubusercontent.com/u/19600025?s=460&u=0743d9d8e8ebefe36990ad498ede9fc0df96e18f&v=4"

fun createUser(): UserRecord {
    val request: UserRecord.CreateRequest = UserRecord.CreateRequest()
            .setEmail(email)
            .setEmailVerified(false)
            .setPassword(password)
            .setPhoneNumber(phoneNumber)
            .setDisplayName(displayName)
            .setPhotoUrl(photoUrl)
            .setDisabled(false)
    return FirebaseAuth.getInstance().createUser(request)
}

fun deleteUser() {
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
fun login(firebaseConfig: FirebaseConfig, objectMapper: ObjectMapper): String? {
    val userRecord = FirebaseAuth.getInstance().getUserByEmail(email)
    val customToken = FirebaseAuth.getInstance().createCustomToken(userRecord.uid)
    val httpClient = HttpClients.createDefault()
    val json = JSONObject()
    json.put("token", customToken)
    json.put("returnSecureToken", true)
    val params = StringEntity(json.toString())
    val request = HttpPost("${firebaseConfig.customTokenVerificationUrl}?key=${firebaseConfig.clientApiKey}")
    request.addHeader("content-type", "application/json")
    request.entity = params;
    val typeRef: TypeReference<HashMap<String, Any>> = object : TypeReference<HashMap<String, Any>>() {}
    val map = objectMapper.readValue(httpClient.execute(request).entity.content, typeRef)
    httpClient.close()
    val idToken = map["idToken"] as String?
    return idToken
}