package pl.emil7f.myrestapi.config;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    /**
                Klasa konfiguracyjna do ObjectMapper
                Json nie będzie serializował nam pól z nullem
                Zaoszczędzimy na transferze

            Autowired nie jest rekomendowany
     */
    @Autowired
    private ObjectMapper objectMapper;

    void customizeObjectMapper(){
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }


}
