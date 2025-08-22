package com.postlundhall.TraningsregisterSpringH2.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ovning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 100)
    private String namn;

    @Min(1)
    @Max(100)
    private int antalReps;

    @Min(1)
    @Max(20)
    private int antalSet;
}
