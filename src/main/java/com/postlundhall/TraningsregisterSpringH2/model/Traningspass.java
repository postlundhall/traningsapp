package com.postlundhall.TraningsregisterSpringH2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Model representing the application's Traningspass-entities.
 * Every Traningspass comprises version, id, name, syfte, niva, langd, and instruktioner attributes.
 * syfte and niva are enumerated by a selection of values for their respective attribute.
 * The instruktioner-list comprise Instruktion-entities for the Ovning-entities that make up the Traningspass.
 * @author postlundhall
 * @since 1.0
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Traningspass {

    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ange passnamn")
    @Size(max = 50, message = "Passets namn får vara max 50 tecken")
    private String passnamn;

    @Enumerated(EnumType.STRING)
    private Syfte syfte;

    @Enumerated(EnumType.STRING)
    private Niva niva;

    @Min(value = 1, message = "Längden får vara minst 1 minut")
    @Max(value = 300, message = "Längden får max vara 300 minuter")
    private int langdMins;

    @OneToMany(mappedBy = "traningspass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Instruktion> instruktioner = new ArrayList<>();

    // Methods for bidirectional relationship
    public void addInstruktion(Instruktion instruktion) {
        instruktioner.add(instruktion);
        instruktion.setTraningspass(this);
    }

    public void removeInstruktion(Instruktion instruktion) {
        instruktioner.remove(instruktion);
        instruktion.setTraningspass(null);
    }

    /** @hidden */
    public enum Syfte {
        STYRKA, BYGG, VIKTMINSKNING, RÖRLIGHET, FLÅS, UNDERHÅLL
    }

    /** @hidden */
    public enum Niva {
        NYBÖRJARE, MEDEL, AVANCERAD, ELIT
    }
}