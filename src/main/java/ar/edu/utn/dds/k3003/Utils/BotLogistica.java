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
    String url = dotenv.get("URL_LOGISTICA");
    String urlColaboradores = dotenv.get("URL_COLABORADORES");

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
                    .uri(URI.create(url + "/rutas"))
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
                    .uri(URI.create(url + "/traslados"))
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
                    .uri(URI.create(url + "/traslados/" + idTraslado))
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
    }
}


