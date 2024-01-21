package com.example.microservicotarefas;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface RepositorioTarefa extends JpaRepository<Tarefa, Integer> {

    @Query(value = "select * from TAREFA t where t.utilizadorID = ?1", nativeQuery = true)
    public List<Tarefa> getTarefasPorUtilizadorId(Integer utilizadorId);


    @Transactional
    @Modifying
    public void deleteByTarefaId(Integer id);





    @Modifying
    @Transactional
    @Query("UPDATE Tarefa t SET t.dataFim = :dataFim, t.duracao = :duracao, t.estado = 'CONCLUIDA', t.objetosIdentificados = :objetos WHERE t.tarefaId = :tarefaId")
    void atualizarTarefa(
            @Param("tarefaId") Integer tarefaId,
            @Param("dataFim") Timestamp dataFim,
            @Param("duracao") long duracao,
            @Param("objetos") ArrayList<String> objetos
    );

    @Query(value= "select * from TAREFA t where t.utilizador_ID = ?1 and t.tarefa_estado != 'BANIDA'", nativeQuery = true)
    ArrayList<Tarefa> findByUserId(Integer idUser);


    @Modifying
    @Transactional
    @Query("UPDATE Tarefa t SET t.estado = :estado WHERE t.tarefaId = :tarefaId")
    void cancelarTarefa(@Param("tarefaId") Integer tarefaId, @Param("estado") String estadoCancelada);

    @Modifying
    @Transactional
    @Query("UPDATE Tarefa t SET t.estado = :estado WHERE t.tarefaId = :tarefaId")
    void permitirTarefa(@Param("tarefaId") Integer tarefaId, @Param("estado") String estadoPermitir);

    @Modifying
    @Transactional
    @Query("UPDATE Tarefa t SET t.estado = :estado WHERE t.tarefaId = :tarefaId")
    void banirTarefa(@Param("tarefaId") Integer tarefaId, @Param("estado") String estadoBanida);
}
