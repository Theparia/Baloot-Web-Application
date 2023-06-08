package Controller;

import DTO.AuthDTO;
import Exceptions.EmailAlreadyExists;
import Model.User;
import Service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:3000",exposedHeaders = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    protected ResponseEntity<AuthDTO> login(@RequestBody Map<String, String> info){
        try{
            String jwtToken = authService.login(info.get("username"), info.get("password"));
            AuthDTO authDTO = new AuthDTO(info.get("username"), jwtToken);
            return ResponseEntity.ok(authDTO);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    protected ResponseEntity<AuthDTO> signup(@RequestBody Map<String, String> info){
        try{
            authService.addUser(new User(info.get("username"), info.get("password"), info.get("email"), info.get("birthDate"), info.get("address"), 0));
            String jwtToken = authService.login(info.get("username"), info.get("password"));
            AuthDTO authDTO = new AuthDTO(info.get("username"), jwtToken);
            return ResponseEntity.ok(authDTO);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/callback",method = RequestMethod.GET)
    protected ResponseEntity<AuthDTO> callbackLogin(@RequestParam(value = "code", required = true) String code) throws IOException, InterruptedException, ParseException {
//        if(iemdbSystem.getLoggedInUser() != null) {
//            String jwt_token = iemdbSystem.createJwtToken(iemdbSystem.getLoggedInUser().getEmail());
//            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//            map.add("token", jwt_token);
//            map.add("userEmail",iemdbSystem.getLoggedInUser().getEmail());
//            return new ResponseEntity<>(map,HttpStatus.OK);
//        }
        String clientId = "8ff24355f5dd638c4422";
        String clientSecret = "6eacc3ef84f377e4f4c3ed0836459135e057d200";
        String accessTokenUrl = String.format(
                "https://github.com/login/oauth/access_token?client_id=%s&client_secret=%s&code=%s",
                clientId, clientSecret, code
        );
        HttpClient client = HttpClient.newHttpClient();
        URI access_token_uri = URI.create(accessTokenUrl);
        HttpRequest.Builder accessTokenBuilder = HttpRequest.newBuilder().uri(access_token_uri);
        HttpRequest accessTokenRequest = accessTokenBuilder
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> accessTokenResult = client.send(accessTokenRequest, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> resultBody = mapper.readValue(accessTokenResult.body(),HashMap.class);
        String accessToken = (String) resultBody.get("access_token");
        URI userDataURI = URI.create("https://api.github.com/user");
        HttpRequest.Builder userDataBuilder = HttpRequest.newBuilder().uri(userDataURI);
        HttpRequest request = userDataBuilder.GET()
                .header("Authorization", String.format("token %s",accessToken))
                .build();
        HttpResponse<String> userDataResult = client.send(request,HttpResponse.BodyHandlers.ofString());
        HashMap dataBody = mapper.readValue(userDataResult.body(),HashMap.class);
        String user_email = (String) dataBody.get("email");
        String username = (String) dataBody.get("login");
        String address = (String) dataBody.get("location");
        String createdAt = (String) dataBody.get("created_at");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthdate = LocalDate.parse(createdAt.substring(0, 10), formatter).minusYears(18);
        String password = "";
        try {
            authService.addUser(new User(username, password, user_email, birthdate.toString(), address, 0));
        } catch (EmailAlreadyExists e) { //todo: username already exist exception
            //todo: ??
//            iemdbSystem.updateUser(name,user_email,password,nickname,birthdate);
        }
        String jwtToken = authService.createJwtToken(username);
        AuthDTO authDTO = new AuthDTO(username, jwtToken);
        return ResponseEntity.ok(authDTO);
//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.add("token", jwt_token);
//        map.add("userEmail",user_email);
//        return new ResponseEntity<>(map,HttpStatus.OK);
    }
}