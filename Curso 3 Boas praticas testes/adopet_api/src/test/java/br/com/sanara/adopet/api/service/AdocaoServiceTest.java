package br.com.sanara.adopet.api.service;

import br.com.sanara.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.sanara.adopet.api.model.Abrigo;
import br.com.sanara.adopet.api.model.Adocao;
import br.com.sanara.adopet.api.model.Pet;
import br.com.sanara.adopet.api.model.Tutor;
import br.com.sanara.adopet.api.repository.AdocaoRepository;
import br.com.sanara.adopet.api.repository.PetRepository;
import br.com.sanara.adopet.api.repository.TutorRepository;
import br.com.sanara.adopet.api.validacoes.ValidacaoSolicitacaoAdocao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AdocaoServiceTest {

    @InjectMocks
    private AdocaoService service;

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private EmailService emailService;

    @Spy
    //simula um obj real que será de fato instanciado o new ArrayList<> obriga o mock a instanciar o obj do tipo java
    private List<ValidacaoSolicitacaoAdocao> validacoes = new ArrayList<>();

    //cria os validadores pra poder adicionar eles dentro da lista de validacoes acima
    @Mock
    private ValidacaoSolicitacaoAdocao validador1;

    @Mock
    private ValidacaoSolicitacaoAdocao validador2;

    @Mock
    private Pet pet;

    @Mock
    private Tutor tutor;

    @Mock
    private Abrigo abrigo;

    private SolicitacaoAdocaoDto dto;

    @Captor // anotacao usada para capturar um argumento que esteja sendo passado para o mock
    private ArgumentCaptor<Adocao> adocaoCaptor;


    @Test
    void deveriaSalvarAdocaoAoSolicitar() {

        //ARRANGE
        //instancia o dto antes de passar ele no given
        this.dto = new SolicitacaoAdocaoDto(10l, 20l, "motivo qualquer");

        given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(dto.idPet())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);


        //ACT
        service.solicitar(dto);

        //ASSERT
        //verifica se esta salvando o objeto que eu estou chamando para fazer o save
        //adocaoCaptor.capture() : captura um objeto que esta sendo passado como parametro para o mock
        then(adocaoRepository).should().save(adocaoCaptor.capture());

        //pega a adocao que foi salva no metodo save
        Adocao adocaoSalva = adocaoCaptor.getValue();

        //outro ASSERT verificar se salvou corretamente o obj
        assertEquals(pet, adocaoSalva.getPet());
        assertEquals(tutor, adocaoSalva.getTutor());
        assertEquals(dto.motivo(), adocaoSalva.getMotivo());

    }

    @Test
    void deveriaChamarValidadoresDeAdocaoAoSolicitar() {
        //ARRANGE
        this.dto = new SolicitacaoAdocaoDto(10l, 20l, "motivo qualquer");
        given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);

        validacoes.add(validador1);
        validacoes.add(validador2);

        //ACT
        service.solicitar(dto);

        //ASSERT para verificar se os validadores estao sendo chamados no metodo validar
        BDDMockito.then(validador1).should().validar(dto);
        BDDMockito.then(validador2).should().validar(dto);
    }

    void testarVariasInstanciasNoMesmoAssert() {
        then(adocaoRepository).should().save(adocaoCaptor.capture());
        Adocao adocaoSalva = adocaoCaptor.getValue();
        assertAll(
                () -> assertEquals(pet, adocaoSalva.getPet()),
                () -> assertEquals(tutor, adocaoSalva.getTutor()),
                () -> assertEquals(dto.motivo(), adocaoSalva.getMotivo())
        );
    }


}