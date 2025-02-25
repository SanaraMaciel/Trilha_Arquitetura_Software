package br.com.sanara.adopet.api.dto;


import br.com.sanara.adopet.api.model.TipoPet;


public record CadastroPetDto(TipoPet tipo, String nome, String raca, Integer idade, String cor, Float peso,
                             Boolean adotado) {
}
