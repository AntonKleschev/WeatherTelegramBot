import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

// здесь будем обробавать нашу погоду (парсить?)
// зарегестрироваться на openWeatherMap.org
// и создать токен 55fa593aee4969ae870b3e378e8df70f
public class Weather {

    //
    public static String getWeather(String message, Model model) throws IOException {
        // для извлечения дааных, нужно послать запрос к нашему API
        // для извлечения тут используем клас URL и в него помещаем наш запрос (нужно свормировать: https://api.openweathermap.org/data/2.5/weather?q=London&units=metric&appid=55fa593aee4969ae870b3e378e8df70f)
        URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + message + "&units=metric&appid=55fa593aee4969ae870b3e378e8df70f");

        //Читаем содержимое того, что пришло.
        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";
        while (in.hasNext()) {
            result += in.nextLine();
        }

        System.out.println(result);

        JSONObject object = new JSONObject(result);
        model.setName(object.getString("name"));

        JSONObject main = object.getJSONObject("main");
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));

        JSONArray getArray = object.getJSONArray("weather");
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject obj = getArray.getJSONObject(i);
            model.setIcon((String) obj.get("icon"));
            model.setMain((String) obj.get("main"));
        }

        return "City: " + model.getName() + "\n" +
                "Temperature: " + model.getTemp() + "C" + "\n" +
                "Humidity: " + model.getHumidity() + "%" + "\n" +
                "Main: " + model.getMain() + "\n" +
                "http://openweathermap.org/img/wn/" + model.getIcon() + ".png";
    }

}
