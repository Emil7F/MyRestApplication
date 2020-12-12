package pl.emil7f.myrestapi.config;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Po skonfigurowaniu SuccessHandlera potrzebny jest jakiś mechanizm który mógłby przechwycić naszego token'a
 * <p>
 * Do tego stworzyliśmy poniższy filter
 * Dzięki niemu będziemy mogli wysyłać go z następnymi requestami
 * Co da nam możliwość odczytu danych endpointów
 * <p>
 * Pamiętaj o dodaniu do properties odpowiednich konfiguracji
 * jwt.secret itp
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private final UserDetailsService userDetailsService;
    private final String secret;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  UserDetailsService userDetailsService, String secret) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.secret = secret;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        // Pobranie obiektu autoryzacji, jeśli nie istnieje przekazuje sterowanie do kolejnego filtra
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if (authentication == null) {
            filterChain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);  // jeśli istnieje ustawiam obiekt w SecurityContextHolder
        filterChain.doFilter(request, response);
    }



    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);  // pobranie tokena z nagłówka
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            String userName = JWT.require(Algorithm.HMAC256(secret))    // inicjalizacja pobrania token'a
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""))        // uzywamy metody verify() do sprawdzenia poprawności sygnatury tokena
                    //  oraz usuwamy z tokena PREFIX
                    .getSubject();                  // Pobieramy subject, w tym wypadku jest to nazwa usera
            if (userName != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName); // pobranie uzytkownika z userDetailsService
                // Poniżej stworzenie UsernamePasswordAuthenticationToken w oparciu o porbane z UserDetails
                return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
            }
        }
        return null;
    }


}
