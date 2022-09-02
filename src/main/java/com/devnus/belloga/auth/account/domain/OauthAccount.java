package com.devnus.belloga.auth.account.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "oauth")
@DiscriminatorValue("oauth")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
public class OauthAccount extends Account {
    @Column(name = "auth_provider")
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;
}
