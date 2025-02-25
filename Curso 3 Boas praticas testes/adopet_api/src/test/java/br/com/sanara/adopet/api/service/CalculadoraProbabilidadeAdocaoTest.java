package br.com.sanara.adopet.api.service;

import br.com.sanara.adopet.api.dto.CadastroAbrigoDto;
import br.com.sanara.adopet.api.dto.CadastroPetDto;
import br.com.sanara.adopet.api.model.Abrigo;
import br.com.sanara.adopet.api.model.Pet;
import br.com.sanara.adopet.api.model.ProbabilidadeAdocao;
import br.com.sanara.adopet.api.model.TipoPet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CalculadoraProbabilidadeAdocaoTest {

    @Test
    @DisplayName("Probabilidade deveria ser alta para gatos jovens com peso baixo") // anotacao usada para indicar o que o teste faz sem precisar dar um nome muito extenso ao metodo
    void deveriaRetornarProbabilidadeAltaParaPetComIdadeBaixaEPesoBaixo() {
        //idade 4 anos e 4kg - ALTA
        // padrão de teste triple A arrange, act e assert

        //Arrange
        Abrigo abrigo = new Abrigo(new CadastroAbrigoDto(
                "Abrigo feliz",
                "94999999999",
                "abrigofeliz@email.com.br"
        ));
        Pet pet = new Pet(new CadastroPetDto(
                TipoPet.GATO,
                "Miau",
                "Siames",
                4,
                "Cinza",
                4.0f
        ), abrigo);

        CalculadoraProbabilidadeAdocao calculadora = new CalculadoraProbabilidadeAdocao();

        //act
        ProbabilidadeAdocao probabilidade = calculadora.calcular(pet);

        //Assert
        Assertions.assertEquals(ProbabilidadeAdocao.ALTA, probabilidade);

    }

    @Test
    @DisplayName("Probabilidade deveria ser média para gatos idosos com peso baixo")
    void deveriaRetornarProbabilidadeMediaParaPetComIdadeAltaEPesoBaixo() {
        //idade 15 anos e 4kg - MEDIA

        Abrigo abrigo = new Abrigo(new CadastroAbrigoDto(
                "Abrigo feliz",
                "94999999999",
                "abrigofeliz@email.com.br"
        ));
        Pet pet = new Pet(new CadastroPetDto(
                TipoPet.GATO,
                "Miau",
                "Siames",
                15,
                "Cinza",
                4.0f
        ), abrigo);

        CalculadoraProbabilidadeAdocao calculadora = new CalculadoraProbabilidadeAdocao();
        ProbabilidadeAdocao probabilidade = calculadora.calcular(pet);

        Assertions.assertEquals(ProbabilidadeAdocao.MEDIA, probabilidade);
    }

}