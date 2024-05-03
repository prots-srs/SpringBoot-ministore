package com.protsprog.ministore;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
class WebSecurityConfig {

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {

		User.UserBuilder users = User.builder();

		UserDetails admin = users
				.username("admin")
				.password(passwordEncoder.encode("admin"))
				// .disabled(true)
				// .authorities("db")
				.roles("ADMIN")
				.build();

		return new InMemoryUserDetailsManager(admin);
	}

	@Bean
	SecurityFilterChain web(HttpSecurity http) throws Exception {

		http
				.authorizeHttpRequests((requests) -> requests
						.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
						.requestMatchers("/",
								"/favicon.ico",
								"/error",
								"/assets/**",
								// "/webjars/**",
								"/css/**",
								"/js/**",
								"/images/**",
								"/actuator/health")
						.permitAll()
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.anyRequest().authenticated())
				// .httpBasic(Customizer.withDefaults())
				.sessionManagement((session) -> session.maximumSessions(1))
				.formLogin((form) -> form
						.loginPage("/login")
						.permitAll())
				.logout((logout) -> logout
						.logoutSuccessUrl("/")
						.invalidateHttpSession(true)
						.permitAll());

		return http.build();
	}
}
