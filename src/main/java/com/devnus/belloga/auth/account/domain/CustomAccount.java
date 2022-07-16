package com.devnus.belloga.auth.account.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("custom")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "custom")
public class CustomAccount extends Account {
    @Column(name = "password")
    private String password;
}
