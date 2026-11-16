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

    for (JsonElement m : meals) {  // for each meal
        JsonObject meal = m.getAsJsonObject();
        if (meal.get("Status").getAsString().equals("Closed")) {
            continue;
        } else {
            JsonArray stations = meal.getAsJsonArray("Stations");
            for (JsonElement s : stations) {  // for each station
                JsonObject station = s.getAsJsonObject();
                JsonArray items = station.getAsJsonArray("Items");
                for (JsonElement i : items) {  // for each item at a station
                    JsonObject item = i.getAsJsonObject();
                    String name = item.get("Name").getAsString();
                    String id = item.get("ID").getAsString();
                    HttpRequest request1 = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.hfs.purdue.edu/menus/v2/items/" + id))
                        .GET()
                        .build();

                    HttpResponse<String> ingList = null;
                    try {
                        ingList =
                            client.send(request1, HttpResponse.BodyHandlers.ofString());
                    } catch (Exception e) {
                        IO.println("Unable to connect with API");
                        return;
                    }
                    if (ingList.statusCode() != 200) {
                        IO.println("Status code: " + ingList.statusCode());
                        return;
                    }
                    
                    JsonObject list = JsonParser.parseString(ingList.body()).getAsJsonObject();
                    if (list.get("Ingredients") == null) {
                        IO.println("Null");
                        continue;
                    }
                    String ingredients = list.get("Ingredients").getAsString();
                    IO.println(ingredients);
                }
            }
        }
        
    }

}
