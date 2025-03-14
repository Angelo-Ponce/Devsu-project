package com.devsu.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "client")
public class Client extends Person {

    @Column(nullable = false, unique=true)
    private String clientId;

    @Column(nullable = false, length = 25)
    private String password;

    @Column(nullable = false, length = 1)
    private Boolean status;
}
