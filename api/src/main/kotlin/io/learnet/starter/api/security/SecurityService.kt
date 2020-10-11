package io.learnet.starter.api.security

import io.learnet.starter.model.UserDto
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.util.*
import javax.servlet.http.HttpServletRequest


@Service
class SecurityService {

    val user: UserDto?
        get() {
            var userPrincipal: UserDto? = null
            val securityContext = SecurityContextHolder.getContext()
            val principal = securityContext.authentication.principal
            if (principal is UserDto) {
                userPrincipal = principal
            }
            return userPrincipal
        }

    fun getCredentials(): Credentials? {
        val securityContext = SecurityContextHolder.getContext()
        return securityContext.authentication.credentials as Credentials
    }

    fun getBearerToken(request: HttpServletRequest): String {
        var bearerToken = String()
        val authorization = request.getHeader("Authorization")
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            bearerToken = authorization.substring(7, authorization.length)
        }
        return bearerToken
    }

    fun getAuthorities(claims: Map<String, Any>): Collection<GrantedAuthority> {
        val authorities: MutableList<SimpleGrantedAuthority> = ArrayList()
        for ((claim: String, value) in claims) {
            if (claim.startsWith("ROLE_") && value == true) {
                authorities.add(SimpleGrantedAuthority(claim))
            }
        }
        return authorities
    }

}
