package io.learnet.service

import io.learnet.web.model.UserDto
import io.learnet.util.FirebaseConfig
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import

@SpringBootConfiguration
@EnableAutoConfiguration
@Import(value = [ServiceConfiguration::class, FirebaseConfig::class])
class ServiceTestConfiguration {

    private val testEmail = "learnetuser@learnet.io"

    @Bean
    fun userDto(): UserDto {
        val userDto = UserDto()
        userDto.email = testEmail
        return userDto
    }
}
