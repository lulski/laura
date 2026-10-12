package com.lulski.laura;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lulski.laura.boardgamebuddy.BoardGameService;
import com.lulski.laura.prompt.Answer;
import com.lulski.laura.prompt.Question;

@SpringBootTest(properties = "spring.ai.openai.api-key=${SPRING_AI_OPENAI_API_KEY}")
public class SpringAiBoardGameServiceTests {

    @Autowired 
    private BoardGameService boardGameService;

    @Autowired 
    private ChatClient.Builder chatClientBuilder;


    private RelevancyEvaluator relevancyEvaluator;

    @BeforeEach 
    public void setup() {
        this.relevancyEvaluator = new RelevancyEvaluator(chatClientBuilder);
    }

    @Test 
    public void evaluateRelevancy() {
        String userText = "Why is the sky blue";
        Question question = new Question(userText);

        Answer answer = boardGameService.askQuestion(question);
        EvaluationRequest evaluationRequest = new EvaluationRequest(userText, answer.answer());

        EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);

        Assertions.assertThat(evaluationResponse.isPass())
        .isTrue()
        .withFailMessage("""
        ========================================
          The answer "%s"
          is not considered relevant to the question
          "%s".
        ======================================== 
        """, answer.answer(),userText)
        ;

    }

}
