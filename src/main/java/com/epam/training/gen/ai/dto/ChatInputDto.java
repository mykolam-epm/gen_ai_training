package com.epam.training.gen.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatInputDto {

  private String input;

  @Builder.Default private String model = "gpt-35-turbo";

  @Builder.Default private float temperature = 0.8f;
}
