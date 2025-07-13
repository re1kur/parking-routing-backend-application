package re1kur.pars.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import re1kur.pars.entity.Make;

public interface MakeRepository extends CrudRepository<Make, Integer> {
    Boolean existsByName(String name);

    Page<Make> findAll(Pageable pageable);
}
