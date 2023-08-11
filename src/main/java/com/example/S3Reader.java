package com.example;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Slf4j
public class S3Reader {

    public enum Env {
        AWS, LOCALSTACK
    }

    private static AwsClientBuilder.EndpointConfiguration endpointConfiguration = null;
    private static AmazonS3 amazonS3 = null;

    public static void setEndpointConfiguration(AwsClientBuilder.EndpointConfiguration endpointConfiguration) {
        S3Reader.endpointConfiguration = endpointConfiguration;
    }

    public static void setAmazonS3(AmazonS3 amazonS3) {
        S3Reader.amazonS3 = amazonS3;
    }

    public static AmazonS3 getS3(Env env) {
        if (env == Env.AWS)
            return getS3Aws();
        else if (env == Env.LOCALSTACK)
            return getS3LocalStack();
        else
            return null;
    }

    private static AmazonS3 getS3Aws() {
        if (amazonS3 == null)  {
            amazonS3 = AmazonS3ClientBuilder.standard().build();
        }
        return amazonS3;
    }

    private static AmazonS3 getS3LocalStack() {
        if (amazonS3 == null)  {
            boolean docker = true;
//            boolean docker = "true".equals(System.getenv(Constants.ENV_DOCKER));
            log.info("docker: " + docker);

            if (endpointConfiguration == null) {
                if (docker)
                    endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(Constants.S3_ENDPOINT_DOCKER, Constants.AWS_REGION);
                else
                    endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(Constants.S3_ENDPOINT, Constants.AWS_REGION);
            }
            AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
            builder.withEndpointConfiguration(endpointConfiguration);

            if (docker)
                builder.setPathStyleAccessEnabled(true);

            amazonS3 = builder.build();
        }
        return amazonS3;
    }


    public String getFileContent(String bucketName, String key){
        Env env = Env.LOCALSTACK;
        AmazonS3 s3 = getS3(env);
        log.info("env: " + env);

        S3Object object = s3.getObject(bucketName, key);
        try (InputStream contentStream = object.getObjectContent();
             Scanner scanner = new Scanner(contentStream, StandardCharsets.UTF_8)) {
            // Scanner trick to read the entire stream into a String
            return scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the object content", e);
        }
    }

}
