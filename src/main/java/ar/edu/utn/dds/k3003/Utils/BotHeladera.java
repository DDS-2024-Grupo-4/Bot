package ar.edu.utn.dds.k3003.Utils;

import ar.edu.utn.dds.k3003.model.RetiroDTODay;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class BotHeladera {

  Dotenv dotenv = Dotenv.load();
  String url = dotenv.get("URL_HELADERA");

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

  public void verIncidentesDeHeladera(Long chatId, String mensaje, Comandos comandos){
    String heladeraId = String.format("{\"heladeraId\": \"%s\"}", mensaje);
    try {
      String uri = url + "/heladera/" + heladeraId + "/obtenerHistorialIncidentes";

      // Crear la solicitud GET
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(uri))
          .header("Content-Type", "application/json")
          .GET()
          .build();

      HttpClient client = HttpClient.newHttpClient();
      // Enviar la solicitud y obtener la respuesta
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 200) {
        System.out.println("Historial de incidentes: " + response.body());
        comandos.sendMessage(chatId, "Historial de incidentes: " + response.body());
      } else {
        System.out.println("Error al obtener el historial de incidentes: " + response.statusCode() + " - " + response.body());
        comandos.sendMessage(chatId, "Error al obtener el historial de incidentes");
      }
    } catch (Exception e) {
      e.printStackTrace();
      comandos.sendMessage(chatId, "Ocurrió un error al obtener el historial de incidentes");
      System.out.println("Ocurrió un error al obtener el historial de incidentes: " + e.getMessage());
    }
  }

  public void verOcupacion(Long chatId, String mensaje, Comandos comandos){
    String heladeraId = String.format("{\"heladeraId\": \"%s\"}", mensaje);
    try {
      String uri = url + "/heladera/" + heladeraId + "/cantidadViandasHastaLLenar";

      // Crear la solicitud GET
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(uri))
          .header("Content-Type", "application/json")
          .GET()
          .build();

      HttpClient client = HttpClient.newHttpClient();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 200) {

        List<Integer> viandasInfo = parseViandasInfo(response.body());

        if (viandasInfo != null && viandasInfo.size() == 2) {
          Integer cantidadHastaLlenar = viandasInfo.get(0);
          Integer cantidadMaxima = viandasInfo.get(1);

          // Calcular el porcentaje de ocupación
          double porcentajeLlenado = (double) (cantidadMaxima - cantidadHastaLlenar) / cantidadMaxima * 100;

          String mssge = String.format(
              "La heladera está al %.2f%% de su capacidad.\n" +
                  "Viandas disponibles hasta llenar: %d\n" +
                  "Capacidad máxima de viandas: %d",
              porcentajeLlenado, cantidadHastaLlenar, cantidadMaxima);

          System.out.println(mssge);
          comandos.sendMessage(chatId, mssge);

        } else {
          System.out.println("Error: La respuesta no tiene los datos esperados.");
          comandos.sendMessage(chatId, "Error al obtener la ocupación de viandas.");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      comandos.sendMessage(chatId, "Ocurrió un error al obtener la ocupación de viandas.");
      System.out.println("Ocurrió un error al obtener la ocupación de viandas: " + e.getMessage());
    }
  }

  public void verRetirosDelDia(Long chatId, String mensaje, Comandos comandos){
    String heladeraId = String.format("{\"heladeraId\": \"%s\"}", mensaje);
    try {
      String uri = url + "/heladera/" + heladeraId + "/obtenerRetirosDelDia";

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(uri))
          .header("Content-Type", "application/json")
          .GET()
          .build();

      HttpClient client = HttpClient.newHttpClient();
      // Espera un ida y vuelta
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 200) {
        // Parsear la respuesta, asumiendo que la respuesta es una lista de objetos RetiroDTODay en formato JSON
        List<RetiroDTODay> retirosDelDia = parseRetirosDelDia(response.body());

        // Si la lista está vacía, se notifica
        if (retirosDelDia == null || retirosDelDia.isEmpty()) {
          String mssge = "No se han registrado retiros para esta heladera.";
          comandos.sendMessage(chatId, mssge);
          System.out.println(mssge);
          return;
        }

        StringBuilder mssge = new StringBuilder("Retiros del día para la heladera " + heladeraId + ":\n");
        for (RetiroDTODay retiro : retirosDelDia) {
          mssge.append(String.format("QR Vianda: %s, Tarjeta: %s, Fecha de Retiro: %s\n",
              retiro.getQrVianda(), retiro.getTarjeta(), retiro.getFechaRetiro()));
        }

        comandos.sendMessage(chatId, mssge.toString());
        System.out.println(mssge.toString());

      } else {
        String errorMessage = "Error al obtener los retiros del día. Código de estado: " + response.statusCode();
        comandos.sendMessage(chatId, errorMessage);
        System.out.println(errorMessage);
      }

    } catch (Exception e) {
      e.printStackTrace();
      comandos.sendMessage(chatId, "Ocurrió un error al obtener los retiros del día.");
      System.out.println("Ocurrió un error al obtener los retiros del día: " + e.getMessage());
    }
  }

  private List<RetiroDTODay> parseRetirosDelDia(String jsonResponse) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      // Convertimos la respuesta en una lista de retiroDTODay
      return objectMapper.readValue(jsonResponse, new TypeReference<List<RetiroDTODay>>(){});
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static List<Integer> parseViandasInfo(String responseBody) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      // Convertimos la respuesta en una lista de enteros
      return objectMapper.readValue(responseBody, List.class);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}

