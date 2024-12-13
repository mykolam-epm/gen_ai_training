package com.epam.training.gen.ai.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EpamDialDeployments {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class EpamDialDeployment {

    private String id;
    private String model;
  }

  private List<EpamDialDeployment> data;
}
