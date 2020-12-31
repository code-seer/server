package io.learnet.starter.service.api.aws

import io.learnet.starter.util.properties.S3Props
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.model.*
import java.io.IOException

import java.nio.ByteBuffer
import java.util.*

@Service
class S3(private val s3Props: S3Props) {

    private fun getS3Client(): S3Client {
        val credentials = AwsBasicCredentials.create(s3Props.key, s3Props.secret)
        val credentialsProvider = StaticCredentialsProvider.create(credentials)
        return S3Client.builder()
            .credentialsProvider(credentialsProvider)
            .region(Region.US_EAST_1)
            .build()
    }

    fun uploadObject(bucketName: String, fileName: String): String {
        val s3Client = getS3Client()
        createBucket(s3Client, bucketName)
        return putS3ObjectFromByteBuffer(s3Client, bucketName, "objectKey")
    }

    private fun createBucket(s3Client: S3Client, bucketName: String) {
       try {
           s3Client.headBucket(HeadBucketRequest.builder()
               .bucket(bucketName)
               .build())
       } catch (e: NoSuchBucketException) {
           s3Client.createBucket(CreateBucketRequest.builder()
               .bucket(bucketName)
               .build())
       }
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