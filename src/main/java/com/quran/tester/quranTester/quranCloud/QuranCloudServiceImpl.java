package com.quran.tester.quranTester.quranCloud;

import com.quran.tester.quranTester.quranCloud.dto.AyahResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@Slf4j
@Service
@RequiredArgsConstructor
public class QuranCloudServiceImpl implements QuranCloudService{

    private final WebClient quranCloudWebClient;

    @Override
    public Mono<AyahResponseDTO> getAyah(String ayahNumber) {
        log.warn("QuranCloudServiceImpl hit: " + ayahNumber);
        return quranCloudWebClient
                .get()
                .uri("/v1/ayah/" + ayahNumber)
                .retrieve()
                .bodyToMono(AyahResponseDTO.class)
                .doOnError(x -> log.error("Error -> " + x));
    }
}
