package io.learnet.starter.service.api.aws

import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectResponse
import java.io.File
import software.amazon.awssdk.services.s3.model.S3Exception
import java.io.IOException

import java.io.FileInputStream
import java.nio.ByteBuffer
import java.util.*


class S3 {

    private fun getS3Client(): S3Client {
        val credentials = AwsBasicCredentials.create("test", "test")
        val credentialsProvider = StaticCredentialsProvider.create(credentials)
        return S3Client.builder()
            .credentialsProvider(credentialsProvider)
            .region(Region.US_EAST_1)
            .build()
    }

    fun uploadObject(bucketName: String, fileName: String): String {
        val s3Client = getS3Client()
        return putS3ObjectFromByteBuffer(s3Client, bucketName, "objectKey")
    }

    private fun putS3ObjectFromByteBuffer(s3: S3Client,
                            bucketName: String,
                            objectKey: String): String {
        try {
            val response: PutObjectResponse = s3.putObject(
                PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build(),
                RequestBody.fromByteBuffer(getRandomByteBuffer(10000)))
            return response.eTag()
        } catch (e: S3Exception) {
            e.printStackTrace()
        }
        return ""
    }

    @Throws(IOException::class)
    private fun getRandomByteBuffer(size: Int): ByteBuffer {
        val b = ByteArray(size)
        Random().nextBytes(b)
        return ByteBuffer.wrap(b)
    }

}