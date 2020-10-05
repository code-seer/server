package com.lms.modern.starter.api.security

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import com.lms.modern.starter.model.UserDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SecurityFilter: OncePerRequestFilter() {

    @Autowired
    private lateinit var securityService: SecurityService

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        // All non-preflight requests must have a valid authorization token
        if (request.method != "OPTIONS") {
            verifyToken(request)
        }
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