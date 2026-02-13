package com.sjcapstone.tandanji.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    private final AiClient aiClient;

    public AiPredictResponseDto predict(MultipartFile file) {
        try {
            AiPredictResponseDto result = aiClient.predict(file);
            log.info("[AI] SUCCESS filename={} result={}", file.getOriginalFilename(), result);
            return result;
        } catch (Exception e) {
            log.error("[AI] FAIL filename={} error={} message={}",
                    file.getOriginalFilename(),
                    e.getClass().getSimpleName(),
                    e.getMessage(),
                    e);
            throw e;
        }
    }
}
