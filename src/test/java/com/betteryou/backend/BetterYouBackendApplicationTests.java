package com.betteryou.backend;

import com.google.cloud.firestore.Firestore;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Proxy;

@SpringBootTest
@ActiveProfiles("test")
class BetterYouBackendApplicationTests {

	@Test
	void contextLoads() {
	}

	@TestConfiguration
	static class FirestoreTestConfig {

		@Bean
		@Primary
		Firestore testFirestore() {
			return (Firestore) Proxy.newProxyInstance(
					Firestore.class.getClassLoader(),
					new Class<?>[]{Firestore.class},
					(proxy, method, args) -> {
						if (method.getName().equals("close") || method.getName().equals("shutdown")) {
							return null;
						}
						if (method.getName().equals("toString")) {
							return "testFirestore";
						}
						throw new UnsupportedOperationException("Firestore should not be called by contextLoads");
					}
			);
		}
	}
}
