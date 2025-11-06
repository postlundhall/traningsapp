package com.postlundhall.TraningsregisterSpringH2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representing the application's Ovning-entities.
 * Every Ovning comprises {@code version}, id, name, {@link PrimarMuskel} and equipmentTraningspass attributes.
 * primarMuskel and equipmentTraningspass are enumerated by a selection of values for their respective attribute.
 * @author postlundhall
 * @since 1.0
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ovning {

    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Övningsnamn får inte vara tomt")
    private String ovningsnamn;

    @NotNull(message = "Primär muskel måste väljas")
    @Enumerated(EnumType.STRING)
    private PrimarMuskel primarMuskel;

    @NotNull(message = "Utrustning måste väljas")
    @Enumerated(EnumType.STRING)
    private Utrustning utrustning;

    /** @hidden */
    public enum Utrustning {
        STÄLLNING, // RACK
        MASKIN, // MACHINE
        HANTLAR, // DUMBBELLS
        BÄNK // BENCH
    }

    /** @hidden */
    public enum PrimarMuskel {
        AXEL_FRAMSIDA, // Deltoid, front
        AXEL_MITTEN, // Deltoid, lateral
        AXEL_BAKSIDA, // Deltoid, rear
        RYGG_NEDRE, // Back, lower
        RYGG_MITTEN, // Back, middle
        RYGG_OVRE, // Back, upper
        LATS, // Lats
        SATESMUSKEL, // Glutes
        LAR_FRAMSIDA, // Thigh, front
        LAR_BAKSIDA, // Thigh, rear
        VADER, // Calves
        TIBIA_SKENBEN, // Tibialis
        MAGE_RECTUS, // Rectus abdominis
        MAGE_TRANSVERSUS, // Transverse abdominis
        MAGE_OBLIQUES, // Obliques
        BROST_NEDRE, // Chest, lower
        BROST_MITTEN, // Chest, middle
        BROST_OVRE, // Chest, upper
        TRICEPS, // Triceps
        BICEPS, // Biceps
        UNDERARM_UNDERSIDA, // Forearm, flexors
        UNDERARM_OVERSIDA // Forearm, extensors
    }
}