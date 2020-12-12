package pl.emil7f.myrestapi.config;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // Te pola zostaną wstrzyknięte przy pomocy adnotacji @Value    !  import org.springframework.beans.factory.annotation.Value;
    // Pozwala ona wstrzykiwać wartości z pliku properties
    private final long expirationTime;
    private final String secret;

    public RestAuthenticationSuccessHandler(
            @Value("${jwt.expirationTime}")                             long expirationTime,        // "{$jwt.expirationTime}"
            @Value("${jwt.secret}")         String secret) {
        this.expirationTime = expirationTime;
        this.secret = secret;
    }



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
         /**
          * Wystarczy tylko ta metoda jeśli mamy uwierzytelnienie tylko przy pomocy Json'a
          clearAuthenticationAttributes(request);           */

        UserDetails principal = (UserDetails) authentication.getPrincipal();  // Pobieranie szczegółów zalogowanego użytkownika
        String token = JWT.create()                                           // Stworzenie buildera
        .withSubject(principal.getUsername())                                 // Dodanie nazwy użytkownika jako subject
        .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime)) // Ustawienie daty wygaśnięcia, expirationTime trzeba dodać gdzies w konfiguracji
        .sign(Algorithm.HMAC256(secret));                                      // Ustawienie algorytmu HMAC256, secret jest ustawiona w konfiguracji
        response.addHeader("Authorization", "Bearer "+ token);           // Zwracamy JWT jako dodatkowy header

        /*    Json zwrócony w ResponseBody
        response.getOutputStream().print("{\"token\": \"" + token + "\"}");   // Wypisuje jsona zawierającego tokena na OutputStream poprzez konkatenacje
                                                                              // Można użyć ObjectMappera
*/

    }
}
