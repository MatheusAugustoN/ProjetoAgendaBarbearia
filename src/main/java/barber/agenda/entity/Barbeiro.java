package barber.agenda.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "barbeiro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Barbeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
}