package helloworld;

import com.amazonaws.services.s3.AmazonS3;
import helloworld.util.S3Utils;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

@Slf4j
public class DatabaseConfig {

    private static final String JDBC_URL_LOCALSTACK = "jdbc:mysql://host.docker.internal:3306/lyricsDB";
    private static final String JDBC_USER_LOCALSTACK = "root";
    private static final String JDBC_PASSWORD_LOCALSTACK = "my-secret-pw";

    private static final String JDBC_URL_AWS = "jdbc:mysql://lyricsdb-instance.cfz7gvc4iozi.us-east-1.rds.amazonaws.com:3306/lyricsDB";
    private static final String JDBC_USER_AWS = "admin";
    private static final String JDBC_PASSWORD_AWS = "my-secret-pw";

    public static Connection getConnectionLocalstack() throws SQLException {
        return DriverManager.getConnection(JDBC_URL_LOCALSTACK, JDBC_USER_LOCALSTACK, JDBC_PASSWORD_LOCALSTACK);
    }
    public static Connection getConnectionAws() throws SQLException {
        return DriverManager.getConnection(JDBC_URL_AWS, JDBC_USER_AWS, JDBC_PASSWORD_AWS);
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        S3Utils.Env env = Objects.equals(System.getenv("ENVIRONMENT"), "AWS") ? S3Utils.Env.AWS : S3Utils.Env.LOCALSTACK;
        log.info("database env: {}", env);
//        if (env == S3Utils.Env.AWS)
        Class.forName("com.mysql.cj.jdbc.Driver");

        return getConnectionAws();
//        else
//            return getConnectionLocalstack();
    }
}
