package Repository;

import Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    String findUsernameByEmail(String userEmail);

    User findByEmail(String userEmail);

    User findByUsername(String username);
}
