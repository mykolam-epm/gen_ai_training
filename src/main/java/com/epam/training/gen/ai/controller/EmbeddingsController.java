package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.EmbeddingsService;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class EmbeddingsController {

  private final EmbeddingsService embeddingsService;

  @PostMapping("/embeddings/ingest")
  public void uploadFiles(@RequestParam("files") MultipartFile[] files) {
    Stream.of(files).filter(f -> !f.isEmpty()).map(this::toContent).forEach(embeddingsService::saveEmbeddings);
  }

  private String toContent(MultipartFile fileupload) {
    try {
      return "File " + fileupload.getName() + " contents: \n\n\"\"\"" + new String(fileupload.getBytes(),
        StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
