package com.lulski.laura.boardgamebuddy;


public class AnswerNotRelevantException extends RuntimeException {
    private String question;
    private String answer;

    public  AnswerNotRelevantException (String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

}
