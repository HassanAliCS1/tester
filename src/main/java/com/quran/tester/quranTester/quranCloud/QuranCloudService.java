package com.quran.tester.quranTester.quranCloud;

import com.quran.tester.quranTester.quranCloud.dto.AyahResponseDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


public interface QuranCloudService {

    Mono<AyahResponseDTO> getAyah (String ayahNumber);
}
