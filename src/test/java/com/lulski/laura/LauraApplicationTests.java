package com.lulski.laura;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import com.lulski.laura.boardgamebuddy.BoardGameService;

@SpringBootTest
class LauraApplicationTests {

	@Autowired 
	BoardGameService boardGameService;
	

	@Test
	void contextLoads() {
	}

}
