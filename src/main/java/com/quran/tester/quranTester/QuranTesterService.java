package com.quran.tester.quranTester;

import com.quran.tester.quranTester.quranCloud.dto.AyahResponseDTO;
import reactor.core.publisher.Mono;

public interface QuranTesterService {
    Mono<AyahResponseDTO> getAyah(int juzNumber);
}
