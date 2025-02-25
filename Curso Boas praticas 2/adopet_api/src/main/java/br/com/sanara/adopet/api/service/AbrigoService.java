package br.com.sanara.adopet.api.service;

import br.com.sanara.adopet.api.dto.AbrigoDto;
import br.com.sanara.adopet.api.dto.CadastroAbrigoDto;
import br.com.sanara.adopet.api.exception.ValidacaoException;
import br.com.sanara.adopet.api.model.Abrigo;
import br.com.sanara.adopet.api.repository.AbrigoRepository;
import br.com.sanara.adopet.api.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbrigoService {

    @Autowired
    private AbrigoRepository abrigoRepository;

    @Autowired
    private PetRepository petRepository;

    public List<AbrigoDto> listar() {
        return abrigoRepository
                .findAll()
                .stream()
                .map(AbrigoDto::new)
                .toList();
    }

    public void cadatrar(CadastroAbrigoDto dto) {
        boolean jaCadastrado = abrigoRepository.existsByNomeOrTelefoneOrEmail(dto.nome(), dto.telefone(), dto.email());

        if (jaCadastrado) {
            throw new ValidacaoException("Dados já cadastrados para outro abrigo!");
        }

        abrigoRepository.save(new Abrigo(dto));
    }
}