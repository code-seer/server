package io.learnet.starter.service.api.aws

interface S3 {
    fun upload(bucketName:String, fileName: String)
}