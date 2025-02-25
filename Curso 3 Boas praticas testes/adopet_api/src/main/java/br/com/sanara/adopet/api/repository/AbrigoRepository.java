package br.com.sanara.adopet.api.repository;

import br.com.sanara.adopet.api.model.Abrigo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AbrigoRepository extends JpaRepository<Abrigo, Long> {

    Optional<Abrigo> findByNome(String nome);

    boolean existsByNomeOrTelefoneOrEmail(String nome, String telefone, String email);

}
