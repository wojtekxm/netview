package zesp03.common.repository;

import org.springframework.data.repository.CrudRepository;
import zesp03.common.entity.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByName(String name);
}
