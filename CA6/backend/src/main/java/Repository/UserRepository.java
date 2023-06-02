package Repository;

import Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    String findUsernameByEmail(String userEmail);

    User findByEmail(String userEmail);

    Optional<User> findByUsername(String username);
}
