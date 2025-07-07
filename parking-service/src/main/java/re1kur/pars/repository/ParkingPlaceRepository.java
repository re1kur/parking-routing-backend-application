package re1kur.pars.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import re1kur.pars.entity.ParkingPlace;

import java.util.List;

public interface ParkingPlaceRepository extends CrudRepository<ParkingPlace, Integer> {
    @Query(value =
            """
            SELECT p.number FROM ParkingPlace p
            """)
    List<Integer> findAllNumbers();
}
