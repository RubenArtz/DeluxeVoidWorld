package developer.voidw;

import okhttp3.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ruben_artz.world.features.addColor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class strings {
    private static final String SECRET_KEY = "O9RKhWeXgo3ggmKapl2q4fLUrBOSLFZl";

    private static final Map<String, String> INVALID_MESSAGE_MAP = new HashMap<String, String>() {{
        put("EXPIRED_AND_DELETED", addColor.ColorCode.colorizeConsole(addColor.ColorCode.COLOR_RED,
                "The license has expired and has been deleted."));
        put("EXPIRED_KEY", addColor.ColorCode.colorizeConsole(addColor.ColorCode.COLOR_RED,
                "Your license expired please contact the developer."));
        put("PRODUCT_NOT_EXIST", addColor.ColorCode.colorizeConsole(addColor.ColorCode.COLOR_RED,
                "The product does not exist."));
        put("VERSION_FIELD_EMPTY", addColor.ColorCode.colorizeConsole(addColor.ColorCode.COLOR_RED,
                "Version field is empty."));
        put("DIFFERENT_PRODUCT_VERSION", addColor.ColorCode.colorizeConsole(addColor.ColorCode.COLOR_RED,
                "The product version is different."));
        put("PRODUCT_DISABLED", addColor.ColorCode.colorizeConsole(addColor.ColorCode.COLOR_RED,
                "The product was disabled."));
        put("PRODUCT_FIELD_EMPTY", addColor.ColorCode.colorizeConsole(addColor.ColorCode.COLOR_RED,
                "Product name is empty."));
        put("REQUEST_LIMIT", addColor.ColorCode.colorizeConsole(addColor.ColorCode.COLOR_RED,
                "Your license exceeded the limit of requests, contact the developer."));
    }};

    /*
    If true, the license is not activated
     */
    private static boolean isTrue = true;

    /*
    Check if the license is valid
     */
    public static void setFalse(String license) {
        if (!strings.isLicenseValid(license)) strings.isTrue = false;
    }
    public static boolean isTrue() {
        return isTrue;
    }

    private static boolean isLicenseValid(String licenseKey) {
        String url = "https://dashboard.stn-studios.dev/api.php?secret=" + SECRET_KEY + "&type=license&key=" + licenseKey + "&product=Deluxe Void World&version=1.0.0";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String jsonData = response.body() != null ? response.body().string() : null;

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonData);

            if (jsonData == null || jsonData.trim().isEmpty() || jsonObject == null) {
                System.out.println(addColor.ColorCode.colorizeConsole(addColor.ColorCode.COLOR_RED,
                        "Your license exceeded the limit of requests, contact the developer."));
                return false;
            }

            if (jsonObject.containsKey("message")) {
                String message = (String) jsonObject.get("message");

                if (INVALID_MESSAGE_MAP.containsKey(message)) {
                    System.out.println(INVALID_MESSAGE_MAP.get(message));
                    return false;
                }
            }

            return jsonObject.containsKey("valid") && (Long) jsonObject.get("valid") == 1;
        } catch (IOException | org.json.simple.parser.ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
}