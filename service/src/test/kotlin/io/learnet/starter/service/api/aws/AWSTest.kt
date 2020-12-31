package io.learnet.starter.service.api.aws


import io.learnet.starter.service.ServiceTestConfiguration
import io.learnet.starter.util.properties.S3Props
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.web.multipart.MultipartFile
import org.testng.annotations.*
import java.io.File
import java.lang.reflect.Method
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.springframework.mock.web.MockMultipartFile
import java.io.FileInputStream


@SpringBootTest(classes = [ServiceTestConfiguration::class])
class AWSTest: AbstractTestNGSpringContextTests() {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    lateinit var s3: S3

    @Autowired
    lateinit var s3Props: S3Props

    @BeforeClass
    fun beforeClass() {

    }

    @BeforeMethod
    fun beforeMethod(method: Method) {
        log.info("Testing ${method.name}")
    }

    @Test
    fun uploadObject() {
        val fileName = UUID.randomUUID().toString()
        val url = s3.uploadObject(s3Props.bucket,  fileName, loadFile())
        assertNotNull(url)
        assertEquals(url.contains(fileName), true)
    }

    private fun loadFile(): File {
        return File(this::class.java.getResource("/aws/cat.jpg").file)
    }

    @Test
    fun uploadObjectMultiPart() {
        val fileName = UUID.randomUUID().toString()
        val url = s3.uploadObject(s3Props.bucket,  fileName, fileToMultipart(loadFile()))
        assertNotNull(url)
        assertEquals(url.contains(fileName), true)
    }

    private fun fileToMultipart(file: File): MultipartFile {
        return MockMultipartFile("mr.cat", file.name, MediaType.IMAGE_JPEG.toString(), FileInputStream(file))
    }

}
