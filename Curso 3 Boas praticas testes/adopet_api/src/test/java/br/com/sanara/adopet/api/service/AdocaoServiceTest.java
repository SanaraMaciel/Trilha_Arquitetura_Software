package br.com.sanara.adopet.api.service;

import br.com.sanara.adopet.api.dto.AprovacaoAdocaoDto;
import br.com.sanara.adopet.api.dto.ReprovacaoAdocaoDto;
import br.com.sanara.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.sanara.adopet.api.model.*;
import br.com.sanara.adopet.api.repository.AdocaoRepository;
import br.com.sanara.adopet.api.repository.PetRepository;
import br.com.sanara.adopet.api.repository.TutorRepository;
import br.com.sanara.adopet.api.validacoes.ValidacaoSolicitacaoAdocao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

/**
 * @Mock: Essa anotação é usada para criar um objeto falso (mock) de uma classe ou interface. É como se você estivesse criando um dublê
 * para simular o comportamento de um objeto real. Por exemplo, se você tem um repositório que acessa o banco de dados, você pode usar @Mock
 * para criar uma versão fictícia desse repositório, que não faz chamadas reais ao banco, mas simula as respostas que você especificar.
 * @InjectMocks: Esta anotação é usada para criar uma instância real da classe que você está testando e injetar os mocks nos atributos dessa classe.
 * Isso é útil quando a classe que você está testando depende de outras classes (que você mockou) para funcionar.
 */
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

    @Mock
    private AprovacaoAdocaoDto aprovacaoAdocaoDto;

    @Mock
    private ReprovacaoAdocaoDto reprovacaoAdocaoDto;

    @Spy
    private Adocao adocao;

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

    /**
     * esse eh um exemplo que eh possivel passar mais de um parametro ao assert assim ele faz varios testes de uma vez
     * nao interrompendo a aplicacao caso algum teste falhe
     */
    void testarVariasInstanciasNoMesmoAssert() {
        then(adocaoRepository).should().save(adocaoCaptor.capture());
        Adocao adocaoSalva = adocaoCaptor.getValue();
        assertAll(
                () -> assertEquals(pet, adocaoSalva.getPet()),
                () -> assertEquals(tutor, adocaoSalva.getTutor()),
                () -> assertEquals(dto.motivo(), adocaoSalva.getMotivo())
        );
    }

    @Test
    void deveriaEnviarEmailAoSolicitarAdocao() {

        //ARRANGE
        SolicitacaoAdocaoDto dto = new SolicitacaoAdocaoDto(10l, 30l, "motivo teste");
        given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);

        //ACT
        service.solicitar(dto);

        //ASSERT
        then(adocaoRepository).should().save(adocaoCaptor.capture());
        Adocao adocao = adocaoCaptor.getValue();
        then(emailService).should().enviarEmail(
                adocao.getPet().getAbrigo().getEmail(),
                "Solicitação de adoção",
                "Olá " + adocao.getPet().getAbrigo().getNome() + "!\n\nUma solicitação de adoção foi registrada hoje para o pet: " + adocao.getPet().getNome() + ". \nFavor avaliar para aprovação ou reprovação."
        );
    }

    @Test
    void deveriaAprovarUmaAdocao() {

        //Arrange
        given(adocaoRepository.getReferenceById(aprovacaoAdocaoDto.idAdocao())).willReturn(adocao);
        given(adocao.getPet()).willReturn(pet);
        given(pet.getAbrigo()).willReturn(abrigo);
        given(abrigo.getEmail()).willReturn("email@example.com");
        given(adocao.getTutor()).willReturn(tutor);
        given(tutor.getNome()).willReturn("Rodrigo");
        given(adocao.getData()).willReturn(LocalDateTime.now());


        //Act
        service.aprovar(aprovacaoAdocaoDto);


        //Assert
        then(adocao).should().marcarComoAprovada();
        assertEquals(StatusAdocao.APROVADO, adocao.getStatus());
    }

    @Test
    void deveriaEnviarEmailAoAprovarUmaAdocao() {

        //Arrange
        given(adocaoRepository.getReferenceById(aprovacaoAdocaoDto.idAdocao())).willReturn(adocao);
        given(adocao.getPet()).willReturn(pet);
        given(pet.getAbrigo()).willReturn(abrigo);
        given(abrigo.getEmail()).willReturn("email@example.com");
        given(adocao.getTutor()).willReturn(tutor);
        given(tutor.getNome()).willReturn("Rodrigo");
        given(adocao.getData()).willReturn(LocalDateTime.now());

        //Act
        service.aprovar(aprovacaoAdocaoDto);

        //Assert

        then(emailService).should().enviarEmail(
                adocao.getPet().getAbrigo().getEmail(),
                "Adoção aprovada",
                "Parabéns " + adocao.getTutor().getNome() + "!\n\nSua adoção do pet " + adocao.getPet().getNome() + ", solicitada em " + adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + ", foi aprovada.\nFavor entrar em contato com o abrigo " + adocao.getPet().getAbrigo().getNome() + " para agendar a busca do seu pet."
        );
    }


    @Test
    void deveriaReprovarUmaAdocao() {
        //Arrange
        given(adocaoRepository.getReferenceById(aprovacaoAdocaoDto.idAdocao())).willReturn(adocao);
        given(adocao.getPet()).willReturn(pet);
        given(pet.getAbrigo()).willReturn(abrigo);
        given(abrigo.getEmail()).willReturn("email@example.com");
        given(adocao.getTutor()).willReturn(tutor);
        given(tutor.getNome()).willReturn("Rodrigo");
        given(adocao.getData()).willReturn(LocalDateTime.now());

        //Act
        service.reprovar(reprovacaoAdocaoDto);


        //
        then(adocao).should().marcarComoReprovada(reprovacaoAdocaoDto.justificativa());
        assertEquals(StatusAdocao.REPROVADO, adocao.getStatus());
    }

    @Test
    void deveriaEnviarEmailAoReprovarUmaAdocao() {
        //Arrange
        given(adocaoRepository.getReferenceById(aprovacaoAdocaoDto.idAdocao())).willReturn(adocao);
        given(adocao.getPet()).willReturn(pet);
        given(pet.getAbrigo()).willReturn(abrigo);
        given(abrigo.getEmail()).willReturn("email@example.com");
        given(adocao.getTutor()).willReturn(tutor);
        given(tutor.getNome()).willReturn("Rodrigo");
        given(adocao.getData()).willReturn(LocalDateTime.now());

        //Act
        service.reprovar(reprovacaoAdocaoDto);

        //Assert

        then(emailService).should().enviarEmail(
                adocao.getPet().getAbrigo().getEmail(),
                "Solicitação de adoção",
                "Olá " + adocao.getTutor().getNome() + "!\n\nInfelizmente sua adoção do pet " + adocao.getPet().getNome() + ", solicitada em " + adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + ", foi reprovada pelo abrigo " + adocao.getPet().getAbrigo().getNome() + " com a seguinte justificativa: " + adocao.getJustificativaStatus()
        );
    }


}