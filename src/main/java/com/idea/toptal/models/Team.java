package com.idea.toptal.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="TEAM")
@Getter
@Setter
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column(name="name")
    String name;

    @Column(name="country")
    String country;

    @Column(name="marketvalue")
    Double marketvalue;
}
