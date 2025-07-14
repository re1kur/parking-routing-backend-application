package re1kur.pars.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import re1kur.pars.entity.car.Car;
import re1kur.pars.entity.car.CarImage;

import java.util.Optional;

@Repository
public interface CarImageRepository extends CrudRepository<CarImage, Car> {
    Optional<CarImage> findByFileId(String fileId);
}
