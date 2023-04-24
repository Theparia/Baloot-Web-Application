package Controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:8080")
//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/x")
public class LoginController {


//    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping
    public String getUsers() {
        return "1234";
    }

}