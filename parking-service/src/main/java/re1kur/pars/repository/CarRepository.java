package re1kur.pars.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import re1kur.pars.entity.car.Car;

import java.util.List;
import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {
    @Query(value = """
            select exists(select * from cars where license_plate = ? and region_code = ?);
            """,
            nativeQuery = true)
    boolean existsByLicensePlateAndRegionCode(
            String licensePlate,
            String code);

    List<Car> findAllByOwnerId(UUID userId);
}
