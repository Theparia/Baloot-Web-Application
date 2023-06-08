package Controller;

import DTO.AuthDTO;
import Exceptions.EmailAlreadyExists;
import Exceptions.InvalidCredentials;
import Exceptions.UserNotFound;
import Model.User;
import Service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    protected ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> info) {
        try {
            String jwtToken = authService.login(info.get("username"), info.get("password"));
            return ResponseEntity.ok(Map.of("jwtToken", jwtToken, "username", info.get("username")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("errorMessage", e.getMessage()));
        }
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    protected ResponseEntity<Map<String, String>> signup(@RequestBody Map<String, String> info){
        try{
            authService.addUser(new User(info.get("username"), info.get("password"), info.get("email"), info.get("birthDate"), info.get("address"), 0));
            String jwtToken = authService.login(info.get("username"), info.get("password"));
            return ResponseEntity.ok(Map.of("jwtToken", jwtToken, "username", info.get("username")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("errorMessage", e.getMessage()));
        }
    }


    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    protected ResponseEntity<AuthDTO> callbackLogin(@RequestParam(value = "code", required = true) String code) throws IOException, InterruptedException{
//        Parnian
        String clientId = "8ff24355f5dd638c4422";
        String clientSecret = "6eacc3ef84f377e4f4c3ed0836459135e057d200";
//        Paria
//        String clientId = "bf0229f4067042e56a4b";
//        String clientSecret = "9dcef5a409303cbdbabe92b35b4907ded461c3da";
        String accessTokenUrl = String.format(
                "https://github.com/login/oauth/access_token?client_id=%s&client_secret=%s&code=%s",
                clientId, clientSecret, code
        );
        HttpClient client = HttpClient.newHttpClient();
        URI accessTokenUri = URI.create(accessTokenUrl);
        HttpRequest.Builder accessTokenBuilder = HttpRequest.newBuilder().uri(accessTokenUri);
        HttpRequest accessTokenRequest = accessTokenBuilder
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> accessTokenResult = client.send(accessTokenRequest, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> resultBody = mapper.readValue(accessTokenResult.body(), HashMap.class);
        String accessToken = (String) resultBody.get("access_token");
        URI userDataURI = URI.create("https://api.github.com/user");
        HttpRequest.Builder userDataBuilder = HttpRequest.newBuilder().uri(userDataURI);
        HttpRequest request = userDataBuilder.GET()
                .header("Authorization", String.format("token %s", accessToken))
                .build();
        HttpResponse<String> userDataResult = client.send(request, HttpResponse.BodyHandlers.ofString());
        HashMap dataBody = mapper.readValue(userDataResult.body(), HashMap.class);
        String email = (String) dataBody.get("email");
        String username = (String) dataBody.get("login");
        String address = (String) dataBody.get("location");
        String createdAt = (String) dataBody.get("created_at");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(createdAt.substring(0, 10), formatter).minusYears(18);
        String password = "1234";
        User user = new User(username, password, email, birthDate.toString(), address, 0);
        try {
            authService.addUser(user);
        } catch (EmailAlreadyExists e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String jwtToken = authService.createJwtToken(username);
        AuthDTO authDTO = new AuthDTO(username, jwtToken);
        return ResponseEntity.ok(authDTO);
    }
}