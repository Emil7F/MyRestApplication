package pl.emil7f.myrestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.emil7f.myrestapi.service.HelloService;


/**
 * Dzięki adnotacji @RestController Spring będzie mógł sobie odnaleźć taką klase przy użyciu
 *
 * ComponentScan i utworzyć dla niej Bean'a
 * <p>
 * W Spring'u możemy wstrzykiwać zaleźności na trzy sposoby.
 * 1. To przez pole, tak jak poniżej. Z andotacją @Autowired
 * 2. Przez adnotowanie settera
 * 3. Przez konstruktor  - najlepszy
 *
 *  tips: Jeśli używamy lomboka, możemy się spotkać jeszcze adnotacje
 *       RequiredArgsContstruktor
 *       wtedy nie trzeba tworzyć konstruktora
 */
@RestController
public class HelloController {

    public HelloService helloService;

    // Wstrzykujemy serwis do kontrolera -> w tym momecie po staremu przy pomocy adnotacji @Autowired.
    //    Sposób nr.1
    //    @Autowired
    //    private HelloService helloService;
    //    Sposób nr.2 działa podobnie tylko musimy wygenerować setter i jego oznaczyć adnotacją @Autowired

    // Sposób nr.3  - Wstrzyknięcie zależności poprzez konstruktor
    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/")
    public String hello() {
        return "HelloController";
    }
    
    // Przykład użycia wstrzykniętego serwisu poprzez wywołanie metody na nim.
    @GetMapping("/helloService")
    public String hello2() {
        return helloService.hello();
    }


}
