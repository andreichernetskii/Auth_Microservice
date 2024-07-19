package com.example.auth_microservice;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table( name = "users" )
public class AppUser {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    // todo: add roles or not??
    @ManyToMany( fetch = FetchType.EAGER )
    private Set<UserRole> roles = new HashSet<>();
}
