package com.lulski.laura.boardgamebuddy;

import com.lulski.laura.prompt.Answer;
import com.lulski.laura.prompt.Question;

/**
 * BoardGameService
 */
public interface BoardGameService {

    Answer askQuestion(Question question);
}
