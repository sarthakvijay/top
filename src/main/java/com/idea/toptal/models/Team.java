package com.idea.toptal.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="TEAM")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Team {

    @Id
    String Id;

    @Column(name="name")
    String name;

    @Column(name="country")
    String country;

    @Column(name="marketvalue")
    Double marketvalue;

    @Column(name="budget")
    Double budget;
}
