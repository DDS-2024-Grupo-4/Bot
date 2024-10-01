package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.service.DDMetricsUtils;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MetricController {
  private final DDMetricsUtils metricsUtils;

  public MetricController(DDMetricsUtils metricsUtils) {
    this.metricsUtils = metricsUtils;
  }
  public void aperturaHeladera(Context context) {
    try {
      metricsUtils.getRegistry().counter("heladera.aperturas").increment();
      context.status(HttpStatus.OK);
      log.info("Apertura de heladera registrada.");
    } catch (Exception e) {
      context.status(HttpStatus.INTERNAL_SERVER_ERROR);
      log.error("Error al registrar la apertura de la heladera", e);
    }
  }
}
