package io.wiklandia.tramapi;

import java.util.Arrays;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final TramProperties tramProperties;
	private final SecurityProperties securityProperties;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().mvcMatchers("/cloudfoundryapplication/**").permitAll();

		// @formatter:off
		http.authorizeRequests()
			.antMatchers("/sickla", "/closestId", "/solna", "/dep", "/cloudfoundryapplication/**", "/application/**", "/disruptions").permitAll()
			.anyRequest().authenticated();
		// @formatter:on

		if (securityProperties.isRequireSsl()) {
			http.requiresChannel().antMatchers("/**").requiresSecure();
			http.requiresChannel().antMatchers("/cloudfoundryapplication/**", "/application/health").requiresInsecure();
		}

		http.httpBasic();

		http.headers().disable();

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.cors();

	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(tramProperties.getAllowedOrigins());
		configuration.setAllowedMethods(Arrays.asList(HttpMethod.GET.name()));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/cloudfoundryapplication/**");
		web.ignoring().mvcMatchers("/cloudfoundryapplication/**");
	}

}
