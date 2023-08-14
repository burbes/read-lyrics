package com.example.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class LyricRequest implements Serializable {

  private static final long serialVersionUID = 622283950534046274L;

  private String title;
  private String artist;

  private MultipartFile file;
}
