package re1kur.is.repository.sql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re1kur.is.entity.UserInformation;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserInformationRepository extends JpaRepository<UserInformation, UUID> {
    Optional<UserInformation> findByUserId(UUID userId);
}
