package Service;

import Domain.Comment;
import Domain.User;
import HTTPRequestHandler.HTTPRequestHandler;
import Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) throws Exception {
        this.userRepository = userRepository;
        fetchData();
    }
    private void fetchData() throws Exception {
        final String USERS_URI = "http://5.253.25.110:5000/api/users";
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        List<User> users = objectMapper.readValue(HTTPRequestHandler.getRequest(USERS_URI), typeFactory.constructCollectionType(List.class, User.class));
        userRepository.saveAll(users);
    }
}
