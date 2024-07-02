package com.quran.tester.quranTester;

import com.quran.tester.config.SecurityConfig;
import com.quran.tester.config.TracingConfig;
import com.quran.tester.quranTester.quranCloud.dto.AyahDataDTO;
import com.quran.tester.quranTester.quranCloud.dto.AyahResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;


import static org.mockito.Mockito.*;

@WebFluxTest
@AutoConfigureWebTestClient(timeout = "60000")
@Import({ SecurityConfig.class, TracingConfig.class })
class QuranTesterControllerTest {

    @Autowired
    protected WebTestClient client;

    @MockBean
    private QuranTesterService svc;

    @Test
    void getRandomAyah(){
        var data = new AyahDataDTO(1, "None", null,"None", null,null,1,1,1,1,1,1,false);
        var expectedResponse = new AyahResponseDTO(200,"OK", data);

        when(svc.getRandomAyah(30)).thenReturn(Mono.just(expectedResponse));

        client.get()
                .uri("/quran-tester/get-random-ayah?juzNumber=30")
                .exchange()
                .expectBody(AyahResponseDTO.class)
                .isEqualTo(expectedResponse);

        verify(svc, times(1)).getRandomAyah(30);
        verifyGet("/quran-tester/get-random-ayah?juzNumber=30");

    }


    protected void verifyGet(String uri) {
        client
                .get()
                .uri(uri)
                .exchange()
                .expectStatus().isOk();
    }
}
