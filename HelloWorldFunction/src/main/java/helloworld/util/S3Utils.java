package helloworld.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

@Slf4j
public class S3Utils {

    public enum Env {
        AWS, LOCALSTACK
    }

    private static final String BUCKET_NAME = "read-lyrics-bucket";

    private static AwsClientBuilder.EndpointConfiguration endpointConfiguration = null;
    private static AmazonS3 amazonS3 = null;

    public static void setEndpointConfiguration(AwsClientBuilder.EndpointConfiguration endpointConfiguration) {
        S3Utils.endpointConfiguration = endpointConfiguration;
    }

    public static void setAmazonS3(AmazonS3 amazonS3) {
        S3Utils.amazonS3 = amazonS3;
    }

    public static AmazonS3 getS3() {
        Env env = Objects.equals(System.getenv("ENVIRONMENT"), "AWS") ? Env.AWS : Env.LOCALSTACK;
        log.info("env: {}", env);
//        if (env == Env.AWS)
            return getS3Aws();
//        else
//        return getS3LocalStack();
    }

    private static AmazonS3 getS3Aws() {
        log.info("Aws Profile Selected");
        if (amazonS3 == null)  {
            amazonS3 = AmazonS3ClientBuilder.standard().build();
        }
        return amazonS3;
    }

    private static AmazonS3 getS3LocalStack() {
        log.info("Localstack Profile Selected");
        if (amazonS3 == null)  {
            boolean docker = !Objects.nonNull(System.getenv(Constants.ENV_DOCKER)) || System.getenv(Constants.ENV_DOCKER).equals("true");
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


    public String getFileContent(String key){
        AmazonS3 s3 = getS3();

        S3Object object = s3.getObject(BUCKET_NAME, key);
        try (InputStream contentStream = object.getObjectContent();
             Scanner scanner = new Scanner(contentStream, StandardCharsets.UTF_8)) {
            // Scanner trick to read the entire stream into a String
            return scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the object content", e);
        }
    }

    public String uploadFile(String key, File file) {
        try {
            AmazonS3 s3 = getS3();
            s3.putObject(new PutObjectRequest(BUCKET_NAME, key, file));
            URL url = s3.getUrl(BUCKET_NAME, key);
            return url.toString();
        } catch (AmazonServiceException e) {
            throw new RuntimeException("Error uploading file to S3", e);
        }
    }

}
