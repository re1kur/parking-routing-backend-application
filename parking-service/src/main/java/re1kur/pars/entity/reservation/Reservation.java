package re1kur.pars.entity.reservation;


import jakarta.persistence.*;
import lombok.*;
import re1kur.pars.entity.place.Place;

import java.util.UUID;

@Entity
@Table(name = "reservations")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "place_number")
    private Place place;

    private UUID userId;

    private UUID carId;

    @Column(insertable = false, columnDefinition = "DEFAULT FALSE")
    private Boolean paid;

    @OneToOne(mappedBy = "reservation", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private ReservationInformation information;
}
