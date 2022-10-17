package brain.repos;

import brain.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String id);
    User findByActivationCode(String code);
}
