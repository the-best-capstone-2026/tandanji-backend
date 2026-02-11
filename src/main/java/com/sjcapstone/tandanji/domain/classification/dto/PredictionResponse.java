package com.sjcapstone.tandanji.domain.classification.dto;

public record PredictionResponse(
        String category, // Food 11개 카테고리
        Double confidence, // 예측 확률
        String message // 사용자에게 보여줄 메시지
) {
}
