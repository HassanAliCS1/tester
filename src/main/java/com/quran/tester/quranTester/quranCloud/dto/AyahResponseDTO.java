package com.quran.tester.quranTester.quranCloud.dto;

public record AyahResponseDTO(
        int code,
        String status,
        AyahDataDTO data
) {
}
