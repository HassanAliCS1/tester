package com.quran.tester.quranTester.quranCloud.dto;

public record EditionDTO(
        String identifier,
        String language,
        String name,
        String englishName,
        String format,
        String type,
        String direction
) {
}
