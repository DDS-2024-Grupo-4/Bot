package ar.edu.utn.dds.k3003.Utils;
import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

public class Comandos extends TelegramLongPollingBot {
    private BotLogistica botLogistica;
    private Map<Long, String> esperandoUsuarios = new HashMap<>();

    public Comandos() {
        this.botLogistica = new BotLogistica();
    }

    public void handleCommand(Long chatId, String command) {
        switch (command) {
            case "/start":
                sendMessage(chatId, "¡Hola! Soy tu bot. usa el comando /menu para ver las funcionalidades");
                break;
            case "/menu":
                showMenu(chatId);
                break;
            case "/darDeAltaRuta":
                esperandoUsuarios.put(chatId, "darDeAltaRuta");
                sendMessage(chatId, "Por favor, envía los datos en el siguiente formato:\n" +
                        "`colaboradorId heladeraOrigenId heladeraDestinoId`\n" +
                        "Ejemplo: `5 1 2`");
                break;
            case "/asignarTraslado":
                esperandoUsuarios.put(chatId, "asignarTraslado");
                sendMessage(chatId, "Por favor, envía los datos en el siguiente formato:\n" +
                        "`listQrViandas status fechaTraslado heladeraOrigen heladeraDestino`\n" +
                        "Ejemplo: `[asd] CREADO 2024-05-15T21:10:40Z 1 2`");
                break;
            case "/iniciarTraslado":
                esperandoUsuarios.put(chatId, "iniciarFinalizarTraslado");
                sendMessage(chatId, "Por favor, envía los datos en el siguiente formato:\n" +
                        "`status idTraslado`\n" +
                        "Ejemplo: `EN_VIAJE 1`");
                break;
            case "/finalizarTraslado":
                esperandoUsuarios.put(chatId, "iniciarFinalizarTraslado");
                sendMessage(chatId, "Por favor, envía los datos en el siguiente formato:\n" +
                        "`status`\n" +
                        "Ejemplo: `ENTREGADO 1`");
                break;

            case "/agregarColaborador":
                esperandoUsuarios.put(chatId, "agregarColaborador");
                sendMessage(chatId, "Por favor, envía los datos en el siguiente formato:\n" +
                        "`nombre FormaDeColaborar`\n" +
                        "Ejemplo: `pepito “DONADOR”`");
                break;

            case  "/modificarFormaDeColaborar":
                esperandoUsuarios.put(chatId, "modificarFormaDeColaborar"); // FUNCIONA???
                sendMessage(chatId, "Por favor, envía los datos en el siguiente formato (puede elegir mas de una):\n" +
                        "`idcolaborador formaDeColaborar`\n" +
                        "Ejemplo: ` 1 “DONADOR“ “TRANSPORTADOR“ “TECNICO“`");
                break;
            case  "/reportarHeladeraRota":
                esperandoUsuarios.put(chatId, "reportarHeladeraRota");
                sendMessage(chatId, "Por favor, envía los datos en el siguiente formato :\n" +
                        "`heladeraID`\n" +
                        "Ejemplo: `1`");
                break;
            case   "/repararHeladera":
                esperandoUsuarios.put(chatId, "repararHeladera");
                sendMessage(chatId, "Por favor, envía los datos en el siguiente formato:\n" +
                        "`colaboradorID heladeraID`\n" +
                        "Ejemplo: `1 4`");
                break;
            case   "/verMisPuntos":
                esperandoUsuarios.put(chatId, "verMisPuntos");
                sendMessage(chatId, "Por favor, envía los datos en el siguiente formato:\n" +
                        "`colaboradorID`\n" +
                        "Ejemplo: `1`");
                break;
            default:
                sendMessage(chatId, "Comando no reconocido.");
        }
    }

    public void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMenu(Long chatId) {
        String menuText = "Elige una opción:\n\n" +
                "LOGISTICA:\n" +
                "/darDeAltaRuta - Crear ruta\n" +
                "/asignarTraslado - asigna un traslado a un colaborador\n" +
                "/iniciarTraslado - inicia un traslado de una vianda\n" +
                "/finalizarTraslado - finaliza el traslado de una vianda\n\n" +
                "COLABORADORES:\n" +
                "/agregarColaborador\n" +
                "/modificarFormaDeColaborar\n" +
                "/reportarHeladeraRota\n" +
                "/repararHeladera - reporta que la heladera ha sido reparada\n" +
                "/verMisPuntos\n" +     //Si me da tiempo agregar forma de ver reparaciones, donaciones de dinero, etc
                "VIANDAS:\n\n" +
                "HELADERAS:\n\n" +
                "/verDatos - Ver mis datos\n";

        //aca poner todos los comandos

        sendMessage(chatId, menuText);
    }

    public void onMessageReceived1(Long chatId, String message) {
        String estado = esperandoUsuarios.getOrDefault(chatId, "");

        switch (estado) {
            case "darDeAltaRuta":
                botLogistica.darDeAltaRuta(chatId, message, this);
                break;
            case "asignarTraslado":
               botLogistica.asignarTraslado(chatId, message, this);
                break;
            case "iniciarFinalizarTraslado":
               botLogistica.iniciarFinalizarTraslado(chatId, message, this);
                break;

            case "agregarColaborador":
                botLogistica.agregarColaborador(chatId, message, this);
                break;
            case "modificarFormaDeColaborar":
                botLogistica.modificarFormaDeColaborar(chatId, message, this);
                break;
            case "reportarHeladeraRota":
                botLogistica.reportarHeladera(chatId, message, this);
                break;
            case "repararHeladera":
                botLogistica.repararHeladera(chatId, message, this);
                break;
            case "verMisPuntos":
                botLogistica.Puntos(chatId, message, this);
                break;

        }
    }

    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return Dotenv.load().get("Nombre_Bot");
    }

    @Override
    public String getBotToken() {
        return Dotenv.load().get("BOT_TOKEN");
    }
}

