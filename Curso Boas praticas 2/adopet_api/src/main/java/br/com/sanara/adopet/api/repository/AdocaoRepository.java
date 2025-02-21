package br.com.sanara.adopet.api.repository;

import br.com.sanara.adopet.api.model.Adocao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdocaoRepository extends JpaRepository<Adocao, Long> {

}
