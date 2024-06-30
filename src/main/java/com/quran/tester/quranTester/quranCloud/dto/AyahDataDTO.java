package com.quran.tester.quranTester.quranCloud.dto;

import java.util.ArrayList;

public record AyahDataDTO(
        int number,
        String audio,
        ArrayList<String> audioSecondary,
        String text,
        EditionDTO edition,
        SurahDTO surah,
        int numberInSurah,
        int juz,
        int manzil,
        int page,
        int ruku,
        int hizbQuarter,
        boolean sajda
) {
}
