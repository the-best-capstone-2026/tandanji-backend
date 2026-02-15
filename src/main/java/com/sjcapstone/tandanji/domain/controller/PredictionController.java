package com.sjcapstone.tandanji.domain.controller;

import com.sjcapstone.tandanji.domain.dto.PredictionResponse;
import com.sjcapstone.tandanji.domain.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/predict")
@RequiredArgsConstructor
public class PredictionController {

    private final FoodService foodService;

    @PostMapping
    public ResponseEntity<PredictionResponse> predict(@RequestParam("image") MultipartFile image) {
        try {
            // 1. 서비스 호출 (이미지 분석 + 저장)
            PredictionResponse response = foodService.classifyAndSave(image);

            // 2. DTO(PredictionResponse)를 직접 반환
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // 잘못된 파일 업로드 처리
            return ResponseEntity.badRequest().body(new PredictionResponse(null, 0.0, e.getMessage()));
        } catch (Exception e) {
            // 서버 내부 오류 처리
            return ResponseEntity.internalServerError().body(new PredictionResponse(null, 0.0, "서버 오류: " + e.getMessage()));
        }
    }
}