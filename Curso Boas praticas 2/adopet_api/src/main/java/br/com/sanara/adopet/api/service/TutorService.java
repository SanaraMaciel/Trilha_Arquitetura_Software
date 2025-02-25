package br.com.sanara.adopet.api.service;

import br.com.sanara.adopet.api.dto.TutorDto;
import br.com.sanara.adopet.api.exception.ValidacaoException;
import br.com.sanara.adopet.api.model.Tutor;
import br.com.sanara.adopet.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TutorService {

    @Autowired
    private TutorRepository repository;

    public void cadastrar(TutorDto tutorDto) {

        boolean jaCadastrado = repository.existsByTelefoneOrEmail(tutorDto.telefone(), tutorDto.email());

        if (jaCadastrado) {
            throw new ValidacaoException("Dados já cadastrados para outro tutor!");
        }

        Tutor tutor = new Tutor(tutorDto.nome(), tutorDto.telefone(), tutorDto.email());
        repository.save(tutor);
    }

    public void atualizar(TutorDto dto) {
        Tutor tutor = repository.getReferenceById(dto.id());
        tutor.atualizarDados(dto);
        repository.save(tutor);
    }

}