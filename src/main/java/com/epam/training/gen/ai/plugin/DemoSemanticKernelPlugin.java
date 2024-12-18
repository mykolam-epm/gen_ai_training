package com.epam.training.gen.ai.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DemoSemanticKernelPlugin {

  @DefineKernelFunction(name = "getWeather", description = "Provides weather for given coordinates (latitude, longitude).")
  public String getWeather(
    @KernelFunctionParameter(description = "Latitude of place to provide weather for", name = "latitude")
    String latitude,
    @KernelFunctionParameter(description = "Longitude of place to provide weather for", name = "longitude")
    String longitude) {
    log.info("Weather plugin was called with query: [{}, {}]", latitude, longitude);
    try {
      double latitudeVal = Double.parseDouble(latitude);
      double longitudeVal = Double.parseDouble(longitude);
      return latitudeVal > 0 ? "It is cold with a bit of snow" : "It is sunny, with occasional clouds";
    } catch (NumberFormatException nfe) {
      return "Sorry, I don't know what the weather is like at that place - I don't know where it is.";
    }
  }
}
