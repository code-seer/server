package io.learnet.starter.service.api.aws

import io.learnet.starter.util.properties.S3Props
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.model.*
import java.io.File
import java.io.IOException

import java.io.FileOutputStream

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

    fun uploadObject(bucketName: String, objectKey: String, multipartFile: MultipartFile): String {
        return uploadObject(bucketName, objectKey, multiPartToFile(multipartFile))
    }

    fun uploadObject(bucketName: String, objectKey: String, file: File): String {
        val s3Client = getS3Client()
        createBucket(s3Client, bucketName)
        try {
            val response: PutObjectResponse = s3Client.putObject(
                PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build(),
                RequestBody.fromFile(file))
            val urlRequest = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build()
            return s3Client.utilities().getUrl(urlRequest).toString()
        } catch (e: S3Exception) {
            return e.message.toString()
        }
    }
    @Throws(IOException::class)
    private fun multiPartToFile(file: MultipartFile): File {
        val convFile = File(file.originalFilename)
        val fos = FileOutputStream(convFile)
        fos.write(file.bytes)
        fos.close()
        return convFile
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
}