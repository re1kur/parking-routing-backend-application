package re1kur.pars.repository;

import org.springframework.data.repository.CrudRepository;
import re1kur.pars.entity.ParkingPlaceReservation;

import java.util.UUID;

public interface ReservationRepository extends CrudRepository<ParkingPlaceReservation, UUID> {
}
