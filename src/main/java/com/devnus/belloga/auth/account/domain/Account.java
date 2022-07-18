package com.devnus.belloga.auth.account.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "account")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
public abstract class Account {
    @Id
    @Column(name = "account_id")
    private String id;

    @Column(name = "username")
    private String username; //기업이면 이메일, Oauth면 식별값

    @Column(name = "is_locked")
    private boolean isLocked;
}
