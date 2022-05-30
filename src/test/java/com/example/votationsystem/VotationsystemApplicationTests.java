package com.example.votationsystem;

import com.example.votationsystem.core.person.application.app.VotationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VotationsystemApplicationTests {

	@BeforeEach
	void contextLoads() {
		VotationService votationService = new VotationService("votation test");
	}
	@Test
	public void requestTest(){

	}

}
