package com.postlundhall.TraningsregisterSpringH2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representing the application's {@link Instruktion} entities.
 * <p>
 * Every Instruktion comprises a version and an id, along with the antalSet for its associated Ovning.
 * </p>
 * @author postlundhall
 * @since 1.0
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Instruktion {

    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "traningspass_id", nullable = false)
    @NotNull(message = "Träningspass måste anges")
    private Traningspass traningspass;

    @Min(value = 1, message = "Antal set får vara minst 1")
    @Max(value = 100, message = "Antal set får vara max 100")
    private int antalSet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ovning_id", nullable = false)
    @NotNull(message = "Övning måste anges")
    private Ovning ovning;


}
