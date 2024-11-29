package ar.edu.utn.dds.k3003.Utils;

import ar.edu.utn.dds.k3003.app.BotApp;
import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.List;

public class BotLogistica {

    Dotenv dotenv = Dotenv.load();
    String urlLogistica = dotenv.get("URL_LOGISTICA");
    String urlColaboradores = dotenv.get("URL_COLABORADORES");
    String urlViandas = dotenv.get("URL_VIANDA");
    String urlHeladera = dotenv.get("URL_HELADERA");

    public void darDeAltaRuta(Long chatId, String mensaje, Comandos comandos) {

        String[] partes = mensaje.split("\\s+");

        int colaboradorId = Integer.parseInt(partes[0]);
        int heladeraIdOrigen = Integer.parseInt(partes[1]);
        int heladeraIdDestino = Integer.parseInt(partes[2]);

        try {
            String requestBody = String.format(
                    "{\"colaboradorId\": %d, \"heladeraIdOrigen\": %d, \"heladeraIdDestino\": %d}",
                    colaboradorId, heladeraIdOrigen, heladeraIdDestino
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlLogistica + "/rutas"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                comandos.sendMessage(chatId, "Ruta creada exitosamente");
                System.out.println("Ruta creada exitosamente: " + response.body());
            } else {
                comandos.sendMessage(chatId,"Error al crear la ruta");
                System.out.println("Error al crear la ruta: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            comandos.sendMessage(chatId,"Ocurrió un error al crear la ruta");
            System.out.println("Ocurrió un error al crear la ruta: " + e.getMessage());
        }
    }


    public void asignarTraslado(Long chatId, String mensaje, Comandos comandos){
        String[] partes = mensaje.split("\\s+");

        String listQrViandas = partes[0];
        String status = partes[1];
        String fechaTraslado = partes[2];
        int heladeraOrigen = Integer.parseInt(partes[3]);
        int heladeraDestino = Integer.parseInt(partes[4]);

        try {
            String requestBody = String.format(
                    "{\"listQrViandas\": [\"%s\"], \"status\": \"%s\", \"fechaTraslado\": \"%s\", \"heladeraOrigen\": %d, \"heladeraDestino\": %d}",
                    listQrViandas, status, fechaTraslado, heladeraOrigen, heladeraDestino
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlLogistica + "/traslados"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                comandos.sendMessage(chatId, "Traslado asignado exitosamente");
                System.out.println("Traslado asignado exitosamente: " + response.body());
            } else {
                comandos.sendMessage(chatId,"Error al aignar el traslado");
                System.out.println("Error al aignar el traslado: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            comandos.sendMessage(chatId,"Ocurrió un error al asignar el traslado");
            System.out.println("Ocurrió un error al asignar el traslado: " + e.getMessage());
        }
    }

    public void iniciarFinalizarTraslado(Long chatId, String mensaje, Comandos comandos){
        String[] partes = mensaje.split("\\s+");

        System.out.println("entro a iniciar finalizar traslado");

        String status = partes[0];
        int idTraslado = Integer.parseInt(partes[1]);

        try {
            String requestBody = String.format(
                    "{\"status\": \"%s\"}",
                    status
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlLogistica + "/traslados/" + idTraslado))
                    .header("Content-Type", "application/json")
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(requestBody)) // Método PATCH
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                comandos.sendMessage(chatId, "Traslado iniciado exitosamente");
                System.out.println("Traslado iniciado exitosamente: " + response.body());
            } else {
                comandos.sendMessage(chatId,"Error al aignar el trasladoa");
                System.out.println("Error al iniciar el traslado: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            comandos.sendMessage(chatId,"Ocurrió un error al asignar el traslado");
            System.out.println("Ocurrió un error al iniciar el traslado: " + e.getMessage());
        }
    }


    public void agregarColaborador(Long chatId, String mensaje, Comandos comandos) {
        String[] partes = mensaje.split("\\s+");

        String nombre  = partes[0];
        String formas = partes[1];
        String forma2="";
        String forma3="";
        try {
            formas = formas.concat(",").concat(partes[2]);
            formas = formas.concat(",").concat(partes[3]);
        }
        catch (ArrayIndexOutOfBoundsException e) {
        }

        try {
            String requestBody = String.format(
                    "{\"nombre\": %s, \"formas\": [%s]}",
                    nombre,formas
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlColaboradores + "/colaboradores"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                comandos.sendMessage(chatId, "Colaborador creado exitosamente");
                System.out.println("Colaborador creado exitosamente: " + response.body());
            } else {
                comandos.sendMessage(chatId,"Error al crear colaborador");
                System.out.println("Error al crear colaborador: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            comandos.sendMessage(chatId,"Ocurrió un error al crear el colaborador");
            System.out.println("Ocurrió un error al crear el colaborador: " + e.getMessage());
        }
    }

    public void modificarFormaDeColaborar(Long chatId, String mensaje, Comandos comandos) {
        String[] partes = mensaje.split("\\s+");

        int id = Integer.parseInt(partes[0]);
        String formas = partes[1];
        String forma2="";
        String forma3="";
        try {
            formas = formas.concat(",").concat(partes[2]);
            formas = formas.concat(",").concat(partes[3]);
        }
        catch (ArrayIndexOutOfBoundsException e) {
        }

        try {
            String requestBody = String.format(
                    "{\"formas\": [%s]}",
                    formas
            );
            String url = String.format("/colaboradores/%d",
                    id
            );
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlColaboradores + url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                comandos.sendMessage(chatId, "Forma cambiada exitosamente");
                System.out.println("Forma cambiada exitosamente: " + response.body());
            } else {
                comandos.sendMessage(chatId,"Error al cambiar forma");
                System.out.println("Error al cambiar forma: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            comandos.sendMessage(chatId,"Ocurrió un error al cambiar forma");
            System.out.println("Ocurrió un error al cambiar forma: " + e.getMessage());
        }
    }

    public void reportarHeladera(Long chatId, String mensaje, Comandos comandos) {
        String[] partes = mensaje.split("\\s+");

        int id = Integer.parseInt(partes[0]);
        try {
            String requestBody = "";
            String url = String.format("/colaboradores/%d/reportar",
                    id
            );
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlColaboradores + url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                comandos.sendMessage(chatId, "Forma cambiada exitosamente");
                System.out.println("Forma cambiada exitosamente: " + response.body());
            } else {
                comandos.sendMessage(chatId, "Error al cambiar forma");
                System.out.println("Error al cambiar forma: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            comandos.sendMessage(chatId, "Ocurrió un error al cambiar forma");
            System.out.println("Ocurrió un error al cambiar forma: " + e.getMessage());
        }
    }

    public void repararHeladera(Long chatId, String mensaje, Comandos comandos) {
        String[] partes = mensaje.split("\\s+");

        int id = Integer.parseInt(partes[0]);
        int heladeraid = Integer.parseInt(partes[0]);
        try {
            String requestBody = "";
            String url = String.format("/colaboradores/%d/reparar/%d",
                    id,heladeraid
            );
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlColaboradores + url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                comandos.sendMessage(chatId, "Heladera reparada exitosamente");
                System.out.println("Heladera reparada exitosamente: " + response.body());
            } else {
                comandos.sendMessage(chatId, "Error al reparar heladera");
                System.out.println("Error al reparar heladera: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            comandos.sendMessage(chatId, "Ocurrió un error al reparar heladera");
            System.out.println("Ocurrió un error al reparar heladera: " + e.getMessage());
        }
    }

    public void Puntos(Long chatId, String mensaje, Comandos comandos) {
    	String[] partes = mensaje.split("\s+");

        int id = Integer.parseInt(partes[0]);
        try {
            String requestBody = "";
            String url = String.format("/colaboradores/%d/puntos",
                    id
            );
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlColaboradores + url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                comandos.sendMessage(chatId, "puntos recuperados exitosamente");
                System.out.println("Tenés esta cantidad de puntos: " + response.body());
            } else {
                comandos.sendMessage(chatId, "Error al buscar puntos");
                System.out.println("Error al buscar puntos: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            comandos.sendMessage(chatId, "Ocurrió un error al buscar puntos");
            System.out.println("Ocurrió un error al buscar puntos: " + e.getMessage());
        }

    }
    
    public void crearYDepositarVianda(Long chatId, String mensaje, Comandos comandos) {
    	String[] partes = mensaje.split("\\s+");
    	
    	String codigoQR = partes[0];
    	String fechaElaboracion = partes[1];
    	int idColaborador = Integer.parseInt(partes[2]);
        int heladeraId = Integer.parseInt(partes[3]);
        
        try {
            String requestBody = String.format(
                    "{\"codigoQR\": \"%s\", \"fechaElaboracion\": \"%s\", \"idColaborador\": %d, \"heladeraId\": %d}",
                    codigoQR, fechaElaboracion, idColaborador, heladeraId
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlViandas + "/viandasDepositar"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                comandos.sendMessage(chatId, "Vianda creada y depositada exitosamente");
                System.out.println("Vianda creada y depositada exitosamente: " + response.body());
            } else {
                comandos.sendMessage(chatId,"Error al crear o depositar la ruta");
                System.out.println("Error al crear o depositar la ruta: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            comandos.sendMessage(chatId,"Ocurrió un error al crear y depositar la vianda");
            System.out.println("Ocurrió un error al crear y depositar la vianda: " + e.getMessage());
        }
    }
    
    public void retirarVianda(Long chatId, String mensaje, Comandos comandos) {
    	String[] partes = mensaje.split("\\s+");
    	
    	String codigoQR = partes[0];
    	int heladeraId = Integer.parseInt(partes[1]);
        
        try {
            String requestBody = String.format(
                    "{\"codigoQR\": \"%s\", \"heladeraId\": %d}",
                    codigoQR, heladeraId
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlHeladera + "/retiros"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            switch(response.statusCode()) {
            case 404:
            	comandos.sendMessage(chatId, "No se encontro la heladera");
                System.out.println("No se encontro la heladera: " + response.statusCode() + " - " + response.body());
            	break;
            case 403:
            	comandos.sendMessage(chatId, "La heladera no esta habilitada");
                System.out.println("La heladera no esta habilitada: " + response.statusCode() + " - " + response.body());
            	break;
            case 201:
            	comandos.sendMessage(chatId, "Se retiro la vianda exitosamente");
                System.out.println("Se retiro la vianda exitosamente: " + response.body());
                break;
            default:
            	comandos.sendMessage(chatId, "Hubo un error en la solicitud");
                System.out.println("Hubo un error en la solicitud: " + response.statusCode() + " - " + response.body());
            	break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            comandos.sendMessage(chatId,"Ocurrió un error al retirar la vianda");
            System.out.println("Ocurrió un error al retirar la vianda: " + e.getMessage());
        }
    }
    
    public void obtenerHistorialIncidentes(Long chatId, String mensaje, Comandos comandos) {
    	String[] partes = mensaje.split("\\s+");
    	
    	int heladeraId = Integer.parseInt(partes[0]);
        
        try {
            String url = String.format("/heladera/%d/obtenerHistorialIncidentes",
            		heladeraId
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlHeladera + url))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            switch(response.statusCode()) {
            case 404:
            	comandos.sendMessage(chatId, "La heladera no tiene un historial");
                System.out.println("La heladera no tiene un historial: " + response.statusCode() + " - " + response.body());
            	break;
            case 400:
            	comandos.sendMessage(chatId, "La heladera no fue encontrada");
                System.out.println("La heladera no fue encontrada: " + response.statusCode() + " - " + response.body());
            	break;
            case 201:
            	comandos.sendMessage(chatId, "Se encontro el historial de la heladera");
                System.out.println("Se encontro el historial de la heladera: " + response.body());
                break;
            default:
            	comandos.sendMessage(chatId, "Hubo un error en la solicitud");
                System.out.println("Hubo un error en la solicitud: " + response.statusCode() + " - " + response.body());
            	break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            comandos.sendMessage(chatId,"Ocurrió un error al intentar de conseguir el historial de la heladera");
            System.out.println("Ocurrió un error al intentar de conseguir el historial de la heladera: " + e.getMessage());
        }
    }
    
    public void viandasEnHeladera(Long chatId, String mensaje, Comandos comandos) {
    	String[] partes = mensaje.split("\\s+");
    	
    	int heladeraId = Integer.parseInt(partes[0]);
        
        try {
            String url = String.format("/heladeras/%d/viandasEnHeladera",
            		heladeraId
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlHeladera + url))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            switch(response.statusCode()) {
            case 404:
            	comandos.sendMessage(chatId, "La heladera no tiene viandas");
                System.out.println("La heladera no tiene viandas: " + response.statusCode() + " - " + response.body());
            	break;
            case 400:
            	comandos.sendMessage(chatId, "La heladera no fue encontrada");
                System.out.println("La heladera no fue encontrada: " + response.statusCode() + " - " + response.body());
            	break;
            case 201:
            	comandos.sendMessage(chatId, "Se encontraron las viandas en la heladera");
                System.out.println("Se encontraron las viandas en la heladera: " + response.body());
                break;
            default:
            	comandos.sendMessage(chatId, "Hubo un error en la solicitud");
                System.out.println("Hubo un error en la solicitud: " + response.statusCode() + " - " + response.body());
            	break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            comandos.sendMessage(chatId,"Ocurrió un error al intentar de conseguir las viandas de la heladera");
            System.out.println("Ocurrió un error al intentar de conseguir las viandas de la heladera: " + e.getMessage());
        }
    }
    
    public void obtenerRetirosDelDia(Long chatId, String mensaje, Comandos comandos) {
    	String[] partes = mensaje.split("\\s+");
    	
    	int heladeraId = Integer.parseInt(partes[0]);
        
        try {
            String url = String.format("/heladera/%d/obtenerRetirosDelDia",
            		heladeraId
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlHeladera + url))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            switch(response.statusCode()) {
            case 404:
            	comandos.sendMessage(chatId, "No hubo retiros en la heladera el dia de hoy");
                System.out.println("No hubo retiros en la heladera el dia de hoy: " + response.statusCode() + " - " + response.body());
            	break;
            case 400:
            	comandos.sendMessage(chatId, "La heladera no fue encontrada");
                System.out.println("La heladera no fue encontrada: " + response.statusCode() + " - " + response.body());
            	break;
            case 201:
            	comandos.sendMessage(chatId, "Se obtuvieron los retiros del dia");
                System.out.println("Se obtuvieron los retiros del dia: " + response.body());
                break;
            default:
            	comandos.sendMessage(chatId, "Hubo un error en la solicitud");
                System.out.println("Hubo un error en la solicitud: " + response.statusCode() + " - " + response.body());
            	break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            comandos.sendMessage(chatId,"Ocurrió un error al intentar de conseguir los retiros del dia");
            System.out.println("Ocurrió un error al intentar de conseguir los retiros del dia: " + e.getMessage());
        }
    }
    
    public void eliminarSuscripcion(Long chatId, String mensaje, Comandos comandos) {
    	String[] partes = mensaje.split("\\s+");
    	
    	String tipoDeSuscripcion = partes[0];
    	int heladeraId = Integer.parseInt(partes[1]);
    	int colaboradorId = Integer.parseInt(partes[2]);
        
        try {
            String url = String.format("/%d/suscripciones",
            		heladeraId
            );
            String requestBody = String.format(
                    "{\"codigoQR\": \"%s\", \"heladeraId\": %d, \"colaboradorId\": %d}",
                    tipoDeSuscripcion, heladeraId, colaboradorId
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlHeladera + url))
                    .header("Content-Type", "application/json")
                    .method("DELETE", HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            switch(response.statusCode()) {
            case 404:
            	comandos.sendMessage(chatId, "No se encontro la heladera");
                System.out.println("No se encontro la heladera: " + response.statusCode() + " - " + response.body());
            	break;
            case 400:
            	comandos.sendMessage(chatId, "El tipo de suscripcion es invalido o falta tipo de suscripcion, id de heladera o id del colaborador");
                System.out.println("El tipo de suscripcion es invalido o falta tipo de suscripcion, id de heladera o id del colaborador: " + response.statusCode() + " - " + response.body());
            	break;
            case 201:
            	comandos.sendMessage(chatId, "Se elimino excitosamente la suscripcion");
                System.out.println("Se elimino excitosamente la suscripcion: " + response.body());
                break;
            default:
            	comandos.sendMessage(chatId, "Hubo un error en la solicitud");
                System.out.println("Hubo un error en la solicitud: " + response.statusCode() + " - " + response.body());
            	break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            comandos.sendMessage(chatId,"Ocurrió un error al intentar de desuscribirse");
            System.out.println("Ocurrió un error al intentar de desuscribirse: " + e.getMessage());
        }
    }
    
    public void suscribirse(Long chatId, String mensaje, Comandos comandos) {
    	String[] partes = mensaje.split("\\s+");

    	int colaboradorId = Integer.parseInt(partes[0]);
    	int heladeraId = Integer.parseInt(partes[1]);
    	String tipoSuscripcion = partes[2];
    	int cantidadFaltante = Integer.parseInt(partes[3]);
    	int cantidadDisponible = Integer.parseInt(partes[4]);
        
        try {
            String requestBody = String.format(
                    "{\"colaboradorId\": %d, \"heladeraId\": %d, \"tipoSuscripcion\": \"%s\", \"cantidadFaltante\": %d, \"cantidadDisponible\": %d}",
                    colaboradorId, heladeraId, tipoSuscripcion, cantidadFaltante, cantidadDisponible
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlHeladera + "/suscribirse"))
                    .header("Content-Type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            switch(response.statusCode()) {
            case 404:
            	comandos.sendMessage(chatId, "No se encontro la heladera");
                System.out.println("No se encontro la heladera: " + response.statusCode() + " - " + response.body());
            	break;
            case 201:
            	comandos.sendMessage(chatId, "Se logro la suscripcion excitosamente");
                System.out.println("Se logro la suscripcion excitosamente: " + response.body());
                break;
            default:
            	comandos.sendMessage(chatId, "Hubo un error en la solicitud");
                System.out.println("Hubo un error en la solicitud: " + response.statusCode() + " - " + response.body());
            	break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            comandos.sendMessage(chatId,"Ocurrió un error al intentar de suscribirse");
            System.out.println("Ocurrió un error al intentar de suscribirse: " + e.getMessage());
        }
    }
}


