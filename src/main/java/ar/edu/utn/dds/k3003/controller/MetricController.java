package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.service.DDMetricsUtils;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class MetricController {
  private final DDMetricsUtils metricsUtils;

  public MetricController(DDMetricsUtils metricsUtils) {
    this.metricsUtils = metricsUtils;
  }
  public void aperturaHeladera(Context context) {
    try {
      String heladeraIdParam = context.pathParam("heladeraId");
      metricsUtils.getRegistry().counter("heladera"+ heladeraIdParam + ".aperturas").increment();
      context.status(HttpStatus.OK);
      log.info("Apertura de heladera registrada.");
    } catch (Exception e) {
      context.status(HttpStatus.INTERNAL_SERVER_ERROR);
      log.error("Error al registrar la apertura de la heladera", e);
    }
  }

  public void trasladosRealizados(Context context) {
    try {
      metricsUtils.getRegistry().counter("traslado.finalizado").increment();
      context.status(HttpStatus.OK);
      log.info("Nuevo traslado finalizado");
    } catch (Exception e) {
      context.status(HttpStatus.INTERNAL_SERVER_ERROR);
      log.error("Error al registrar traslado finalizado", e);
    }
  }

  public void trasladosEnCurso(Context context) {
    try {
      String accion = context.queryParam("accion");

      if ("incrementar".equals(accion)) {
        metricsUtils.getRegistry().gauge("trasladosEnCurso", new AtomicInteger(0)).incrementAndGet();
        log.info("Traslados en curso incrementados.");
      } else if ("disminuir".equals(accion)) {
        metricsUtils.getRegistry().gauge("trasladosEnCurso", new AtomicInteger(0)).decrementAndGet();
        log.info("Traslados en curso disminuidos.");
      } else {
        throw new IllegalArgumentException("Acción no válida. Debe ser 'incrementar' o 'disminuir'.");
      }
      context.status(HttpStatus.OK);
    } catch (Exception e) {
      context.status(HttpStatus.INTERNAL_SERVER_ERROR);
      log.error("Error al actualizar los traslados en curso", e);
    }
  }
}
