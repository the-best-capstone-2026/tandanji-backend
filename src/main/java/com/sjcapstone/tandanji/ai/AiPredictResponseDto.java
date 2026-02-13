package com.sjcapstone.tandanji.ai;

public record AiPredictResponseDto(
        String filename,
        String label,
        Double confidence
) {
}
