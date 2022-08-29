package gg.matt.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Utils {
    public static JSONObject returnStream(String string) {
        URL url;
        try {
            url = new URL(string);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) throw new RuntimeException(responseCode + " " + connection.getResponseMessage());
            else {
                StringBuilder result = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext()) result.append(scanner.nextLine());
                scanner.close();
                if (!result.isEmpty()) return (JSONObject) new JSONParser().parse(result.toString());
            }
        } catch (IOException | ParseException ignored) {}
        return null;
    }
}
