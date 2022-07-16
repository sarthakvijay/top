package com.idea.toptal.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="TRANSFER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id")
    Player player;

    @Column(name="ask_value")
    Double ask_value;

    public Transfer(Player player, Double ask_value) {
        this.player = player;
        this.ask_value = ask_value;
    }
}
