package zesp03.common.repository;

import org.springframework.data.repository.CrudRepository;
import zesp03.common.entity.Token;

public interface TokenRepository extends CrudRepository<Token, Long> {
}
