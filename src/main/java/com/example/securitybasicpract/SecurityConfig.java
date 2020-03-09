package com.example.securitybasicpract;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(getDigestEntryPoint())
                .and()
                .addFilter(getDigestFilter());
    }

    public DigestAuthenticationFilter getDigestFilter() {
        var filter = new DigestAuthenticationFilter();
        filter.setUserDetailsService(userDetailsService());
        filter.setAuthenticationEntryPoint(getDigestEntryPoint());
        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password("admin").roles("ADMIN").and()
                .withUser("user").password("user").roles("USER");
    }

    @Override
    @Bean
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }


    private DigestAuthenticationEntryPoint getDigestEntryPoint() {
        var digestAuthenticationEntryPoint = new DigestAuthenticationEntryPoint();
        digestAuthenticationEntryPoint.setKey("gfghfJGHJG%");
        digestAuthenticationEntryPoint.setRealmName("admin-digest-realm");
        return digestAuthenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //https://github.com/spring-projects/spring-security/issues/6759
        //return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * #Authorization header
     *
     * Authorization: Digest username="admin", realm="admin-digest-realm",
     * nonce="MTU4Mzc1MzE2NDEyMzplMGQyNWFjOGY0Njk2ZTZkZGJmYWJhNGFjNDU4YmY1NQ==",
     * uri="/test", response="b1d2207c381297b5bfc1222795bfc8db", qop=auth, nc=00000007,
     * cnonce="5eef9c0fb9f11aa0"
     *
     * some comment about vulnerability
     *    More secure than basic auth, in each request the nonce count increment.
     *   in case of wrong nonce and cnounce  count replay attacked prevented.
     *   However it is possible MIM(man in middle attack), the attacker can intercept the request modify it and
     *   forward to server, there is however version of digest that addresses this. the qop(Quality of protection)
     *   is auth/int it include the hash in body of response. that is way server can verify body isn't  tempered.
     *   However its not widely supported and the spring also dose'nt support it.
     *
     *   Digest work very different way, it does'nt use authentication manager but instead use digest and digest vary
     *   between each request. it retrieve plain text password and generate the digest
     *
     *   Spring security not recommended to user digest authentication. check documentation
     *
     *
     *
     */
}
