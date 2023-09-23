package developer.voidw;

import okhttp3.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class strings {

    /*
    If true, the license is not activated
     */
    private static boolean isTrue = true;

    /*
    Check if the license is valid
     */
    public static void setFalse(String license) {
        if (strings.isLicenseValid(license)) strings.isTrue = false;
    }
    public static boolean isTrue() {
        return isTrue;
    }

    private static boolean isLicenseValid(String licenseKey) {
        final OkHttpClient client = new OkHttpClient().newBuilder().build();
        final MediaType mediaType = MediaType.parse("application/json");
        final RequestBody body = RequestBody.create("{\n    \"license\": \"" + licenseKey + "\",\n    \"product\": \"" + "Deluxe Void World" + "\",\n    \"version\": \"" + "1.0.0" + "\"\n}", mediaType);
        final Request request = new Request.Builder()
                .url("https://panel.stn-studios.dev/api/client")
                .method("POST", body)
                .addHeader("Authorization", "eKNAZE3A3kGSRRadPeckCVWpjD4DQJYj3vqgXmiL")
                .build();
        try {
            final Response response = client.newCall(request).execute();

            final String data = response.body() != null ? response.body().string() : "Null";
            final JSONObject obj = (JSONObject) new JSONParser().parse(data);

            if (!obj.containsKey("status_msg") || !obj.containsKey("status_id")) return true;
            return !obj.containsKey("status_overview");
        } catch (IOException | ParseException ignored) {
            return true;
        }
    }
}