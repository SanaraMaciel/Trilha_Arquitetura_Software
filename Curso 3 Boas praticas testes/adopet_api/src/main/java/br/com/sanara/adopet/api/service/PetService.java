package br.com.sanara.adopet.api.service;

import br.com.sanara.adopet.api.dto.CadastroPetDto;
import br.com.sanara.adopet.api.dto.PetDto;
import br.com.sanara.adopet.api.model.Abrigo;
import br.com.sanara.adopet.api.model.Pet;
import br.com.sanara.adopet.api.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    @Autowired
    private PetRepository repository;

    public List<PetDto> buscarPetsDisponiveis() {
        return repository
                .findAllByAdotadoFalse()
                .stream()
                .map(PetDto::new)
                .toList();
    }

    public void cadastrarPet(Abrigo abrigo, CadastroPetDto dto) {
        repository.save(new Pet(dto, abrigo));
    }
}
