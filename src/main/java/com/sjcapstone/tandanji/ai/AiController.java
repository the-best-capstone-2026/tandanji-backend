package com.sjcapstone.tandanji.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping(value = "/predict", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> predict(@RequestPart("file") MultipartFile file) {
        try {
            AiPredictResponseDto result = aiService.predict(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // 🔥 여기 추가
            log.error("AI 호출 중 예외 발생", e);

            return ResponseEntity.status(500).body(
                    new ErrorResponseDto(
                            e.getClass().getName(),
                            String.valueOf(e.getMessage())
                    )
            );
        }
    }
}