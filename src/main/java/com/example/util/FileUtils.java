package com.example.util;

import com.example.model.LyricRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Slf4j
public class FileUtils {

//  public static File convertMultiPartToFile(LyricRequest request) {
  public static File convertMultiPartToFile(MultipartFile multipartFile) {


    MultipartFile file = multipartFile;
    String baseName =
            new SimpleDateFormat("yyyy-MM-dd-HHmmss-SSS-")
                    .format(new Date())
                    .concat(multipartFile.getName());

    Path tempDir = null;
    try {
      tempDir = Files.createTempDirectory("");
    } catch (IOException e) {
      log.error("Error creating temp directory", e);
      throw new RuntimeException("Error creating temp directory", e);
    }
    String originalFileName = file.getOriginalFilename();
    String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
    File tempFile = tempDir.resolve(baseName + extension).toFile();
    try {
      file.transferTo(tempFile);
    } catch (IOException e) {
      log.error("Error converting multipart file to file", e);
      throw new RuntimeException("Error converting multipart file to file", e);
    }
    return tempFile;
  }

}
