package com.arimar.gwent.domain.player;

//import com.arimar.gwent.domain.carta.CartaEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "gw_jugador")
public class JugadorEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="apodo", unique = true)
    private String apodo;

    //@OneToMany(mappedBy = "jugador")
    //@JsonManagedReference
    //private List<CartaEntity> cartaEntities;

}
