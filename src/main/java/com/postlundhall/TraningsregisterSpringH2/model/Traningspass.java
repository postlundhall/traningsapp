package com.postlundhall.TraningsregisterSpringH2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data                   // Lombok för getters, setters, toString, equals/hashCode
@NoArgsConstructor      // Lombok för tom konstruktor
@AllArgsConstructor     // Lombok för full konstruktor
@Builder

public class Traningspass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Inkrement av ID
    private Long id;

    @NotBlank(message = "Ange passnamn")
    @Size(max = 50, message = "Passets namn får vara max 50 tecken")
    private String passnamn;

    @NotBlank(message = "Ange syftet med träningspasset")
    @Size(max = 50, message = "Syfte får max vara 50 tecken")
    private String syfte;

    @NotBlank(message = "Ange nivå för passet (t.ex. Nybörjare, Medel, Avancerad)")
    private String niva;

    @Min(value = 1, message = "Längden får vara minst 1 minut")
    @Max(value = 300, message = "Längden får max vara 300 minuter")
    private int langdMins;


}
