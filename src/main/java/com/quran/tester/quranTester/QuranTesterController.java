package com.quran.tester.quranTester;

import com.quran.tester.quranTester.quranCloud.dto.AyahResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
@Slf4j
@RestController
@RequestMapping("/quran-tester")
public class QuranTesterController {
    //TODO: add swagger

    private final QuranTesterService svc;

    public QuranTesterController(QuranTesterService svc) {
        this.svc = svc;
    }

    @GetMapping("/get-ayah")
    @ResponseStatus(HttpStatus.OK)
    public Mono<AyahResponseDTO> getAyah(int juzNumber){
        log.warn("QuranTesterController hit: "+ juzNumber);
        return svc.getAyah(juzNumber);
    }
}
