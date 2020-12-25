package io.learnet.starter.service.api.aws

import io.learnet.starter.util.properties.DigitalOceanProps

class UploadObject(private val digitalOceanProps: DigitalOceanProps) : S3 {

    override fun upload(bucketName: String, fileName: String) {
        val s3Client: AmazonS3
    }
}