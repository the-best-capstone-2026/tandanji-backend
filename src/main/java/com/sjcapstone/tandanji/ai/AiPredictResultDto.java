package com.sjcapstone.tandanji.ai;
//(결과 1개 단위)
public record AiPredictResultDto(
        String label,
        Double confidence
) {
}
