package com.freelibrary.Paplibrary;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;




@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/index","/resources/static/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/index")
                        .permitAll());


        return http.build();
    }



    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("user")
                        .roles("USER")
                        .build();

        UserDetails admin =
                User.withDefaultPasswordEncoder()
                        .username("admin")
                        .password("admin")
                        .roles("ADMIN")
                        .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}
//@Configuration
//@EnableWebSecurity
//public class MultiHttpSecurityConfig {
//	@Bean
//	public UserDetailsService userDetailsService() throws Exception {
//		// ensure the passwords are encoded properly
//		UserBuilder users = User.withDefaultPasswordEncoder();
//		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//		manager.createUser(users.username("user").password("password").roles("USER").build());
//		manager.createUser(users.username("admin").password("password").roles("USER","ADMIN").build());
//		return manager;
//	}
//
//	@Bean
//	@Order(1)
//	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
//		http
//			.securityMatcher("/api/**")
//			.authorizeHttpRequests(authorize -> authorize
//				.anyRequest().hasRole("ADMIN")
//			)
//			.httpBasic(withDefaults());
//		return http.build();
//	}
//
//	@Bean
//	public SecurityFilterChain formLoginFilterChain(HttpSecurity http) throws Exception {
//		http
//			.authorizeHttpRequests(authorize -> authorize
//				.anyRequest().authenticated()
//			)
//			.formLogin(withDefaults());
//		return http.build();
//	}
//}