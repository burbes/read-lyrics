package com.example.controller;

import com.example.model.LyricRequest;
import com.example.util.FileUtils;
import com.example.util.S3Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.QueryParam;
import java.io.File;

@RestController
@RequestMapping("/lyrics")
@Slf4j
public class LyricsController {

    private final S3Utils s3Utils;

    public LyricsController() {
        this.s3Utils = new S3Utils();
    }

    @GetMapping(path = "/{key}")
    public ResponseEntity<String> getLyric(@PathVariable String key) {
        try {

            String fileContent = s3Utils.getFileContent(key);
            log.info("fileContent: {}", fileContent);

            return ResponseEntity.ok(fileContent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch lyrics: " + e.getMessage());
        }
    }


    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> saveLyric(MultipartFile multipartFile) {
        try {
            File file = FileUtils.convertMultiPartToFile(multipartFile);
            String filePath = s3Utils.uploadFile(file.getName(), file);
            log.info("filePath: {}", filePath);

            return ResponseEntity.ok(filePath);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch lyrics: " + e.getMessage());
        }
    }

}
