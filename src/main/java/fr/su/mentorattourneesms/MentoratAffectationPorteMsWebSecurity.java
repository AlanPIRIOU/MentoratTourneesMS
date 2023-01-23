package fr.su.mentorattourneesms;

import fr.su.back.api.security.AuthenticationConstantes;
import fr.su.back.api.security.SecurityConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Order(1)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@EnableFeignClients
public class MentoratAffectationPorteMsWebSecurity extends SecurityConfiguration {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.httpBasic();

        // Intercepteur permettant de gérer une error correctement formatée en cas d'accès interdit
        http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint());


        // si on accède au "/actuator/health" en ANONYMOUS ou en USER, on ne voit que le statut global, et on voit
        // davantage d'informations si on est connecté avec un profil de rôle ACTUATOR
        http.authorizeRequests().antMatchers("/actuator/health").hasAnyRole(AuthenticationConstantes.ROLE_ACTUATOR,
                AuthenticationConstantes.ROLE_ANONYMOUS, AuthenticationConstantes.ROLE_USER);

        // accès au endpoint "actuator/info" quel que soit le rôle de l'utilisateur
        http.authorizeRequests().antMatchers("/actuator/info").hasAnyRole(AuthenticationConstantes.ROLE_ACTUATOR,
                AuthenticationConstantes.ROLE_ANONYMOUS, AuthenticationConstantes.ROLE_USER);

        // les autres actuators ne sont accessibles que pour les profils de rôle ACTUATOR
        http.authorizeRequests().antMatchers("/actuator/**").hasRole(AuthenticationConstantes.ROLE_ACTUATOR);

        // accès à la documentation de l'API quel que soit le rôle de l'utilisateur
        http.authorizeRequests().antMatchers("/v3/api-docs.yaml").hasAnyRole(AuthenticationConstantes.ROLE_ACTUATOR,
                AuthenticationConstantes.ROLE_ANONYMOUS, AuthenticationConstantes.ROLE_USER);
        http.authorizeRequests().antMatchers("/v3/api-docs/**").hasAnyRole(AuthenticationConstantes.ROLE_ACTUATOR,
                AuthenticationConstantes.ROLE_ANONYMOUS, AuthenticationConstantes.ROLE_USER);
        http.authorizeRequests().antMatchers("/swagger-ui/**").hasAnyRole(AuthenticationConstantes.ROLE_ACTUATOR,
                AuthenticationConstantes.ROLE_ANONYMOUS, AuthenticationConstantes.ROLE_USER);
        http.authorizeRequests().antMatchers("/swagger-ui.html").hasAnyRole(AuthenticationConstantes.ROLE_ACTUATOR,
                AuthenticationConstantes.ROLE_ANONYMOUS, AuthenticationConstantes.ROLE_USER);

        // toutes les autres requêtes nécessitent le rôle USER
        http.authorizeRequests().anyRequest().hasRole(AuthenticationConstantes.ROLE_USER);

        // Ajout du filtre permettant le traitement éventuel d'un jeton JWT passé dans le header
        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

}
