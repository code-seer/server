package com.lms.modern.starter.api.security

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
import com.lms.modern.starter.model.UserDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SecurityFilter: OncePerRequestFilter() {

    @Autowired
    private lateinit var securityService: SecurityService

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        verifyToken(request)
        filterChain.doFilter(request, response)
    }

    private fun verifyToken(request: HttpServletRequest) {
        var bearerToken: String = securityService.getBearerToken(request)
        val decodedToken = FirebaseAuth.getInstance().verifyIdToken(bearerToken)
        val userDto = firebaseTokenToUserDto(decodedToken)
        val authentication = UsernamePasswordAuthenticationToken(userDto,
                Credentials(Credentials.CredentialType.ID_TOKEN, decodedToken, bearerToken),
                securityService.getAuthorities(userDto.roles))
        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authentication


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

    private fun firebaseTokenToUserDto(decodedToken: FirebaseToken?): UserDto {
        val userDto = UserDto()
        if (decodedToken != null) {
            userDto.uid = decodedToken.uid
            userDto.name = decodedToken.name
            userDto.email = decodedToken.email
            userDto.picture = decodedToken.picture
            userDto.isEmailVerified(decodedToken.isEmailVerified)
            userDto.roles = decodedToken.claims
        }
        return userDto
    }
}