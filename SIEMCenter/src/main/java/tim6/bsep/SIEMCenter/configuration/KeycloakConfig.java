package tim6.bsep.SIEMCenter.configuration;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tim6.bsep.SIEMCenter.security.CertificateFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
public class KeycloakConfig extends KeycloakWebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(
            AuthenticationManagerBuilder auth) throws Exception {

        KeycloakAuthenticationProvider keycloakAuthenticationProvider
                = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(
                new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(
                new SessionRegistryImpl());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/connect").permitAll()
                .antMatchers("/topic/messages").permitAll()
                .antMatchers("/api/v1/logs/receive").permitAll()
                .antMatchers("/api/v1/logs/*").hasAnyRole("SIEM center admin", "SIEM center operator")
                .antMatchers("/api/v1/alarms/*").hasAnyRole("SIEM center admin", "SIEM center operator")
                .antMatchers("/api/v1/logs").hasAnyRole("SIEM center admin", "SIEM center operator")
                .antMatchers("/api/v1/alarms").hasAnyRole("SIEM center admin", "SIEM center operator")
                .antMatchers("/api/v1/rules").hasRole("SIEM center admin")
                .antMatchers("/api/v1/rules/*").hasRole("SIEM center admin")
                .antMatchers("/api/v1/whitelists").hasRole("SIEM center admin")
                .antMatchers("/api/v1/whitelists/*").hasRole("SIEM center admin")
                .antMatchers("/api/v1/blacklists").hasRole("SIEM center admin")
                .antMatchers("/api/v1/blacklists/*").hasRole("SIEM center admin")
                .anyRequest().authenticated()
                .and().cors();
        http.csrf().disable().addFilterAfter(new CertificateFilter(), BasicAuthenticationFilter.class);
    }

    @Bean("corsConfigurationSource")
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://localhost:3001"));
        configuration.setAllowedHeaders(Arrays.asList(CorsConfiguration.ALL));
        configuration.setAllowedMethods(Arrays.asList(CorsConfiguration.ALL));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
