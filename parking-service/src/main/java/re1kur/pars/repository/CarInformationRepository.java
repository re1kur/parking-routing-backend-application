package re1kur.pars.repository;

import org.springframework.data.repository.CrudRepository;
import re1kur.pars.entity.car.CarInformation;

public interface CarInformationRepository extends CrudRepository<CarInformation, Integer> {
}
