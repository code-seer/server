package com.lms.modern.starter.api.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.lms.modern.starter.api.ApiTestConfiguration
import com.lms.modern.starter.model.DemoUserResponse
import com.lms.modern.starter.model.PageableRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


@SpringBootTest(classes = [ApiTestConfiguration::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringSecurityTest: AbstractTestNGSpringContextTests() {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @LocalServerPort
    private val port = 0

    @Autowired
    private val restTemplate: TestRestTemplate? = null

    private val objectMapper = jacksonObjectMapper()

    private val testUrl = "/api/starter/demoUser/findAllUsers"
    private val host = "localhost"

    @Test
    fun demo_user_endpoint_test() {
        val uri = "http://${host}:${port}${testUrl}"
        val headers = HttpHeaders()
        val request = PageableRequest()
        request.limit = 25
        request.offset = 0
        headers.contentType = MediaType.APPLICATION_JSON
        val response = restTemplate?.postForEntity(uri, request, String::class.java)
        assertNotNull(response)
        assertEquals(response.statusCodeValue, 200)
        val demoUserResponse = objectMapper.readValue(response.body, DemoUserResponse::class.java)
    }
}