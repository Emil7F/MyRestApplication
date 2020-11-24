package pl.emil7f.myrestapi.service;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

/**
 * Stworzenie serwisu do controllera Hello
 * Dodanie adnotacji aby kontekst Springa mógł ją odnaleźć
 * W tym momecie możemy wstrzyknąć HelloService do HelloControllera

 Jest też adnotacja @Scope, o wartościach:
 SCOPE_SINGLETON  - domyślny, Spring tworzy tylko jeden bean
 SCOPE_PROTOTYPE  - za każdym razem kiedy odwołujemy się do bean'a to jest tworzona nowa instancja bean'a
                    żeby wstrzyknąć tego bean'a potrzebna jest jeszcze zmiana proxyMode() na ScopedProxyMode.TARGET_CLASS
                    Scope(value = "PROTOTYPE", proxyMode = ScopedProxyMode.TARGET_CLASS)
 SCOPE_REQUEST    - przy każdym nowym requescie tworzona jest nowa instancja bean'a
 SCOPE_SESSION    - jeśli używamy w apce sesji http

  */

@Service
public class HelloService {


    public String hello() {
        return "Hello Service";
    }
}
