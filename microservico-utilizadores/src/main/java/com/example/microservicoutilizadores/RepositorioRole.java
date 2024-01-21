package com.example.microservicoutilizadores;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface RepositorioRole extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String roleName);
}
