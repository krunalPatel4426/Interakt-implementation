package com.implintrakt.Impl.interakt.component.Clients;

import com.implintrakt.Impl.interakt.config.Constants.Constant;
import com.implintrakt.Impl.interakt.config.globalException.customException.InteraktInvalidArgException;
import com.implintrakt.Impl.interakt.dto.AuthDto.LoginRequestDto;
import com.implintrakt.Impl.interakt.dto.InteraktDtos.InteraktMessageRequestDto;
import com.implintrakt.Impl.interakt.dto.InteraktDtos.InteraktResponseDto;
import com.implintrakt.Impl.interakt.dto.WebClientHelper.MessageDeliveryResult;
import com.implintrakt.Impl.interakt.model.ScheduledMessageEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Objects;

@Component
public class InteraktClient {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient webClient;

    @Value("${interakt.api.url}")
    private String apiUrl;

    @Value("${interakt.api.secret}")
    private String apiSecret;
    @Autowired
    private ObjectMapper objectMapper;

    public InteraktResponseDto sendMessage(InteraktMessageRequestDto requestDto) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(requestDto);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            headers.set("Authorization", apiSecret);
            HttpEntity<String> entity = new HttpEntity<>(json, headers);
            String url = apiUrl + "public/message/";
            return restTemplate.postForObject(url, entity, InteraktResponseDto.class);

        }catch (HttpClientErrorException e) {
            logger.error(e.getResponseBodyAsString());
            String errorBody = e.getResponseBodyAsString();
            String cleanMessage = "Interakt Error: " + errorBody;
            try{
                JsonNode root = objectMapper.readTree(errorBody);
                if (root.has("message")) {
                    cleanMessage = root.get("message").asString();
                }
            }catch (Exception ex){
                cleanMessage = errorBody;
            }
            throw new InteraktInvalidArgException(cleanMessage);
        }
        catch (InteraktInvalidArgException e) {
            throw e;
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Value("${interakt.api.url_v2}")
    String apiUrl_v2;
    public Object login(LoginRequestDto  loginRequestDto) {
        try{
            String url = apiUrl_v2 + "login/";
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(loginRequestDto);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(json, headers);
            return restTemplate.postForObject(url, entity, Objects.class);
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException("API Failed: " + e.getMessage());
        }
    }


    public Mono<MessageDeliveryResult> sendMessageReactive(ScheduledMessageEntity scheduledMessageEntity, InteraktMessageRequestDto interaktMessageRequestDto) {
        try{
//            String requestBody = objectMapper.writeValueAsString(scheduledMessageEntity);

            return webClient.post()
                    .uri(apiUrl + "public/message/")
                    .header("Authorization", apiSecret)
                    .bodyValue(interaktMessageRequestDto)
                    .retrieve()
                    .toEntity(InteraktResponseDto.class)
                    .map(response -> {
                        logger.info("Interakt Message: Status: " + response.getBody().isResult() + ", message: " + response.getBody().getMessage() + ", id : " + response.getBody().getId());
                        if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().isResult()) {
                            return new MessageDeliveryResult(scheduledMessageEntity, Constant.INTERAKT_STATUS_SENT, null);
                        }else {
                            logger.error("Error: " + response.getBody());
                            return new  MessageDeliveryResult(scheduledMessageEntity, Constant.INTERAKT_STATUS_FAILED, "API returned false");
                        }
                    }).onErrorResume(e -> {
                        return Mono.just(new MessageDeliveryResult(scheduledMessageEntity, "FAILED", e.getMessage()));
                    });
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException("API Failed: " + e.getMessage());
        }
    }
}
