package com.quran.tester.quranTester.quranCloud;

import com.quran.tester.quranTester.quranCloud.dto.AyahDataDTO;
import com.quran.tester.quranTester.quranCloud.dto.AyahResponseDTO;
import com.quran.tester.quranTester.quranCloud.dto.EditionDTO;
import com.quran.tester.quranTester.quranCloud.dto.SurahDTO;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Slf4j
@SpringBootTest
class QuranCloudPactTest {

    private final QuranCloudServiceImpl svc;

    QuranCloudPactTest() {
        this.svc = new QuranCloudServiceImpl(
                WebClient
                        .builder()
                        .baseUrl("http://api.alquran.cloud")
                        .defaultHeader(ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .defaultHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .build()
        );
    }

    @Test
    void testGetAyah(){
        StepVerifier.create(svc.getAyah("6081"))
                .consumeNextWith(resp ->
                    assertEquals(getAyahResponseDTO(), resp, "API Response error (may have changed)")
                )
                .verifyComplete();
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
