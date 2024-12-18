package com.epam.training.gen.ai.config;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up the Qdrant Client.
 * <p>
 * This configuration defines a bean that provides a client for interacting with a Qdrant service. The client is built
 * using gRPC to connect to a Qdrant instance running at the specified host and port.
 */

@Configuration
public class QdrantConfiguration {

  @Value("${qdrant.host:localhost}") private String qdrantHost;

  @Value("${qdrant.post:6334}") private int qdrantPort;

  @Value("${qdrant.tls:false}") private boolean qdrantUseTls;

  /**
   * Creates a {@link QdrantClient} bean for interacting with the Qdrant service.
   *
   * @return an instance of {@link QdrantClient}
   */
  @Bean
  public QdrantClient qdrantClient() {
    return new QdrantClient(QdrantGrpcClient.newBuilder(qdrantHost, qdrantPort, qdrantUseTls).build());
  }
}
