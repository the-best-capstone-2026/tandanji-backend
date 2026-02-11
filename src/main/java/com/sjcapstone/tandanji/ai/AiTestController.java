package com.sjcapstone.tandanji.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/ai/test")
@RequiredArgsConstructor
public class AiTestController {

    private final AiService aiService;

    @PostMapping(value = "/predict-test", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> predictTest(@RequestPart("file") MultipartFile file) {
        try {
            Map result = aiService.predict(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", e.getClass().getName(),
                    "message", String.valueOf(e.getMessage())
            ));
        }
    }
}
