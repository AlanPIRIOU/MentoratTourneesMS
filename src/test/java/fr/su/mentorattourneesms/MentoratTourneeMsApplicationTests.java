package fr.su.mentorattourneesms;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;


@Configuration
public class MentoratTourneeMsApplicationTests {
	@Configuration
	@Profile("test")
	@PropertySource("classpath:env-test.properties")
	static class TestProperty {
	}
}
