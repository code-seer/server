package com.lms.modern.starter.search


import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Import

@SpringBootConfiguration
@EnableAutoConfiguration
@Import(value = [SearchConfiguration::class])
class SearchTestConfiguration