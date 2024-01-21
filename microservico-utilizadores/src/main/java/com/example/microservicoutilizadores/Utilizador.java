package com.example.microservicoutilizadores;





import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Entity
@Table(name="Utilizador")
public class Utilizador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Utilizador_ID")
    private Integer userId;


    @Column(name="UserName", unique = true)
    private String username;

    @Column(name="Password")
    private String password;

    @Column(name="ESTA_BLOQUEADO")
    private Boolean estaBloqueado;

    @JoinColumn(name="role_name", referencedColumnName = "name")
    @ManyToOne
    private Role role;

    public Utilizador(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.estaBloqueado = false;
        this.role = role;
    }

}
