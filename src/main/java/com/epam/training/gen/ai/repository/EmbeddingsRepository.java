package com.epam.training.gen.ai.repository;

import static io.qdrant.client.VectorsFactory.vectors;
import static io.qdrant.client.WithPayloadSelectorFactory.enable;

import io.qdrant.client.PointIdFactory;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections;
import io.qdrant.client.grpc.Collections.VectorParams;
import io.qdrant.client.grpc.JsonWithInt.Value;
import io.qdrant.client.grpc.Points.PointStruct;
import io.qdrant.client.grpc.Points.ScoredPoint;
import io.qdrant.client.grpc.Points.SearchPoints;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingsRepository {

  private static final String COLLECTION_NAME = "embeddings";

  private final QdrantClient qdrantClient;

  @PostConstruct
  public void init() {
    try {
      var result = qdrantClient.createCollectionAsync(COLLECTION_NAME,
        VectorParams.newBuilder().setDistance(Collections.Distance.Cosine).setSize(1536).build()).get();
      log.info("Collection was created: [{}]", result.getResult());
    } catch (Exception e) {
      log.error("Failed to create collection " + COLLECTION_NAME, e);
    }
  }

  public void save(List<List<Float>> embeddings, String text) {
    var pointStructs = new ArrayList<PointStruct>();
    embeddings.forEach(point -> {
      var pointStruct = toPointStruct(point, text);
      pointStructs.add(pointStruct);
    });

    try {
      qdrantClient.upsertAsync(COLLECTION_NAME, pointStructs).get();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public List<ScoredPoint> find(List<List<Float>> embeddings) {
    var qe = new ArrayList<Float>();
    embeddings.forEach(qe::addAll);
    try {
      return qdrantClient.searchAsync(
        SearchPoints.newBuilder().setCollectionName(COLLECTION_NAME).addAllVector(qe).setWithPayload(enable(true))
          .setLimit(1).build()).get();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private PointStruct toPointStruct(List<Float> point, String text) {
    return PointStruct.newBuilder().setId(PointIdFactory.id(UUID.randomUUID())).setVectors(vectors(point))
      .putPayload("text", Value.newBuilder().setStringValue(text).build()).build();
  }
}
