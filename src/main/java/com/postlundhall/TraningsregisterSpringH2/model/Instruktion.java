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

public class Instruktion {

    @Id
    private String ovningsnamn;
    private int antalSet;
    private String repRange;
    private int langdMins;


}
