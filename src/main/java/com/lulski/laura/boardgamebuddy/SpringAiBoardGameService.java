package com.lulski.laura.boardgamebuddy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.lulski.laura.prompt.Answer;
import com.lulski.laura.prompt.Question;



@Service
public class SpringAiBoardGameService implements BoardGameService {

    private final ChatClient chatClient;
    private final RelevancyEvaluator relevancyEvaluator;

    private final Logger LOGGER = LoggerFactory.getLogger(SpringAiBoardGameService.class);


    public SpringAiBoardGameService(ChatClient.Builder chatClientBuilder) {
        var chatOptions = ChatOptions.builder().model("gpt-6-luna");
  
        this.chatClient = chatClientBuilder
        .defaultOptions(chatOptions)
        .build();

        this.relevancyEvaluator = new RelevancyEvaluator(chatClientBuilder);
    }


    @Override
    @Retryable(includes = AnswerNotRelevantException.class )
    public Answer askQuestion(Question question) {
        LOGGER.info("calling LLM start: "+ question.question());
        var answerText = chatClient.prompt()
            .user(question.question())
            .call()
            .content(); 
        LOGGER.info("calling LLM end: "+ answerText);

        evaluateRelevancy(question, answerText);

        return new Answer(answerText);            
    }

    private void evaluateRelevancy(Question question, String answerText) {
        var evaluationRequest = new EvaluationRequest(question.question(), answerText);
        LOGGER.info("evaluate relevancy calling LLM start: "+ question.question());
        var evaluationResponse = this.relevancyEvaluator.evaluate(evaluationRequest);
        LOGGER.info("evaluate relevancy calling LLM end: "+ evaluationResponse.getScore());
        if (!evaluationResponse.isPass()) {
            LOGGER.error(answerText  +" is not relevant");
            throw new AnswerNotRelevantException(question.question(), answerText);
        }
    }
    
}
