package re1kur.is.repository.cache;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import re1kur.is.entity.Code;

@Repository
public interface CodeRepository extends CrudRepository<Code, String> {
}
