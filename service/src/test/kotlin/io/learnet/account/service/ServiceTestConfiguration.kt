package io.learnet.account.service

import io.learnet.account.util.FirebaseConfig
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Import

@SpringBootConfiguration
@EnableAutoConfiguration
@Import(value = [ServiceConfiguration::class, FirebaseConfig::class])
class ServiceTestConfiguration
