package tim6.bsep.SIEMCenter.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import tim6.bsep.SIEMCenter.security.X509CustomFilter;

@Configuration
@EnableWebSecurity
public class CustomFilterConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().addFilterAfter(new X509CustomFilter(), BasicAuthenticationFilter.class);
    }
}
