package com.example.microservicotarefas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepositorioImagem extends JpaRepository<Imagem,Integer> {


    @Query(value="select * FROM IMAGEM t where t.IMAGEM_HASH = ?1", nativeQuery = true)
    Imagem findByImagemHash(String hash);

    @Query(value= "SELECT i.NOME FROM IMAGEM i WHERE i.ID = ?1", nativeQuery = true)
    public String findImageById(Integer imagemId);
}
