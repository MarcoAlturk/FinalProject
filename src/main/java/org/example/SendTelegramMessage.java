package org.example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Class used to send telegram messages by implementing their API
 */
public class SendTelegramMessage extends TelegramLongPollingBot {
    private static final Logger logger = LoggerFactory.getLogger(SendTelegramMessage.class);

    /**
     * This class is just to store the name of the bot
     * @return
     */
    @Override
    public String getBotUsername() {
        return "EventManager1923Bot";
    }

    /**
     * This class is just to store the bot token
     * @return
     */
    @Override
    public String getBotToken() {
        return "7104909219:AAHTU_sWeQrqzcxMTQAYDqXoB6w0DSdWdnA";
    }

    /**
     * This class is used to send a message, the chatId is needed and the text to send
     * @param chatId the chat group ID
     * @param text the text to send
     */
    public void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        System.out.println("Please add the bot @EventManager1923Bot with admin privileges to the group to be able to send messages.");

        try {
            Message sentMessage = execute(message);
            PinChatMessage pinChatMessage = new PinChatMessage(chatId, sentMessage.getMessageId());
            execute(pinChatMessage);
            execute(message);
        } catch (TelegramApiException e) {
            logger.error("Failed to send message to chat ID {}: {}", chatId, e.getMessage());
        }
    }

    /**
     * Method needed to handle incoming messages, I did not need it so I didn't implement it
     * @param update
     */
    @Override
    public void onUpdateReceived(Update update) {

    }

}
