package com.quran.tester.quranTester.quranCloud;

import com.quran.tester.quranTester.quranCloud.dto.AyahDataDTO;
import com.quran.tester.quranTester.quranCloud.dto.AyahResponseDTO;
import com.quran.tester.quranTester.quranCloud.dto.EditionDTO;
import com.quran.tester.quranTester.quranCloud.dto.SurahDTO;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import mockwebserver3.junit5.internal.MockWebServerExtension;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockWebServerExtension.class)
class QuranCloudServiceTest {

    private QuranCloudServiceImpl svc;

    private MockWebServer server;
    @BeforeEach
    void setup(MockWebServer server) {
        this.server = server;
        this.svc = new QuranCloudServiceImpl(WebClient.create(server.url("/").toString()));
    }

    @Test
    void getAyah() throws InterruptedException {
        server.enqueue(
                new MockResponse()
                        .newBuilder()
                        .code(HttpStatus.OK.value())
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .body(responseJson())
                        .build()
        );

        StepVerifier.create(svc.getAyah("6081"))
                .expectNext(getAyahResponseDTO())
                .verifyComplete();

        var request = server.takeRequest();
        assertEquals(HttpMethod.GET.name(), request.getMethod());
        assertEquals("/v1/ayah/6081/ar.alafasy", request.getPath());
    }

    private String responseJson(){
        return """
                {
                    "code": 200,
                    "status": "OK",
                    "data": {
                        "number": 6081,
                        "audio": "https://cdn.islamic.network/quran/audio/128/ar.alafasy/6081.mp3",
                        "audioSecondary": [
                            "https://cdn.islamic.network/quran/audio/64/ar.alafasy/6081.mp3"
                        ],
                        "text": "وَٱلَّيْلِ إِذَا سَجَىٰ",
                        "edition": {
                            "identifier": "ar.alafasy",
                            "language": "ar",
                            "name": "مشاري العفاسي",
                            "englishName": "Alafasy",
                            "format": "audio",
                            "type": "versebyverse",
                            "direction": null
                        },
                        "surah": {
                            "number": 93,
                            "name": "سُورَةُ الضُّحَىٰ",
                            "englishName": "Ad-Dhuhaa",
                            "englishNameTranslation": "The Morning Hours",
                            "numberOfAyahs": 11,
                            "revelationType": "Meccan"
                        },
                        "numberInSurah": 2,
                        "juz": 30,
                        "manzil": 7,
                        "page": 596,
                        "ruku": 535,
                        "hizbQuarter": 238,
                        "sajda": false
                    }
                }
                """;
    }

    @NotNull
    private static AyahResponseDTO getAyahResponseDTO() {
        var secondaryAudio = new ArrayList<String>();
        secondaryAudio.add("https://cdn.islamic.network/quran/audio/64/ar.alafasy/6081.mp3");
        var edition = new EditionDTO("ar.alafasy","ar","مشاري العفاسي","Alafasy","audio","versebyverse",null);
        var surah = new SurahDTO(93,"سُورَةُ الضُّحَىٰ","Ad-Dhuhaa","The Morning Hours",11,"Meccan");
        var data = new AyahDataDTO(6081, "https://cdn.islamic.network/quran/audio/128/ar.alafasy/6081.mp3", secondaryAudio,"وَٱلَّيْلِ إِذَا سَجَىٰ", edition,surah,2,30,7,596,535,238,false);
        return new AyahResponseDTO(200,"OK",data);
    }
}
