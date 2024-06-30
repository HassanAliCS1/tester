package com.quran.tester.quranTester;

import com.quran.tester.quranTester.quranCloud.QuranCloudService;
import com.quran.tester.quranTester.quranCloud.dto.AyahResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
public class QuranTesterServiceImpl implements QuranTesterService {

    private final QuranCloudService svc;
    private final Random random = new Random();

    public QuranTesterServiceImpl(QuranCloudService svc) {
        this.svc = svc;
    }

    @Override
    public Mono<AyahResponseDTO> getAyah(int juzNumber) {
        //TODO: should this be webflux? (Find an optimal way of getAyahsFromJuz logic with webflux maybe)
        Integer[] resp = getAyahsFromJuz(juzNumber);
        int minAyah = resp[0];
        int maxAyah = resp[1];
        int ayahNumber = getRandomNumberUsingNextInt(minAyah, maxAyah);

        return svc.getAyah(String.valueOf(ayahNumber));
    }

    private int getRandomNumberUsingNextInt(int min, int max) {
        return random.nextInt(max - min) + min;
        //TODO: Tech Debt: Some problems: 1.Can pick last ayah of Juz (Not good).  2.Can pick Ayahs like حمۤ (Not Good)
    }

    //TODO: is this optimal ? Find better way. while making getAyah method webflux
    private Integer[] getAyahsFromJuz(int juz){
        if (juz >= 1 && juz <= 10){
            return ayahRangeOneLookUp.get(juz);
        }
        else if (juz >= 11 && juz <= 20){
            return ayahRangeTwoLookUp.get(juz);
        }
        else if (juz >= 21 && juz <= 30){
            return ayahRangeThreeLookUp.get(juz);
        }
        return null;
    }

    static Map<Integer, Integer[]> ayahRangeOneLookUp = Map.of(
            1, new Integer[]{1, 148},
            2, new Integer[]{149,259},
            3, new Integer[]{260,385},
            4, new Integer[]{386,516},
            5, new Integer[]{517,640},
            6, new Integer[]{641,750},
            7, new Integer[]{751,899},
            8, new Integer[]{900,1041},
            9, new Integer[]{1042,1200},
            10, new Integer[]{1201,1327}
    );
    static Map<Integer, Integer[]> ayahRangeTwoLookUp = Map.of(
            11, new Integer[]{1328,1478},
            12, new Integer[]{1479,1648},
            13, new Integer[]{1649,1802},
            14, new Integer[]{1803,2029},
            15, new Integer[]{2030,2214},
            16, new Integer[]{2215,2483},
            17, new Integer[]{2484,2673},
            18, new Integer[]{2674,2875},
            19, new Integer[]{2876,3214},
            20, new Integer[]{3215,3385}
    );

    static Map<Integer, Integer[]> ayahRangeThreeLookUp = Map.of(
            21, new Integer[]{3386,3563},
            22, new Integer[]{3564,3732},
            23, new Integer[]{3733,4089},
            24, new Integer[]{4090,4264},
            25, new Integer[]{4265,4510},
            26, new Integer[]{4511,4705},
            27, new Integer[]{4706,5104},
            28, new Integer[]{5105,5241},
            29, new Integer[]{5242,5672},
            30, new Integer[]{5673,6236}
    );
}
