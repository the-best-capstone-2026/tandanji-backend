package com.sjcapstone.tandanji.ai;

import java.util.List;

public record AiPredictResponseDto(
        String label,
        Double confidence,
        List<TopKItem> topK
) {
    public record TopKItem(
            String label,
            Double prob
    ) {}
}