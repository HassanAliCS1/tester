package com.quran.tester.quranTester.quranCloud.dto;

public record SurahDTO (
        int number,
        String name,
        String englishName,
        String englishNameTranslation,
        int numberOfAyahs,
        String revelationType
){
}
