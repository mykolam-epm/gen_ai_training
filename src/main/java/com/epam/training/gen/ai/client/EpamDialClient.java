package com.epam.training.gen.ai.client;

import com.epam.training.gen.ai.dto.EpamDialDeployments;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "dial", url = "${endpoint-modellist}", configuration = EpamDialClientConfig.class)
public interface EpamDialClient {

  @GetMapping("deployments")
  EpamDialDeployments listDeployments();
}
