package com.idea.toptal.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Random;

@Entity
@Table(name="PLAYER")
@Getter
@Setter
@NoArgsConstructor
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    Long id;

    @Column(name="first_name")
    String first_name;

    @Column(name="last_name")
    String last_name;

    @Column(name="country")
    String country;

    @Column(name="teamId")
    String teamId;

    @Column(name="positions")
    Position positions;

    @Column(name="marketvalue")
    Double marketvalue;

    @Column(name="age")
    Integer age;

    public Player(String first_name, String last_name, String country, String teamId, Position positions, Double marketvalue) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.country = country;
        this.teamId = teamId;
        this.positions = positions;
        this.marketvalue = marketvalue;
        this.age = getRandomNumberInRange(18, 40);
    }

    public void updateMarketValue(Double ask_value){
        double randomPercentage = (double)getRandomNumberInRange(10, 100)/100;
        double newValue = ask_value*(1 + randomPercentage);
        this.setMarketvalue(newValue);
    }

    private int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
