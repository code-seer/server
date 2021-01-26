package io.learnet.account.service

import io.learnet.account.model.UserDto
import io.learnet.account.search.SearchConfiguration
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.context.annotation.RequestScope


@Configuration
@ComponentScan
@Import(value = [
    SearchConfiguration::class
])
class ServiceConfiguration {
    @RequestScope
    @Bean
    fun getUserPrincipal(): UserDto {
        return SecurityContextHolder.getContext().authentication.principal as UserDto
    }
}
