package br.com.sanara.adopet.api.controller;

import br.com.sanara.adopet.api.dto.AbrigoDto;
import br.com.sanara.adopet.api.dto.PetDto;
import br.com.sanara.adopet.api.model.Abrigo;
import br.com.sanara.adopet.api.model.Pet;
import br.com.sanara.adopet.api.repository.AbrigoRepository;
import br.com.sanara.adopet.api.service.AbrigoService;
import br.com.sanara.adopet.api.service.PetService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/abrigos")
public class AbrigoController {

    @Autowired
    private AbrigoRepository repository;

    @Autowired
    private AbrigoService abrigoService;

    @Autowired
    private PetService petService;

    @GetMapping
    public ResponseEntity<List<AbrigoDto>> listar() {

        /* metodo antes da refatoracao
        List<Abrigo> abrigos = abrigoService.findAll();

        // Transforma cada Abrigo em AbrigoDto e coleta em uma lista
        List<AbrigoDto> dtos = abrigos.stream()
                .map(AbrigoDto::new)
                .toList();

        // Retorna a lista de DTOs no corpo da resposta
        return ResponseEntity.ok(dtos);*/

        List<AbrigoDto> abrigos = abrigoService.listar();
        return ResponseEntity.ok(abrigos);

    }

    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrar(@RequestBody @Valid AbrigoDto abrigoDto) {

        boolean nomeJaCadastrado = repository.existsByNome(abrigoDto.nome());
        boolean telefoneJaCadastrado = repository.existsByTelefone(abrigoDto.telefone());
        boolean emailJaCadastrado = repository.existsByEmail(abrigoDto.email());

        if (nomeJaCadastrado || telefoneJaCadastrado || emailJaCadastrado) {
            return ResponseEntity.badRequest().body("Dados já cadastrados para outro abrigo!");
        } else {

            Abrigo abrigo = new Abrigo(abrigoDto.nome(), abrigoDto.email(), abrigoDto.telefone());

            repository.save(abrigo);
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/{idOuNome}/pets")
    public ResponseEntity<List<PetDto>> listarPets(@PathVariable String idOuNome) {
        try {
            Long id = Long.parseLong(idOuNome);
            List<Pet> pets = repository.getReferenceById(id).getPets();

            List<PetDto> petsDto = new ArrayList<>();
            for (Pet pet : pets) {
                petsDto.add(new PetDto(pet));
            }
            return ResponseEntity.ok(petsDto);
        } catch (EntityNotFoundException enfe) {
            return ResponseEntity.notFound().build();
        } catch (NumberFormatException e) {
            try {
                List<Pet> pets = repository.findByNome(idOuNome).getPets();
                List<PetDto> petsDto = new ArrayList<>();
                for (Pet pet : pets) {
                    petsDto.add(new PetDto(pet));
                }
                return ResponseEntity.ok(petsDto);
            } catch (EntityNotFoundException enfe) {
                return ResponseEntity.notFound().build();
            }
        }
    }

    @PostMapping("/{idOuNome}/pets")
    @Transactional
    public ResponseEntity<String> cadastrarPet(@PathVariable String idOuNome, @RequestBody @Valid PetDto petDto) {
        try {
            Long id = Long.parseLong(idOuNome);
            Abrigo abrigo = repository.getReferenceById(id);
            Pet pet = new Pet(petDto.tipo(), petDto.nome(), petDto.cor(), petDto.raca(), petDto.idade(), petDto.peso());
            pet.setAbrigo(abrigo);
            pet.setAdotado(false);

            abrigo.getPets().add(pet);
            repository.save(abrigo);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException enfe) {
            return ResponseEntity.notFound().build();
        } catch (NumberFormatException nfe) {
            try {
                Abrigo abrigo = repository.findByNome(idOuNome);
                Pet pet = new Pet(petDto.tipo(), petDto.nome(), petDto.cor(), petDto.raca(), petDto.idade(), petDto.peso());
                pet.setAbrigo(abrigo);
                pet.setAdotado(false);
                abrigo.getPets().add(pet);

                repository.save(abrigo);
                return ResponseEntity.ok().build();
            } catch (EntityNotFoundException enfe) {
                return ResponseEntity.notFound().build();
            }
        }
    }

}
