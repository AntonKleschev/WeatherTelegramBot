import com.fasterxml.jackson.annotation.JsonCreator;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Получили токен на телеграме.
//В тест java написали зависимости dependencies
//Сдесь создали класс и унаследовались от телеграмма



public class Bot extends TelegramLongPollingBot {

    public static void main(String[] args) {

        //нужно проинициализировать API.
        ApiContextInitializer.init();

        //создаем объект телеграмма
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        //регестрируем бота
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException ex) {
            ex.printStackTrace();
        }
//          Тут закоменченно vnp для Тора. Не работает чего-то.
//        System.getProperties().put("proxySet", "true");
//        System.getProperties().put("socksProxyHost", "127.0.0.1");
//        System.getProperties().put("socksProxyPort", "9150");
    }

    // через альт энтер имплимитируем следующие три метода.
    // onUpdateReceived для получения сообщений. Люновляет сообщения через LongPull
    //LongPull это очередь ожидающих запросов запрос на сервер, потом ждем событие (сервером), далее событие отправляется в ответ на запрос
    // и клиет отправляет новый ожидающий запрос
    public void onUpdateReceived(Update update) {
        Model model = new Model();
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/help":
                    sendMsg(message, " Чем я могу помоч Вам?");
                    break;
                case "/setting":
                    sendMsg(message, "Что будем настраивать?");
                    break;
                default:
                    try {
                        sendMsg(message, Weather.getWeather(message.getText(), model));
                    } catch (IOException e) {
                        sendMsg(message, "Такой город не найден!");
                    }
            }
        }
    }

    //что бы вернуть имя нашего бота, указанного при регистрации
    public String getBotUsername() {
        return "TestStudyWeatherBot";
    }

    //наш токен, полученный при регистрации
    public String getBotToken() {
        return "1284650134:AAGQPlALEBddVONQ9cWMvbBYmeNAhKxZqqo";
    }


    //создаем метод для ответа на сообщения (для отправки сообдений)
    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        //вклчаем возможность разметки.
        sendMessage.enableMarkdown(false);
        //устанавливаем id чата, что бы было понятно, кому отвечать:
        sendMessage.setChatId(message.getChatId().toString());
        //на какое конкретно сообщение мы должны ответить.
        sendMessage.setReplyToMessageId(message.getMessageId());
        //установим этому сообщению ответ, который мы отправляли в этот метод
        sendMessage.setText(text);

        //устанавливаем саму отправку нашего сообщения.
        try {
            setButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Ошибка при отварке сообщений.");
            e.printStackTrace();
        }
    }

    //3. Добавляем клавиантуру под текстовой панелью
    public void setButtons(SendMessage sendMessage) {
        //создаем клавиатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        // разметка для клавиатуры Связваем сообщение с клавиатурой
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        // указваем кому показывать клавиатуру
        replyKeyboardMarkup.setSelective(true);
        // подгонка клавы
        replyKeyboardMarkup.setResizeKeyboard(true);
        //скрывать ли клаву после использования
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // создаем кнопки
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyBoardFirstRow = new KeyboardRow();
        keyBoardFirstRow.add(new KeyboardButton("/help"));
        keyBoardFirstRow.add(new KeyboardButton("/setting"));

        //добавляем все строчки квлаы в списко
        keyboardRows.add(keyBoardFirstRow);
        //устанавливаес список клавеатуре
        replyKeyboardMarkup.setKeyboard(keyboardRows);

        //что бы клава заработала нужно поместить ее в отправку сообщений ^

    }

}
