package com.quran.tester.quranTester;

import com.quran.tester.quranTester.quranCloud.QuranCloudService;
import com.quran.tester.quranTester.quranCloud.dto.AyahResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
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
        List<Integer> resp = getAyahsFromJuz(juzNumber);
        int minAyah = resp.getFirst();
        int maxAyah = resp.getLast();
        int ayahNumber = getRandomNumberUsingNextInt(minAyah, maxAyah);

        return svc.getAyah(String.valueOf(ayahNumber));
    }

    private int getRandomNumberUsingNextInt(int min, int max) {
        return random.nextInt(max - min) + min;
        //TODO: Tech Debt: Some problems: 1.Can pick last ayah of Juz (Not good).  2.Can pick Ayahs like حمۤ (Not Good)
    }

    //TODO: is this optimal ? Find better way. while making getAyah method webflux
    public List<Integer> getAyahsFromJuz(int juz){
        List<Integer> ayahs = new ArrayList<>();

        switch (juz){
            case 1:
                ayahs.add(0,1);
                ayahs.add(1,148);
                break;
            case 2:
                ayahs.add(0,149);
                ayahs.add(1,259);
                break;
            case 3:
                ayahs.add(0,260);
                ayahs.add(1,385);
                break;
            case 4:
                ayahs.add(0,386);
                ayahs.add(1,516);
                break;
            case 5:
                ayahs.add(0,517);
                ayahs.add(1,640);
                break;
            case 6:
                ayahs.add(0,641);
                ayahs.add(1,750);
                break;
            case 7:
                ayahs.add(0,751);
                ayahs.add(1,899);
                break;
            case 8:
                ayahs.add(0,900);
                ayahs.add(1,1041);
                break;
            case 9:
                ayahs.add(0,1042);
                ayahs.add(1,1200);
                break;
            case 10:
                ayahs.add(0,1201);
                ayahs.add(1,1327);
                break;
            case 11:
                ayahs.add(0,1328);
                ayahs.add(1,1478);
                break;
            case 12:
                ayahs.add(0,1479);
                ayahs.add(1,1648);
                break;

            case 13:
                ayahs.add(0,1649);
                ayahs.add(1,1802);
                break;
            case 14:
                ayahs.add(0,1803);
                ayahs.add(1,2029);
                break;
            case 15:
                ayahs.add(0,2030);
                ayahs.add(1,2214);
                break;
            case 16:
                ayahs.add(0,2215);
                ayahs.add(1,2483);
                break;
            case 17:
                ayahs.add(0,2484);
                ayahs.add(1,2673);
                break;
            case 18:
                ayahs.add(0,2674);
                ayahs.add(1,2875);
                break;
            case 19:
                ayahs.add(0,2876);
                ayahs.add(1,3214);
                break;
            case 20:
                ayahs.add(0,3215);
                ayahs.add(1,3385);
                break;
            case 21:
                ayahs.add(0,3386);
                ayahs.add(1,3563);
                break;
            case 22:
                ayahs.add(0,3564);
                ayahs.add(1,3732);
                break;
            case 23:
                ayahs.add(0,3733);
                ayahs.add(1,4089);
                break;
            case 24:
                ayahs.add(0,4090);
                ayahs.add(1,4264);
                break;
            case 25:
                ayahs.add(0,4265);
                ayahs.add(1,4510);
                break;
            case 26:
                ayahs.add(0,4511);
                ayahs.add(1,4705);
                break;
            case 27:
                ayahs.add(0,4706);
                ayahs.add(1,5104);
                break;
            case 28:
                ayahs.add(0,5105);
                ayahs.add(1,5241);
                break;
            case 29:
                ayahs.add(0,5242);
                ayahs.add(1,5672);
                break;
            case 30:
                ayahs.add(0,5673);
                ayahs.add(1,6236);
                break;
            default:
                ayahs.add(0,1);
                ayahs.add(1,6236);
                break;
        }
        //TODO: add a default case: error ^ replace that

        return ayahs;
    }
}
