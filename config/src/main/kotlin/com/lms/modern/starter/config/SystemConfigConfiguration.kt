package com.lms.modern.starter.config

import com.lms.modern.starter.util.UtilConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * Loads Spring Boot configuration variables from the environment. We use environment
 * variables instead of bootstrap.yaml to allow each environment to configure its own
 * profile and specify the location of the configuration server. Typically, this is done
 * in bootstrap.yaml like so:
 *
 *  spring:
 *      profiles:
 *          active: dev
 *      application:
 *          name: course
 *      cloud:
 *          config:
 *              uri: http://localhost:8888
 *              enabled: true
 *              fail-fast: true
 *
 * Thi would not be ideal if bootstrap.yaml gets checked in. We would have to manually edit
 * the file so the profile is set to prod for production. In addition, the uri of the config
 * server would be different in a production environment. We avoid the problem of having to
 * modify this family by setting environment variables instead. Spring can load them from the
 * environment as long as the variable names are consistent with the yaml dotted names. For
 * example, spring.profiles.active=dev becomes SPRING_PROFILES_ACTIVE=dev. Our sample
 * bootstrap.yaml file turns into the following set of environment variables:
 *
 *      spring.profiles.active=dev                      | SPRING_PROFILES_ACTIVE=dev
 *      spring.application.name=course                  | SPRING_APPLICATION_NAME=course
 *      spring.cloud.config.ur=http://localhost:8888    | SPRING_CLOUD_CONFIG_URI=http://localhost:8888
 *      spring.cloud.config.enabled=true                | SPRING_CLOUD_CONFIG_ENABLED=true
 *      spring.cloud.config.fail-fast=true              | SPRING_CLOUD_CONFIG_FAIL_FAST=true
 */
@Configuration
@ComponentScan
@Import(value = [UtilConfiguration::class])
open class SystemConfigConfiguration {
    val activeProfile: String? = System.getenv("SPRING_PROFILES_ACTIVE")
    val appName: String = System.getenv("SPRING_APPLICATION_NAME")
    val uri: String = System.getenv("SPRING_CLOUD_CONFIG_URI")
}