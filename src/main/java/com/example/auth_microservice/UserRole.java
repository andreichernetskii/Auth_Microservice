package com.example.auth_microservice;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table( name = "roles")
public class UserRole {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private long id;
    private String roleName;
}
