package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LyricsController {

    private final S3Reader s3Reader;

    public LyricsController() {
        this.s3Reader = new S3Reader();
    }

    @GetMapping("/lyrics")
    public ResponseEntity<String> getLyric() {
        try {

            S3Reader s3Reader = new S3Reader();
            String bucket = "myamazingbucket";
            String key = "munra.rtf";

            String fileContent = s3Reader.getFileContent(bucket, key);
            log.info("fileContent: {}", fileContent);

            return ResponseEntity.ok(fileContent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch lyrics: " + e.getMessage());
        }
    }
}
