package com.arquitectojava.letters.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes=LettersAPIAppTests.class)
@AutoConfigureTestDatabase
class LettersAPIAppTests {

	@Test
	void contextLoads() {
	}

}
