import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public void main() {
    HttpClient client = HttpClient.newHttpClient();

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://api.hfs.purdue.edu/menus/v2/locations/Earhart/2026-08-23"))
        .GET()
        .build();

    HttpResponse<String> response = null;
    try {
        response =
            client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (Exception e) {
        IO.println("oops");
    }
    
    System.out.println(response.statusCode());
    System.out.println(response.body());
}
