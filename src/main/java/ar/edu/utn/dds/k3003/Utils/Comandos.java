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
                sendMessage(chatId, "¡Hola! Soy tu bot. ¿En qué puedo ayudarte?");
                break;
            case "/help":
                sendMessage(chatId, "Lista de comandos:\n" +
                        "/start - Inicia el bot\n" +
                        "/menu - Muestra el menú\n" );
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
        String menuText = "Elige una opción:\n" +
                "/verDatos - Ver mis datos\n" +
                "/darDeAltaRuta - Crear ruta\n";

        //aca poner todos los comandos

        sendMessage(chatId, menuText);
    }

    public void onMessageReceived1(Long chatId, String message) {
        sendMessage(chatId, "entro a onMessageRecieved1");
        String estado = esperandoUsuarios.getOrDefault(chatId, "");

        switch (estado) {
            case "darDeAltaRuta":
                botLogistica.darDeAltaRuta(chatId, message, this);
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

