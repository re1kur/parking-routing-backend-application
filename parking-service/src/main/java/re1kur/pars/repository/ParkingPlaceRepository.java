package re1kur.pars.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import re1kur.pars.entity.place.Place;

import java.util.List;

public interface ParkingPlaceRepository extends CrudRepository<Place, Integer> {
    @Query(value =
            """
            SELECT p.number FROM ParkingPlace p
            """)
    List<Integer> findAllNumbers();

    Page<Place> findAll(Pageable pageable);
}
