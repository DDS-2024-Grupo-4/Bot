package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.service.DDMetricsUtils;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.micrometer.core.instrument.Gauge;
import lombok.extern.slf4j.Slf4j;

import java.util.AbstractMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class MetricController {
  private final DDMetricsUtils metricsUtils;
  private final AtomicInteger trasladosEnCurso = new AtomicInteger(0);
  private final AtomicInteger cantColaboradores = new AtomicInteger(0);
  private final AtomicInteger cantDonadores = new AtomicInteger(0);
  private final AtomicInteger cantTransportadores = new AtomicInteger(0);
  private final AtomicInteger viandasCreadas = new AtomicInteger(0);
  private final AtomicInteger viandasDepositadas = new AtomicInteger(0);
  private final AtomicInteger viandasVencidas = new AtomicInteger(0);

  public MetricController(DDMetricsUtils metricsUtils) {
    this.metricsUtils = metricsUtils;
  }
  public void aperturaHeladera(Context context) {
    try {
      String heladeraIdParam = context.pathParam("heladeraId");
      metricsUtils.getRegistry().counter("heladerasAperturas").increment();
      context.status(HttpStatus.OK);
      log.info("Apertura de heladera registrada.");
    } catch (Exception e) {
      context.status(HttpStatus.INTERNAL_SERVER_ERROR);
      log.error("Error al registrar la apertura de la heladera", e);
    }
  }
  public void excesoTemperaturaHeladera(Context context){
    try{
    String heladeraId = context.pathParam("heladeraId");
    metricsUtils.getRegistry().counter("excesoTemperaturaHeladera"+heladeraId).increment();
    context.status(HttpStatus.OK);
    log.info("Exceso de temperatura registrada.");
    } catch (Exception e) {
    context.status(HttpStatus.INTERNAL_SERVER_ERROR);
    log.error("Error al registrar un exceso de temperatura en la heladera", e);
    }
  }
  public void consultaHeladera(Context context) {
    try {
      metricsUtils.getRegistry().counter("consultaHeladeras").increment();
      context.status(HttpStatus.OK);
      log.info("Consulta de heladera registrada.");
    } catch (Exception e) {
      context.status(HttpStatus.INTERNAL_SERVER_ERROR);
      log.error("Error al registrar la consulta de la heladera", e);
    }
  }
  public void trasladosRealizados(Context context) {
    try {
      metricsUtils.getRegistry().counter("trasladoFinalizado").increment();
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
        metricsUtils.getRegistry().gauge("trasladosEnCurso", trasladosEnCurso.incrementAndGet());
        log.info("Traslados en curso incrementados.");
      } else if ("disminuir".equals(accion)) {
        metricsUtils.getRegistry().gauge("trasladosEnCurso", trasladosEnCurso.decrementAndGet());
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

  public void CantColaboradores(Context context) {
      try {
          String accion = context.pathParamAsClass("accion", String.class).get();

          if ("incrementar".equals(accion)) {
              metricsUtils.getRegistry().gauge("cantColaboradores", cantColaboradores.incrementAndGet());
              log.info("cantColaboradores incrementados.");
          } else if ("disminuir".equals(accion)) {
              metricsUtils.getRegistry().gauge("cantColaboradores", cantColaboradores.decrementAndGet());
              log.info("cantColaboradores disminuidos.");
          } else {
              throw new IllegalArgumentException("Acción no válida. Debe ser 'incrementar' o 'disminuir'.");
          }
          context.status(HttpStatus.OK);
          log.info("Nuevo colaborador agregado");
      } catch (Exception e) {
          context.status(HttpStatus.INTERNAL_SERVER_ERROR);
          log.error("Error al agregar colaborador", e);
      }
  }

  public void cantDonadores(Context context) {
    try {
        String accion = context.pathParamAsClass("accion", String.class).get();

      if ("incrementar".equals(accion)) {
        metricsUtils.getRegistry().gauge("cantDonadores", cantDonadores.incrementAndGet());
        log.info("cantDonadores incrementados.");
      } else if ("disminuir".equals(accion)) {
        metricsUtils.getRegistry().gauge("cantDonadores", cantDonadores.decrementAndGet());
        log.info("cantDonadores disminuidos.");
      } else {
        throw new IllegalArgumentException("Acción no válida. Debe ser 'incrementar' o 'disminuir'.");
      }
      context.status(HttpStatus.OK);
    } catch (Exception e) {
      context.status(HttpStatus.INTERNAL_SERVER_ERROR);
      log.error("Error al actualizar la cantidad de donadores", e);
    }
  }

  public void cantTransportadores(Context context) {
    try {
        String accion = context.pathParamAsClass("accion", String.class).get();

        if ("incrementar".equals(accion)) {
        metricsUtils.getRegistry().gauge("cantTransportadores", cantTransportadores.incrementAndGet());
        log.info("cantidad de transportadores incrementados.");
      } else if ("disminuir".equals(accion)) {
        metricsUtils.getRegistry().gauge("cantTransportadores", cantTransportadores.decrementAndGet());
        log.info("cantidad de transportadores disminuidos.");
      } else {
        throw new IllegalArgumentException("Acción no válida. Debe ser 'incrementar' o 'disminuir'.");
      }
      context.status(HttpStatus.OK);
    } catch (Exception e) {
      context.status(HttpStatus.INTERNAL_SERVER_ERROR);
      log.error("Error al actualizar la cantidad de transportadores", e);
    }
  }
  
  public void viandasCreadas(Context context) {
	  try {
	      String accion = context.queryParam("accion");

	      if ("incrementar".equals(accion)) {
	        metricsUtils.getRegistry().gauge("viandasCreadas", new AtomicInteger(0)).incrementAndGet();
	        log.info("viandasCreadas aumento.");
	      } else {
	        throw new IllegalArgumentException("Acción no válida. Debe ser 'incrementar' o 'disminuir'.");
	      }
	      context.status(HttpStatus.OK);
	    } catch (Exception e) {
	      context.status(HttpStatus.INTERNAL_SERVER_ERROR);
	      log.error("Error al actualizar la cantidad de viandas creadas", e);
	    }
	  }
  public void viandasEnTransporte(Context context) {
	  try {
	      String accion = context.queryParam("accion");

	      if ("viandasEnTransporte".equals(accion)) {
	        metricsUtils.getRegistry().gauge("viandasEnTransporte", new AtomicInteger(0)).incrementAndGet();
	        log.info("viandasEnTransporte aumento.");
	      } else if ("disminuir".equals(accion)) {
	        metricsUtils.getRegistry().gauge("viandasEnTransporte", new AtomicInteger(0)).decrementAndGet();
	        log.info("viandasEnTransporte disminuyo.");
	      } else {
	        throw new IllegalArgumentException("Acción no válida. Debe ser 'incrementar' o 'disminuir'.");
	      }
	      context.status(HttpStatus.OK);
	    } catch (Exception e) {
	      context.status(HttpStatus.INTERNAL_SERVER_ERROR);
	      log.error("Error al actualizar la cantidad de viandas en transporte", e);
	    }
	  }
  public void viandasVencidas(Context context) {
	  try {
	      String accion = context.queryParam("accion");

	      if ("incrementar".equals(accion)) {
	        metricsUtils.getRegistry().gauge("viandasVencidas", new AtomicInteger(0)).incrementAndGet();
	        log.info("viandasVencidas aumento.");
	      } else {
	        throw new IllegalArgumentException("Acción no válida. Debe ser 'incrementar' o 'disminuir'.");
	      }
	      context.status(HttpStatus.OK);
	    } catch (Exception e) {
	      context.status(HttpStatus.INTERNAL_SERVER_ERROR);
	      log.error("Error al actualizar la cantidad de viandas vencidas", e);
	    }
	  }
}
