package com.foodon.foodon.meal.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class MealDetectAiClient {

    private final WebClient webClient;

    public MealDetectAiResponse detect(String imageUrl) {
        return webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new MealDetectAiRequest(imageUrl))
                .retrieve()
                .bodyToMono(MealDetectAiResponse.class)
                .block();
    }

    public MealDetectAiResponse detect(MultipartFile multipartFile) {
        MealDetectAiResponse response = webClient.post()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("file", multipartFile.getResource()))
                .retrieve()
                .bodyToMono(MealDetectAiResponse.class)
                .block();

        log.info("AI 분석 결과 검출된 음식: {}", response);
        return response;
    }

}
