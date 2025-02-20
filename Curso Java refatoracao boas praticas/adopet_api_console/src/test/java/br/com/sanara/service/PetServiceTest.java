package br.com.sanara.service;

import br.com.sanara.client.ClientHttpConfiguration;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.http.HttpResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class PetServiceTest {

    private ClientHttpConfiguration client = mock(ClientHttpConfiguration.class);
    private PetService petService = new PetService(client);
    private HttpResponse<String> response = mock(HttpResponse.class);


    @Test
    public void deveVerificarSeDispararRequisicaoPostSeraChamado() throws IOException, InterruptedException {
        String userInput = String.format("teste%spets.csv",
                System.lineSeparator());
        ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(bais);

        when(client.dispararRequisicaoPost(anyString(), any())).thenReturn(response);

        petService.importarPetsDoAbrigo();
        verify(client.dispararRequisicaoPost(anyString(), anyString()), atLeast(1));
    }
}
