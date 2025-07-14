package re1kur.pars.entity.car;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "car_images")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "car")
public class CarImage {
    @Id
    private UUID carId;

    @MapsId
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id")
    private Car car;

    @Column(name = "file_id")
    private String fileId;

}
