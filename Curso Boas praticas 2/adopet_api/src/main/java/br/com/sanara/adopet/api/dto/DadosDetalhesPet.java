package br.com.sanara.adopet.api.dto;

import br.com.sanara.adopet.api.model.Pet;
import br.com.sanara.adopet.api.model.TipoPet;

public record DadosDetalhesPet(Long id, TipoPet tipo, String nome, String raca, Integer idade) {

    public DadosDetalhesPet(Pet pet) {
        this(pet.getId(), pet.getTipo(), pet.getNome(), pet.getRaca(), pet.getIdade());
    }
}