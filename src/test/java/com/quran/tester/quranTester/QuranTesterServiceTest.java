package com.quran.tester.quranTester;

import com.quran.tester.quranTester.quranCloud.QuranCloudService;
import com.quran.tester.quranTester.quranCloud.dto.AyahDataDTO;
import com.quran.tester.quranTester.quranCloud.dto.AyahResponseDTO;
import com.quran.tester.quranTester.quranCloud.dto.EditionDTO;
import com.quran.tester.quranTester.quranCloud.dto.SurahDTO;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuranTesterServiceTest {

    private QuranTesterServiceImpl svc;

    @Mock
    private QuranCloudService quranCloudService;

    @BeforeEach
    void setUp(){
        svc = new QuranTesterServiceImpl(quranCloudService);
    }

    @Test
    void getAyah(){
        when(quranCloudService.getAyah(any(String.class))).thenReturn(Mono.just(getAyahResponseDTO()));

        StepVerifier.create(svc.getAyah(30))
                .expectNext(getAyahResponseDTO())
                .verifyComplete();

        verify(quranCloudService, times(1)).getAyah(argThat(ayahNumber -> {
            int num = Integer.parseInt(ayahNumber);
            return num >= 5673 && num <= 6236;
        }));

    }

    @NotNull
    private static AyahResponseDTO getAyahResponseDTO() {
        var secondaryAudio = new ArrayList<String>();
        secondaryAudio.add("https://cdn.islamic.network/quran/audio/64/ar.alafasy/5710.mp3");
        var edition = new EditionDTO("ar.alafasy","ar","مشاريالعفاسي","Alafasy","audio","versebyverse","none");
        var surah = new SurahDTO(78,"سُورَةُالنَّبَإ","An-Naba","TheAnnouncement",40,"Meccan");
        var data = new AyahDataDTO(5710, "https://cdn.islamic.network/quran/audio/128/ar.alafasy/5710.mp3", secondaryAudio,"يَوْمَيَقُومُٱلرُّوحُوَٱلْمَلَٰٓئِكَةُصَفًّۭاۖلَّايَتَكَلَّمُونَإِلَّامَنْأَذِنَلَهُٱلرَّحْمَٰنُوَقَالَصَوَابًۭا", edition,surah,38,30,7,583,519,233,false);
        return new AyahResponseDTO(200,"OK",data);
    }

}
