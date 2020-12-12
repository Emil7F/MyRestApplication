package pl.emil7f.myrestapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private final ObjectMapper objectMapper;
    private final RestAuthenticationSuccessHandler successHandler;
    private final RestAuthenticationFailureHandler failureHandler;
    private final String secret;

    public SecurityConfig(DataSource dataSource, ObjectMapper objectMapper, RestAuthenticationSuccessHandler successHandler,
                          RestAuthenticationFailureHandler failureHandler,
                          @Value("${jwt.secret}") String secret) {
        this.dataSource = dataSource;
        this.objectMapper = objectMapper;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.secret = secret;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .withDefaultSchema()        // tworzenie domyślnej tablicy USER
                .dataSource(dataSource)
                .withUser("test")
                .password("{bcrypt}" + new BCryptPasswordEncoder().encode("test"))
                .roles("USER");
    }

    /**
     * Podstawowa konfiguracja configure (HttpSecurity http)
     *
     * @Override protected void configure(HttpSecurity http) throws Exception {
     * <p>
     * http.csrf().disable();  // wyłączenie obsługi csrd tokena
     * http
     * .authorizeRequests()
     * .antMatchers("/").permitAll()   // pozwalamy wchodzić na endpoint "/" bez logowania
     * .anyRequest().authenticated()
     * .and()
     * .formLogin().permitAll();          // pozwalamy wchodzić bez logowania na formularz
     * }
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http
                .authorizeRequests()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
//        .and()                po dodaniu filtra, można zamienić tą część, dostęp jest ustawiony
//                .formLogin().permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(authenticationFilter())
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userDetailsManager(), secret))
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .headers()
                .frameOptions().disable();   // aby dostać się do consoli H2
        // Spring Security wystawia headery, przez które nie można dostać się do ramek w HTML

    }

    public JsonObjectAuthenticationFilter authenticationFilter() throws Exception {
        JsonObjectAuthenticationFilter authenticationFilter = new JsonObjectAuthenticationFilter(objectMapper);
        authenticationFilter.setAuthenticationSuccessHandler(successHandler);
        authenticationFilter.setAuthenticationFailureHandler(failureHandler);
        authenticationFilter.setAuthenticationManager(super.authenticationManager());
        return authenticationFilter;
    }

    @Bean
    public UserDetailsManager userDetailsManager(){
        return new JdbcUserDetailsManager(dataSource);
    }

}
