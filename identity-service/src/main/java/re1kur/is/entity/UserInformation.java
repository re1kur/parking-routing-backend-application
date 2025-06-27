package re1kur.is.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_information")
public class UserInformation {

    @Id
    private UUID userId;

    private String firstName;

    private String lastName;

    private String middleName;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}
