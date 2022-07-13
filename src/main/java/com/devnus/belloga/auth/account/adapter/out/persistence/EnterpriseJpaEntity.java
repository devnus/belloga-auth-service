package com.devnus.belloga.auth.account.adapter.out.persistence;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("enterprise")
@NoArgsConstructor
@Builder
public class EnterpriseJpaEntity extends AccountJpaEntity{
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String organization;

    @Builder
    public EnterpriseJpaEntity(String email, String password, String name, String phoneNumber, String organization) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.organization = organization;
    }
}
