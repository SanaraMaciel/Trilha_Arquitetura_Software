package br.com.sanara.adopet.api.validacoes;

import br.com.sanara.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.sanara.adopet.api.exception.ValidacaoException;
import br.com.sanara.adopet.api.model.Pet;
import br.com.sanara.adopet.api.repository.PetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.BDDMockito;


@ExtendWith(MockitoExtension.class) //diz ao mockito que ele deve rodar o JUnit junto com o mockito
class ValidacaoPetDisponivelTest {

    //declaramos o validador como um atributo InjectMocks, com isso,
    //o Mockito sabe que deve pegar esse mock e injeta-lo automaticamente no validador de pet.
    @InjectMocks
    ValidacaoPetDisponivel validacao;

    @Mock
    private PetRepository petRepository;

    @Mock
    private Pet pet;

    @Mock
    private SolicitacaoAdocaoDto dto;

    @Test
    void deveriaPermitirSolicitacaoDeAdocaoPet() {

        //ARRANGE
        BDDMockito.given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        BDDMockito.given(pet.getAdotado()).willReturn(false);

        //ASSERT + ACT
        Assertions.assertDoesNotThrow(() -> validacao.validar(dto));
    }


    @Test
    void naoDeveriaPermitirSolicitacaoDeAdocaoPet() {

        // ARRANGE
        BDDMockito.given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        BDDMockito.given(pet.getAdotado()).willReturn(false);

        //ASSERT + ACT
        Assertions.assertThrows(ValidacaoException.class,() -> validacao.validar(dto));
    }
}