package com.quran.tester.quranTester;

import com.quran.tester.quranTester.quranCloud.dto.AyahResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/quran-tester")
public class QuranTesterController {
    //TODO: add swagger

    private final QuranTesterService svc;

    public QuranTesterController(QuranTesterService svc) {
        this.svc = svc;
    }

    @GetMapping("/get-random-ayah")
    @ResponseStatus(HttpStatus.OK)
    public Mono<AyahResponseDTO> getRandomAyah(int juzNumber){
        log.warn("QuranTesterController hit getRandomAyah: "+ juzNumber);
        return svc.getRandomAyah(juzNumber);
    }

    @GetMapping("/get-ayah-multiple-juz")
    @ResponseStatus(HttpStatus.OK)
    public Mono<AyahResponseDTO> getAyahMultipleJuz(int[] juzNumber){
        log.warn("QuranTesterController hit getAyahMultipleJuz: "+ Arrays.toString(juzNumber));
        return svc.getRandomAyahMultipleJuz(juzNumber);
    }

    @GetMapping("/get-specific-ayah")
    @ResponseStatus(HttpStatus.OK)
    public Mono<AyahResponseDTO> getSpecificAyah(int ayahNumber){
        log.warn("QuranTesterController hit getSpecificAyah: "+ ayahNumber);
        return svc.getSpecificAyah(ayahNumber);
    }
}
