package com.fitconnet.config.security;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fitconnet.enums.Role;
import com.fitconnet.service.interfaces.entity.UserServiceI;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	private final UserServiceI userService;

	public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, UserServiceI userService) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.userService = userService;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(request -> request.requestMatchers("/api/v1/auth/**").permitAll()
						.requestMatchers("/api/v1/actividades/**")
						.hasAnyAuthority(Role.ROLE_USER.toString(), Role.ROLE_ADMIN.toString())
						.requestMatchers("/api/v1/notification/**")
						.hasAnyAuthority(Role.ROLE_USER.toString(), Role.ROLE_ADMIN.toString())
						.requestMatchers("/api/v1/user/**")
						.hasAnyAuthority(Role.ROLE_USER.toString(), Role.ROLE_ADMIN.toString())
						.requestMatchers("/api/v1/admin/**").hasAnyAuthority(Role.ROLE_ADMIN.toString())
						.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
						.anyRequest().authenticated())
				.sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userService.userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
