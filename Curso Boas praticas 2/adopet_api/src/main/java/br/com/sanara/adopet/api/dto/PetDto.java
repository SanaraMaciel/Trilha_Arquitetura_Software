package br.com.sanara.adopet.api.dto;

import br.com.sanara.adopet.api.model.Pet;
import br.com.sanara.adopet.api.model.TipoPet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PetDto(@NotNull TipoPet tipo, @NotBlank String nome, @NotBlank String raca, @NotNull Integer idade,
                     @NotBlank String cor, @NotNull Float peso) {

    public PetDto(Pet pet) {
        this(pet.getTipo(), pet.getNome(), pet.getRaca(), pet.getIdade(), pet.getCor(), pet.getPeso());
    }
}
