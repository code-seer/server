package com.lms.modern.starter.api.security

import com.google.firebase.auth.FirebaseAuth
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SecurityFilter: OncePerRequestFilter() {

//    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        verifyToken(request)
        filterChain.doFilter(request, response)
    }

    private fun verifyToken(request: HttpServletRequest) {
        var bearerToken: String? = null
        val authorization = request.getHeader("Authorization")
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            bearerToken = authorization.substring(7, authorization.length)
        }
        val decodedToken = FirebaseAuth.getInstance().verifyIdToken(bearerToken)
//        decodedToken
        val x = 1
//        var sessionCookieValue: String? = null
//        var decodedToken: FirebaseToken? = null
//        var type: CredentialType? = null
//        val strictServerSessionEnabled: Boolean = securityProps.getFirebaseProps().isEnableStrictServerSession()
//        val sessionCookie: Cookie = cookieUtils.getCookie("session")
//        val token: String = securityService.getBearerToken(request)
//        try {
//            if (sessionCookie != null) {
//                sessionCookieValue = sessionCookie.value
//                decodedToken = FirebaseAuth.getInstance().verifySessionCookie(sessionCookieValue,
//                        securityProps.getFirebaseProps().isEnableCheckSessionRevoked())
//                type = CredentialType.SESSION
//            } else if (!strictServerSessionEnabled) {
//                if (token != null && !token.equals("undefined", ignoreCase = true)) {
//                    decodedToken = FirebaseAuth.getInstance().verifyIdToken(token)
//                    type = CredentialType.ID_TOKEN
//                }
//            }
//        } catch (e: FirebaseAuthException) {
//            e.printStackTrace()
//            SecurityFilter.log.error("Firebase Exception:: ", e.localizedMessage)
//        }
//        val user: User? = firebaseTokenToUserDto(decodedToken)
//        if (user != null) {
//            val authentication = UsernamePasswordAuthenticationToken(user,
//                    Credentials(type, decodedToken, token, sessionCookieValue), null)
//            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
//            SecurityContextHolder.getContext().authentication = authentication
//        }
    }

//    private fun firebaseTokenToUserDto(decodedToken: FirebaseToken?): User? {
//        var user: User? = null
//        if (decodedToken != null) {
//            user = User()
//            user.setUid(decodedToken.uid)
//            user.setName(decodedToken.name)
//            user.setEmail(decodedToken.email)
//            user.setPicture(decodedToken.picture)
//            user.setIssuer(decodedToken.issuer)
//            user.setEmailVerified(decodedToken.isEmailVerified)
//        }
//        return user
//    }
}