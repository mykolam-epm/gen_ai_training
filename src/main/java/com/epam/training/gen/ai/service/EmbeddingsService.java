package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.EmbeddingItem;
import com.azure.ai.openai.models.Embeddings;
import com.azure.ai.openai.models.EmbeddingsOptions;
import com.epam.training.gen.ai.repository.EmbeddingsRepository;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmbeddingsService {

  private final EmbeddingsRepository repo;
  private final OpenAIAsyncClient openAIAsyncClient;

  @Value("${embeddings.model:text-embedding-ada-002}") private String embeddingsModel;

  public void saveEmbeddings(String text) {
    var qembeddingsOptions = new EmbeddingsOptions(List.of(text));
    var embeddings = openAIAsyncClient.getEmbeddings(embeddingsModel, qembeddingsOptions).block();
    if (embeddings != null) {
      repo.save(mapToFloats(embeddings), text);
    }
  }

  private static List<List<Float>> mapToFloats(Embeddings embeddings) {
    return embeddings.getData().stream().map(EmbeddingItem::getEmbedding).toList();
  }

  public List<String> findEmbeddings(String text) {
    var qembeddingsOptions = new EmbeddingsOptions(List.of(text));
    var embeddings = openAIAsyncClient.getEmbeddings(embeddingsModel, qembeddingsOptions).block();
    if (embeddings != null) {
      return repo.find(mapToFloats(embeddings)).stream().map(p -> p.getPayloadMap().get("text").getStringValue())
        .toList();
    }
    return Collections.emptyList();
  }
}
