import com.google.gson.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public void main() {
    HttpClient client = HttpClient.newHttpClient();

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://api.hfs.purdue.edu/menus/v2/locations/Earhart/2026-11-16"))
        .GET()
        .build();

    HttpResponse<String> response = null;
    try {
        response =
            client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (Exception e) {
        IO.println("Unable to connect with API");
        return;
    }

    if (response.statusCode() != 200) {
        IO.println("Status code: " + response.statusCode());
        return;
    }
    
    JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();
    JsonArray meals = root.getAsJsonArray("Meals");

    for (JsonElement m : meals) {
        JsonObject meal = m.getAsJsonObject();
        IO.println(meal.get("Name").getAsString());
        if (meal.get("Status").getAsString().equals("Closed")) {
            continue;
        } else {
            JsonArray stations = meal.getAsJsonArray("Stations");
            for (JsonElement s : stations) {
                JsonObject station = s.getAsJsonObject();
                IO.println("    " + station.get("Name").getAsString());
                
                JsonArray items = station.getAsJsonArray("Items");
                for (JsonElement i : items) {
                    JsonObject item = i.getAsJsonObject();
                    String name = item.get("Name").getAsString();
                    String id = item.get("ID").getAsString();
                    IO.println("        " + name + " " + id);
                }
            }
        }
        
    }

}
