package com.ManShirtShop.Authentication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ManShirtShop.Authentication.security.jwt.AuthEntryPointJwt;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
				.and().addFilterAfter(new JwtTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests()
				.antMatchers("/v3/api-docs", "/swagger-ui/**",
						"/configuration/ui",
						"/swagger-resources/**",
						"/configuration/security",
						"/swagger-ui.html",
						"/webjars/**",
						"/api/Rating/findByIdProduct/**",
						"/ghn/api/**","/api/client/vnpay/**","/client/api/properties/**",
						"/client/api/product/**","/client/api/voucher/getAllVoucher",
						"/swagger-ui/index.html")
				.permitAll()
				.antMatchers("/api/auth/**",
						"/api/employee/get_user",
						"/api/customer/register",
						"/api/customer/send-code",
						"/api/customer/send-code-dki",
						"/api/customer/confirm-code-dki",
				    "/api/customer/forgot-password","/api/customer/confirm-code",
            "/api/upload-firebase","/api/Rating/**","/api/RatingImage/**").permitAll()
				.antMatchers("/api/upload-firebase","/api/customer/changePass").hasAnyRole("Customer", "Admin", "Manager")
				.antMatchers("/api/**").hasAnyRole("Admin","Manager")
				.anyRequest().authenticated();
	}

}
