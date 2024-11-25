package ar.edu.utn.dds.k3003.Utils;
import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Comandos extends TelegramLongPollingBot {
    private BotLogistica botLogistica;

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
                        "/menu - Muestra el menú\n" +
                        "/info - Información sobre el bot");
                break;
            case "/info":
                sendMessage(chatId, "Soy un bot creado para ayudarte con tu aplicación.");
                break;
            case "/menu":
                showMenu(chatId);
                break;
            case "/darDeAltaRuta":
                String respuesta = botLogistica.darAltaRuta();
                sendMessage(chatId, respuesta);
                break;
            default:
                sendMessage(chatId, "Comando no reconocido.");
        }
    }

    private void sendMessage(Long chatId, String text) {
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
                "/darDeAltaRuta - Crear ruta\n" +
                "/crearVianda - Crear vianda\n" +
                "/retirarVianda - Retirar vianda\n" +
                "/verIncidencias - Ver incidencias\n" +
                "/verHeladeras - Ver heladeras en zona\n";

        sendMessage(chatId, menuText); // Enviar el menú como texto
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

