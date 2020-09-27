package com.lms.modern.starter.api.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.lms.modern.starter.api.ApiTestConfiguration
import com.lms.modern.starter.model.DemoUserResponse
import com.lms.modern.starter.model.PageableRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
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
class ApiTest: AbstractTestNGSpringContextTests() {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @LocalServerPort
    private val port = 0

    @Autowired
    private val restTemplate: TestRestTemplate? = null

    @Autowired
    @Qualifier("jacksonObjectMapper")
    lateinit var objectMapper: ObjectMapper

    private val testUrl = "/api/starter/demoUser/findAllUsers"
    private val host = "localhost"

    @Test
    fun demo_user_endpoint_test() {
        val uri = "http://${host}:${port}${testUrl}"
        val headers = HttpHeaders()
        val request = PageableRequest()
        request.limit = 25
        request.offset = 0
        request.sortBy = arrayOf("lastName").toMutableList()
        request.sortDir = arrayOf("ASC").toMutableList()
        headers.contentType = MediaType.APPLICATION_JSON
        val response = restTemplate?.postForEntity(uri, request, String::class.java)
        assertNotNull(response)
        assertEquals( 200, response.statusCodeValue)
        val demoUserResponse = objectMapper.readValue(response.body, DemoUserResponse::class.java)
        assertNotNull(demoUserResponse)
        assertEquals( 4, demoUserResponse.numPages)
        assertEquals(100, demoUserResponse.totalRecords)
        assertEquals( "Angelo", demoUserResponse.content[7].firstName)
        assertEquals( "Carter", demoUserResponse.content[7].lastName)
    }
}