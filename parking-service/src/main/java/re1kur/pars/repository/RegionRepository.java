package re1kur.pars.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import re1kur.pars.entity.region.Region;

public interface RegionRepository extends CrudRepository<Region, Integer> {
    Page<Region> findAll(Pageable pageable);

    boolean existsByName(String name);
}
