package io.learnet.accout.util

import io.learnet.account.util.UtilConfiguration
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Import

@SpringBootConfiguration
@EnableAutoConfiguration
@Import(value = [UtilConfiguration::class])
class UtilTestConfiguration