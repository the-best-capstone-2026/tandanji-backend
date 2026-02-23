package com.sjcapstone.tandanji.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

// [추가] 실패 시 503 반환을 위해 import 추가
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    private final AiClient aiClient;

    public AiPredictResponseDto predict(MultipartFile file) {
        try {
            AiPredictResponseDto result = aiClient.predict(file);

            log.info("[AI] SUCCESS filename={} result={}",
                    file.getOriginalFilename(),
                    result);

            // [추가(선택)] result 자체가 null이면 실패로 처리
            // (원래는 여기서 null을 그대로 return할 수도 있는데, 설계상 실패로 보는 게 안전)
            if (result == null) {
                throw new ResponseStatusException(
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "AI 응답이 비어있음"
                );
            }

            return result;

            // [추가] 이미 503 같은 상태가 정해진 예외면 그대로 통과
        } catch (ResponseStatusException e) {
            throw e;

        } catch (Exception e) {

            log.error("[AI] FAIL filename={} error={} message={}",
                    file.getOriginalFilename(),
                    e.getClass().getSimpleName(),
                    e.getMessage(),
                    e);

            // [삭제] 실패를 "성공 응답처럼" 포장하던 fallback DTO 반환
            // return new AiPredictResponseDto(file.getOriginalFilename(), null, 0.0);

            // [추가] 팀장 요구: 실패면 200이 아니라 503으로 반환
            // "예외 던지면 서버 죽음" X → 해당 요청만 503으로 끝나고 서버는 계속 살아있음
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "AI 서버 호출 실패",
                    e
            );
        }
    }
}