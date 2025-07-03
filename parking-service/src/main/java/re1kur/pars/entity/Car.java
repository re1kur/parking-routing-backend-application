package re1kur.pars.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "cars")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID ownerId;

    private String licensePlate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "region_code")
    private Code regionCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private CarInformation carInformation;

    @ElementCollection
    @CollectionTable(name = "car_images",
                joinColumns = @JoinColumn(name = "car_id"))
    @Column(name = "file_id")
    private Set<String> imageIds = new HashSet<>();
}
