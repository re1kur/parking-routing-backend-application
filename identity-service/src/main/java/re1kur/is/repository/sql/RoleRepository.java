package re1kur.is.repository.sql;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import re1kur.is.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM roles r WHERE r.name = 'USER';")
    Role findUserRole();
}
