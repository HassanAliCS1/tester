package com.quran.tester.quranTester;

import com.quran.tester.quranTester.quranCloud.QuranCloudService;
import com.quran.tester.quranTester.quranCloud.dto.AyahResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Random;

@Slf4j
@Service
public class QuranTesterServiceImpl implements QuranTesterService {

    private QuranCloudService svc;

    public QuranTesterServiceImpl(QuranCloudService svc) {
        this.svc = svc;
    }

    @Override
    public Mono<AyahResponseDTO> getAyah(String juzNumber) {
        //TODO: logic to turn juz number to ayahnumber format:(surah:ayah)
        log.warn("juznumber: " + juzNumber);

        int minSurah = 1;
        int maxSurah = 2;
        int minAyah = 1;
        int maxAyah = 255;

//        String ayahNumber = (getRandomNumberUsingNextInt(minSurah, maxSurah).toString + ":" + getRandomNumberUsingNextInt(minAyah, maxAyah).toString);
        String ayahNumber = (maxSurah+":"+maxAyah);
        log.warn("ayahNumber: " + ayahNumber);
        return svc.getAyah(ayahNumber);
    }

    private int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
}
