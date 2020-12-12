package pl.emil7f.myrestapi.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.emil7f.myrestapi.config.LoginCredentials;

@RestController
public class LoginController {

    @PostMapping("/login")
    public void login(@RequestBody LoginCredentials credentials) {
/**    Normalnie ten endpoint w Spring Security  "/login"
    Jest potrzebny żeby było go widać w swaggerze
    Tworzymy klase LoginCredentials, gdzie będzie login i hasło
    Ta metoda nie musi nic robić, za uwierzytenienie odpowiada filtr
    Bez filtra endpoint /login przyjmuje dane w formacie  application/x-www--form-urlencoded
    Nie jest to spójne z usługami Restowymi, które przyjmują jsona: application/json
*/

    }
}
