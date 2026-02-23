package com.sjcapstone.tandanji.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

// [추가] timeout 예외/재시도 제어를 위해 import 추가
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiClient {

    private final WebClient aiWebClient;

    public AiPredictResponseDto predict(MultipartFile file) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", file.getResource());

        return aiWebClient.post()
                .uri("/predict")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()

                // [추가] 4xx/5xx는 "응답은 왔지만 실패" → 예외로 변환
                // (이렇게 해야 재시도 필터에서도 'timeout만' 재시도하고 나머지는 바로 실패로 떨어짐)
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        resp -> resp.createException()
                )

                .bodyToMono(AiPredictResponseDto.class)

                // [삭제] 모든 예외(400/500 포함) 재시도하는 retry(1)
                // .retry(1)

                // [추가] timeout일 때만 1회 재시도
                .retryWhen(
                        Retry.fixedDelay(1, Duration.ofMillis(300)) // 1회 재시도 + 짧은 딜레이
                                .filter(this::isTimeoutLike)        // "timeout만" 통과
                                .onRetryExhaustedThrow((spec, signal) -> signal.failure()) // 재시도 다 쓰면 원래 예외 던짐
                )
                .block();
    }

    // /api/health에서 AI 서버 살아있는지 확인용 (기존 그대로)
    public void checkHealth() {
        aiWebClient.get()
                .uri("/docs")
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    // [추가] "timeout 계열 예외만" 재시도하도록 판별 함수 추가
    private boolean isTimeoutLike(Throwable t) {
        // 명시적 TimeoutException이면 timeout으로 취급
        if (t instanceof TimeoutException) return true;

        // 4xx/5xx는 응답이 온 거니까 timeout 아님 → 재시도 금지
        if (t instanceof WebClientResponseException) return false;

        // 네트워크/IO 계열 예외는 보통 WebClientRequestException으로 감싸짐
        if (t instanceof WebClientRequestException) {
            Throwable cause = t.getCause();
            if (cause == null) return false;

            // 프로젝트/환경마다 timeout 예외 클래스명이 달라서
            // "클래스명/메시지에 timeout 문자열" 포함 여부로 방어적으로 판별
            String className = cause.getClass().getName().toLowerCase();
            String msg = (cause.getMessage() == null) ? "" : cause.getMessage().toLowerCase();

            return className.contains("timeout")
                    || msg.contains("timeout")
                    || msg.contains("timed out");
        }

        return false;
    }
}