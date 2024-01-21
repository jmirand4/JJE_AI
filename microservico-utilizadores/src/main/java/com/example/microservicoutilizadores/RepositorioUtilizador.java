package com.example.microservicoutilizadores;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RepositorioUtilizador extends JpaRepository<Utilizador, Integer> {

    @Transactional
    @Modifying
    public void deleteByUserId(Integer id);


    Optional<Utilizador> findByUsername(String nome);

    @Query(value= "select u.Utilizador_ID from UTILIZADOR u where u.USER_NAME = :nome", nativeQuery = true)
    Integer findIdByUsername(@Param("nome") String name);

    @Transactional
    @Modifying
    @Query("UPDATE Utilizador u SET u.username = :name, u.password = :password WHERE u.userId = :id")
    void updateUserInfo(@Param("id") Integer id, @Param("name") String name, @Param("password") String password);

    @Transactional
    @Modifying
    @Query("UPDATE Utilizador u SET u.estaBloqueado = :estaBloqueado WHERE u.userId = :id")
    void updateUserStatus(@Param("id") Integer id, @Param("estaBloqueado") boolean estaBloqueado);

}
