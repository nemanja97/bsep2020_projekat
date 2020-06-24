package tim6.bsep.SIEMCenter.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tim6.bsep.SIEMCenter.security.CertificateFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class CustomFilterConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/connect").permitAll()
                .antMatchers("/topic/messages").permitAll()
                .antMatchers("/api/v1/logs/*").permitAll()
                .antMatchers("/api/v1/alarms/*").permitAll()
                .antMatchers("/api/v1/logs").permitAll()
                .antMatchers("/api/v1/alarms").permitAll()
                .antMatchers("/api/v1/rules").permitAll()
                .antMatchers("/api/v1/rules/*").permitAll()
                .antMatchers("/api/v1/whitelists").permitAll()
                .antMatchers("/api/v1/whitelists/*").permitAll()
                .antMatchers("/api/v1/blacklists").permitAll()
                .antMatchers("/api/v1/blacklists/*").permitAll()
                .antMatchers("/api/v1/reports").permitAll()
                .antMatchers("/api/v1/reports/**").permitAll()
                .anyRequest().authenticated()
                .and().cors();

        http.csrf().disable().addFilterAfter(new CertificateFilter(), BasicAuthenticationFilter.class);
    }

    @Bean("corsConfigurationSource")
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://localhost:3000"));
        configuration.setAllowedHeaders(Arrays.asList(CorsConfiguration.ALL));
        configuration.setAllowedMethods(Arrays.asList(CorsConfiguration.ALL));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
