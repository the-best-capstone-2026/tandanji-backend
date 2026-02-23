package com.sjcapstone.tandanji.ai;

import java.time.Duration; // ✅ [추가] 3초 같은 시간 값을 코드로 표현하려고 추가

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector; // ✅ [추가] WebClient에 "네트워크 설정된 HttpClient"를 붙이기 위해 추가
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient; // ✅ [추가] WebClient 내부에서 실제 HTTP 통신을 담당하는 Netty HttpClient를 직접 설정하려고 추가

@Configuration
public class AiClientConfig {

    @Value("${ai.base-url}")
    private String aiBaseUrl;

    @Bean
    public WebClient aiWebClient() {

        // ✅ [추가된 핵심]
        // 기존: WebClient.builder().baseUrl(aiBaseUrl).build();  (타임아웃 설정 없음)
        // 변경: HttpClient에 responseTimeout(3초)을 걸고, 그 HttpClient를 WebClient에 연결함
        //
        // 이유(요구사항):
        // - AI 서버가 죽었거나 멈췄을 때 요청이 오래 붙잡히지 않게 하려고
        // - "3초 안에 실패로 빠지기"를 보장해서 Spring이 계속 정상 동작하게 만들려고
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(3)); // ✅ [추가] 응답이 3초 안 오면 타임아웃 발생

        return WebClient.builder()
                .baseUrl(aiBaseUrl) // ✅ (기존과 동일) base-url + "/predict" 형태로 요청 만들기 위해 유지
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                // ✅ [추가] 위에서 만든 "3초 타임아웃이 적용된 httpClient"를 WebClient에 붙이는 줄
                // 즉, 이제 이 WebClient(aiWebClient)로 호출하는 모든 요청은 응답이 3초 넘으면 끊김
                .build();
    }
}
