package com.muttsApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
 
@Configuration
@EnableAutoConfiguration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
 
	@Autowired
	DataSource dataSource;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

 
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
				.dataSource(dataSource)
				.usersByUsernameQuery("select email, password, enabled from user where email = ?")
				.authoritiesByUsernameQuery("select u.username, r.role " +
						"from role r " +
						"join userrole ur " +
						"on r.roleId = ur.roleId " +
						"join user u " +
						"on ur.userId = u.userId " +
						"where email = ?");
//				.passwordEncoder(bCryptPasswordEncoder);
	}
 
	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http
//				.csrf().disable()
//				.authorizeRequests()
//					.antMatchers("/", "/home", "/users").permitAll()
//	//				.antMatchers("/admin/**").hasRole("ADMIN")
//					.anyRequest().authenticated()
//					.and()
//				.formLogin()
//					.loginPage("/login").permitAll()
//					.failureUrl("/login?error=true")
//					.defaultSuccessUrl("/home")
//					.and()
//				.logout().permitAll();
//
//		http.exceptionHandling().accessDeniedPage("/403");

		http.
				authorizeRequests()
				.antMatchers("/", "/login", "/users/**", "/chats/**").permitAll()
				.antMatchers("/registration").permitAll()
//				.antMatchers("/users/**").hasAuthority(("USER"))
//				.antMatchers("/admin/**").hasAuthority("ADMIN")
				.anyRequest().authenticated()
				.and().csrf().disable().formLogin()
				.loginPage("/login").failureUrl("/login?error=true")
				.defaultSuccessUrl("/home")
				.usernameParameter("email")
				.passwordParameter("password")
				.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/").and().exceptionHandling()
				.accessDeniedPage("/403");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web
				.ignoring()
				.antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	}
}