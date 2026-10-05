package com.lulski.laura;

import java.io.IOException;
import java.nio.charset.Charset;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.lulski.laura.boardgamebuddy.SpringAiBoardGameService;
import com.lulski.laura.prompt.Question;

@SpringBootTest(properties = "spring.ai.openai.base-url=${openai.base.url}")
@EnableWireMock(@ConfigureWireMock(baseUrlProperties = "openai.base.url"))
public class SpringAiBoardGameServiceWireMockTests {

    @Value("classpath:/test-openai-response.json")
    Resource respResource;

    @Autowired
    ChatClient.Builder chatBuilder;

    @BeforeEach
    public void setup() throws IOException {
        var cannedResponse = respResource.getContentAsString(Charset.defaultCharset());
        var mapper = new ObjectMapper();
        var responseNode = mapper.readTree(cannedResponse);
        WireMock.stubFor(
                WireMock.post("/chat/completions").willReturn(ResponseDefinitionBuilder.okForJson(responseNode)));
    }

    @Test 
    public void testAskQuestion(){
        var boardGameService = new SpringAiBoardGameService(chatBuilder);
        var answer = boardGameService.askQuestion(new Question("what is the capital of France?"));
        Assertions.assertThat(answer).isNotNull();
        Assertions.assertThat(answer.answer()).isEqualTo("Paris");
    }
}
