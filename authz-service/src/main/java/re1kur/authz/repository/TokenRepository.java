package re1kur.authz.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import re1kur.authz.entity.RefreshToken;

@Repository
public interface TokenRepository extends CrudRepository<RefreshToken, String> {
}
