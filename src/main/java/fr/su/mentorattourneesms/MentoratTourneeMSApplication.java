package fr.su.mentorattourneesms;

import fr.su.back.api.headers.HeaderUtil;
import fr.su.back.api.security.openapidoc.IrisOperationCustomizer;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackageClasses = {MentoratTourneeMSApplication.class, Jsr310JpaConverters.class})
@EnableFeignClients(basePackages = {"fr.su.mentorattourneems", "fr.su.back"})
@EnableCaching
@OpenAPIDefinition(info = @Info(description = "Cette API...", contact = @Contact(email = "u-gie-iris-pole-sc-etudes-logistique@systeme-u.fr")))
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
public class MentoratTourneeMSApplication extends SpringBootServletInitializer {

	@Autowired
	Environment env;

	@Autowired
	private HeaderUtil headerUtil;

	public static void main(String[] args) {
		SpringApplication.run(MentoratTourneeMSApplication.class, args);
	}

	/**
	 * Gestion des fichiers de messages de l'application
	 *
	 * @return Bean spring pour les messages
	 */
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:/messages", "classpath:/defaultErrorMessages");

		return messageSource;
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOriginPatterns("*").allowedMethods("*").allowCredentials(true).exposedHeaders(headerUtil.getExposedHeader());
			}
		};
	}

	/**
	 * Déclare un Bean permettant de customiser la documentation Open API générée par Springdoc pour chaque opération.
	 * Automatise l'ajout des habilitations et scopes (à partir du @Secured) dans la description de l'Opération.
	 *
	 * @return un OperationCustomizer (Springdoc) Iris
	 */
	@Bean
	public OperationCustomizer operationCustomizer() {
		return new IrisOperationCustomizer().getIrisOperationCustomizer();
	}

	@Configuration
	@Profile("dev")
	@PropertySource("classpath:env.properties")
	static class DevProperty {
	}
}
