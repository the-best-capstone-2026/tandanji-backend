package com.sjcapstone.tandanji.src.service;

import com.sjcapstone.tandanji.src.dto.PredictionResponse;
import com.sjcapstone.tandanji.src.entity.Food;
import com.sjcapstone.tandanji.src.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service // 이 클래스가 서비스 계층임을 선언합니다.
@Transactional(readOnly = true) // 기본적으로 읽기 전용으로 설정하여 성능을 높입니다.
@RequiredArgsConstructor // final이 붙은 필드(레포지토리)를 자동으로 연결해줍니다.
public class FoodService {

    private final FoodRepository foodRepository; // 레포지토리를 불러옵니다.
    /**
     * 음식 정보 저장 (예측 결과 저장)
     */
    // FoodService.java
    @Transactional
    public PredictionResponse classifyAndSave(MultipartFile image) { // 1. 메서드명 변경 및 파라미터 수정
        // 2. 예외 처리 (IllegalArgumentException으로 변경)
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("업로드된 이미지 파일이 없습니다.");
        }

        // 3. 분석 로직 (가짜 데이터로 구현)
        String analyzedCategory = "치킨";
        Double confidence = 0.98;

        // 4. DB 저장
        Food food = new Food(analyzedCategory);
        foodRepository.save(food);

        // 5. 결과 반환 (DTO 활용)
        return new PredictionResponse(
                analyzedCategory,
                confidence,
                "음식이 성공적으로 분석 및 저장되었습니다."
        );
    }
}