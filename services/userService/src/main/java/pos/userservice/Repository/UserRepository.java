package pos.userservice.Repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import pos.userservice.Module.User;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);

}
