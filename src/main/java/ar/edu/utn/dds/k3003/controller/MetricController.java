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
  private AtomicInteger cantColaboradores = new AtomicInteger(0);
  private AtomicInteger cantDonadores = new AtomicInteger(0);
  private AtomicInteger cantTransportadores = new AtomicInteger(0);
  private final AtomicInteger viandasCreadas = new AtomicInteger(0);
  private final AtomicInteger viandasEnTransporte = new AtomicInteger(0);
  private final AtomicInteger viandasVencidas = new AtomicInteger(0);

  public MetricController(DDMetricsUtils metricsUtils) {
    this.metricsUtils = metricsUtils;
  }

    // Registro del gauge en la inicialización de tu aplicación o clase
    public static void inicializarMetricas() {
        metricsUtils.getRegistry().gauge("cantColaboradores", cantColaboradores, AtomicInteger::get);
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
          context.result("cantColaboradores incrementados");
      } else if ("disminuir".equals(accion)) {
        metricsUtils.getRegistry().gauge("trasladosEnCurso", trasladosEnCurso.decrementAndGet());
        log.info("Traslados en curso disminuidos.");
          context.result("cantColaboradores disminuidos");
      } else {
        throw new IllegalArgumentException("Acción no válida. Debe ser 'incrementar' o 'disminuir'.");
      }
      context.status(HttpStatus.OK);
    } catch (Exception e) {
      context.status(HttpStatus.INTERNAL_SERVER_ERROR);
      log.error("Error al actualizar los traslados en curso", e);
    }
  }

    // Método para manejar el incremento/disminución
    public void CantColaboradores(Context context) {
        try {
            String accion = context.pathParamAsClass("accion", String.class).get();

            if ("incrementar".equals(accion)) {
                cantColaboradores.incrementAndGet();  // Incrementamos el contador
                log.info("cantColaboradores incrementados.");
                context.result("cantColaboradores incrementados");
            } else if ("disminuir".equals(accion)) {
                cantColaboradores.decrementAndGet();  // Disminuimos el contador
                log.info("cantColaboradores disminuidos.");
                context.result("cantColaboradores disminuidos");
            } else {
                throw new IllegalArgumentException("Acción no válida. Debe ser 'incrementar' o 'disminuir'.");
            }

            context.status(HttpStatus.OK);
        } catch (Exception e) {
            context.status(HttpStatus.INTERNAL_SERVER_ERROR);
            log.error("Error al actualizar cantColaboradores", e);
        }
    }
/*
  public void CantColaboradores(Context context) {
      try {
          String accion = context.pathParamAsClass("accion", String.class).get();

          if ("incrementar".equals(accion)) {
              metricsUtils.getRegistry().gauge("cantColaboradores", cantColaboradores.incrementAndGet());
              log.info("cantColaboradores incrementados.");
              context.result("cantColaboradores incrementados");
          } else if ("disminuir".equals(accion)) {
              metricsUtils.getRegistry().gauge("cantColaboradores", cantColaboradores.decrementAndGet());
              log.info("cantColaboradores disminuidos.");
              context.result("cantColaboradores disminuidos");
          } else {
              throw new IllegalArgumentException("Acción no válida. Debe ser 'incrementar' o 'disminuir'.");
          }
          context.status(HttpStatus.OK);
      } catch (Exception e) {
          context.status(HttpStatus.INTERNAL_SERVER_ERROR);
          log.error("Error al agregar colaborador", e);
      }
  }*/

  public void cantDonadores(Context context) {
    try {
        String accion = context.pathParamAsClass("accion", String.class).get();

      if ("incrementar".equals(accion)) {
        metricsUtils.getRegistry().gauge("cantDonadores", cantDonadores.incrementAndGet());
        log.info("cantDonadores incrementados.");
          context.result("cantColaboradores incrementados");
      } else if ("disminuir".equals(accion)) {
        metricsUtils.getRegistry().gauge("cantDonadores", cantDonadores.decrementAndGet());
        log.info("cantDonadores disminuidos.");
          context.result("cantColaboradores disminuidos");
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
        context.result("cantidad de transportadores incrementados");
      } else if ("disminuir".equals(accion)) {
        metricsUtils.getRegistry().gauge("cantTransportadores", cantTransportadores.decrementAndGet());
        log.info("cantidad de transportadores disminuidos.");
            context.result("cantidad de transportadores disminuidos");
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
		  String accion = context.pathParamAsClass("accion", String.class).get();

	      if ("incrementar".equals(accion)) {
	          metricsUtils.getRegistry().counter("viandasCreadas").increment();
	          log.info("viandasCreadas aumento.");
	          context.result("cantidad de viandas creadas aumento");
	        } else {
	          throw new IllegalArgumentException("Acción no válida. Debe ser 'incrementar'.");
	        }
	      context.status(HttpStatus.OK);
	    } catch (Exception e) {
	      context.status(HttpStatus.INTERNAL_SERVER_ERROR);
	      log.error("Error al actualizar la cantidad de viandas creadas", e);
	    }
	  }
  public void viandasEnTransporte(Context context) {
	  try {
		  String accion = context.pathParamAsClass("accion", String.class).get();

		  if ("incrementar".equals(accion)) {
		        metricsUtils.getRegistry().gauge("viandasEnTransporte", viandasEnTransporte.incrementAndGet());
		        log.info("cantidad de viandasEnTransporte aumento.");
		        context.result("cantidad de viandasEnTransporte aumento");
		      } else if ("disminuir".equals(accion)) {
		        metricsUtils.getRegistry().gauge("viandasEnTransporte", viandasEnTransporte.incrementAndGet());
		        log.info("cantidad de viandasEnTransporte disminuio.");
		            context.result("cantidad de viandasEnTransporte disminuio");
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
		  String accion = context.pathParamAsClass("accion", String.class).get();

		  if ("incrementar".equals(accion)) {
	          metricsUtils.getRegistry().counter("viandasVencidas").increment();
	          log.info("viandasVencidas aumento.");
	          context.result("cantidad de viandas creadas aumento");
	        } else {
	          throw new IllegalArgumentException("Acción no válida. Debe ser 'incrementar'.");
	        }
	      context.status(HttpStatus.OK);
	    } catch (Exception e) {
	      context.status(HttpStatus.INTERNAL_SERVER_ERROR);
	      log.error("Error al actualizar la cantidad de viandas vencidas", e);
	    }
	  }
}
