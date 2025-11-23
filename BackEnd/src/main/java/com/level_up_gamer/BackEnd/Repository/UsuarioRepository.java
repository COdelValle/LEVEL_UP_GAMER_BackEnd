package com.level_up_gamer.BackEnd.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.level_up_gamer.BackEnd.Model.Usuario.Usuario;
import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByApiKey(String apiKey);
    boolean existsByEmail(String email);
    void deleteByEmail(String email);
}
