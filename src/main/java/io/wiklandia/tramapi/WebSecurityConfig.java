package io.wiklandia.tramapi;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.channel.ChannelDecisionManagerImpl;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final TramProperties tramProperties;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// @formatter:off
		http.authorizeRequests()
			.antMatchers("/closestId", "/dep", "/disruptions", "/actuator/health").permitAll()
			.anyRequest().authenticated();
		// @formatter:on
		http.httpBasic();
		http.headers().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.cors();

		if (tramProperties.isRequireSsl()) {
			http.requiresChannel().antMatchers("/actuator/health").requires(ChannelDecisionManagerImpl.ANY_CHANNEL)
					.anyRequest().requiresSecure();
		}
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
