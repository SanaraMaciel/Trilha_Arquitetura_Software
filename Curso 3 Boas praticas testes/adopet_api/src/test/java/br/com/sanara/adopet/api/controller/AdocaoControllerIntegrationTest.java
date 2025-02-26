package br.com.sanara.adopet.api.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

/**
 * Essa classe eh uma implementacao na qual se dispara requisicoes reais para a API ao inves do Mock
 * porem a API precisa estar em execu��o em alguma porta.
 * A anota��o @SpringBootTest esta com o argumento webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
 * que configura o teste para usar uma porta aleatoria para realizar as chamadas HTTP reais.
 * Isso � �til, pois evita que tenhamos que escolher uma porta que j� pode estar em uso no sistema operacional.
 * O TestRestTemplate � uma classe que faz parte do m�dulo de teste do Spring Boot e permite que voc� realize solicita��es HTTP para a API
 * em execu��o durante os testes. Observe que, o metodo perform() do MockMvc, foi substitu�do pelo metodo exchange() do TestRestTemplate para
 * realizar as chamadas HTTP reais. As asser��es dos c�digos de status de resposta tamb�m foram adaptadas para usar os valores de HttpStatus.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdocaoControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void deveriaDevolverCodigo400ParaSolicitacaoDeAdocaoComErros() {
        // ARRANGE
        String json = "{}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // ACT
        ResponseEntity<Void> response = restTemplate.exchange(
                "/adocoes",
                HttpMethod.POST,
                new HttpEntity<>(json, headers),
                Void.class
        );

        // ASSERT
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deveriaDevolverCodigo200ParaSolicitacaoDeAdocaoSemErros() {
        // ARRANGE
        String json = """
                {
                    "idPet": 1,
                    "idTutor": 1,
                    "motivo": "Motivo qualquer"
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // ACT
        ResponseEntity<Void> response = restTemplate.exchange(
                "/adocoes",
                HttpMethod.POST,
                new HttpEntity<>(json, headers),
                Void.class
        );

        // ASSERT
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}