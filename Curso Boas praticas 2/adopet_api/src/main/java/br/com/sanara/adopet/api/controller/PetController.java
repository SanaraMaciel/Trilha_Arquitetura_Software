package br.com.sanara.adopet.api.controller;

import br.com.sanara.adopet.api.dto.DadosDetalhesPetDto;
import br.com.sanara.adopet.api.model.Pet;
import br.com.sanara.adopet.api.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private PetRepository repository;

    @GetMapping
    public ResponseEntity<List<DadosDetalhesPetDto>> listarTodosDisponiveis() {
        List<Pet> pets = repository.findAllByAdotadoFalse();
        List<DadosDetalhesPetDto> disponiveis = new ArrayList<>();
        for (Pet pet : pets) {
            if (pet.getAdotado() == false) {
                disponiveis.add(new DadosDetalhesPetDto(pet));
            }
        }
        return ResponseEntity.ok(disponiveis);
    }

}
