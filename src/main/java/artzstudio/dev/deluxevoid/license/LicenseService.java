/*
 *
 *  Copyright (c) 2026 Ruben_Artz and Artz Studio. All rights reserved.
 *
 *  This code is proprietary software. It is strictly prohibited to
 *  copy, modify, distribute, or use this code for any purpose
 *  without the express written permission of the owner.
 *
 *  Project: Deluxe Void World
 *
 */

package artzstudio.dev.deluxevoid.license;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class LicenseService {

    private final OkHttpClient client;
    private final String apiUrl;

    public LicenseService() {
        this.client = new OkHttpClient();
        this.apiUrl = "https://dash.artzstudio.dev/verify.php";
    }

    public CompletableFuture<LicenseResult> validate(String licenseKey) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String product = "Deluxe Void World";
                String encodedProduct = URLEncoder.encode(product, StandardCharsets.UTF_8);

                String url = String.format("%s?key=%s&product=%s", this.apiUrl, licenseKey, encodedProduct);

                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("User-Agent", "Deluxe-Void-World-Protection")
                        .build();

                try (Response response = client.newCall(request).execute()) {

                    String jsonData = response.body().string();

                    if (jsonData.trim().isEmpty()) {
                        return new LicenseResult(false, "connection_error", "Empty response from server");
                    }

                    try {
                        JsonObject json = JsonParser.parseString(jsonData).getAsJsonObject();

                        boolean isValid = json.has("valid") && json.get("valid").getAsBoolean();
                        String status = json.has("status") ? json.get("status").getAsString() : "unknown";
                        String message = json.has("message") ? json.get("message").getAsString() : "No message provided";

                        return new LicenseResult(isValid, status, message);

                    } catch (Exception e) {
                        return new LicenseResult(false, "parse_error", "Invalid JSON: " + e.getMessage());
                    }

                } catch (IOException e) {
                    return new LicenseResult(false, "connection_error", "Network Error: " + e.getMessage());
                }
            } catch (Exception e) {
                return new LicenseResult(false, "internal_error", "Error preparing request: " + e.getMessage());
            }
        });
    }

    public record LicenseResult(boolean isValid, String statusCode, String serverMessage) {
    }
}