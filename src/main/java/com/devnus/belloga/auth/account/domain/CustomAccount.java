package com.devnus.belloga.auth.account.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "custom")
@DiscriminatorValue("custom")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
public class CustomAccount extends Account {
    @Column(name = "password")
    private String password;
}
