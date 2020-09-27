package com.lms.modern.starter.api.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import java.io.IOException
import java.sql.Timestamp
import java.util.*
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.HashMap

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    @Qualifier("jacksonObjectMapper")
    lateinit var objectMapper: ObjectMapper

    @Autowired
    private val tokenAuthenticationFilter: SecurityFilter? = null

    @Bean
    fun restAuthenticationEntryPoint(): AuthenticationEntryPoint {
        return AuthenticationEntryPoint { httpServletRequest, httpServletResponse, e ->
            val errorObject: MutableMap<String, Any> = HashMap()
            val errorCode = 401
            errorObject["message"] = "Unauthorized access of protected resource, invalid credentials"
            errorObject["error"] = HttpStatus.UNAUTHORIZED
            errorObject["code"] = errorCode
            errorObject["timestamp"] = Timestamp(Date().time)
            httpServletResponse.contentType = "application/json;charset=UTF-8"
            httpServletResponse.status = errorCode
            httpServletResponse.writer.write(objectMapper!!.writeValueAsString(errorObject))
        }
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true;
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config)
        return source
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors().configurationSource(corsConfigurationSource()).and().csrf().disable().formLogin().disable()
                .httpBasic().disable().exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint())
                .and().authorizeRequests()
//                .antMatchers(restSecProps.getAllowedPublicApis().stream().toArray({ _Dummy_.__Array__() })).permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll().anyRequest().authenticated().and()
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }
}