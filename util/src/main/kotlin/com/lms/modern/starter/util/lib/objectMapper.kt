package com.lms.modern.starter.util.lib

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

/**
 * Wraps around Kotlin's jackson object mapper to prevent bean ambiguity during injection.
 */
class CustomObjectMapper {
     open val o = jacksonObjectMapper()
}