package ar.edu.utn.dds.k3003.Utils;

import ar.edu.utn.dds.k3003.app.BotApp;
import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BotLogistica {

    Dotenv dotenv = Dotenv.load();
    String url = dotenv.get("URL_LOGISTICA");

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

            // Crear el cliente HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Enviar la solicitud y obtener la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Manejar la respuesta
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


    public void asignarTraslado(){

    }

    public void iniciarTraslado(){
        //aca le mando a modificarEstado EN_VIAJE
    }

    public void finalizarTraslado(){
        //aca le mando a modificarEstado ENTREGADO
    }



}


