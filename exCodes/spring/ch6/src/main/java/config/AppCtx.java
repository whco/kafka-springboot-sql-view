package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import spring.Client;
import spring.Client2;

@Configuration
public class AppCtx {
    @Bean
//    @Scope("prototype")
    public Client client() {
        Client client = new Client();
        client.setHost("host");
        System.out.println("client() 실행");
        return client;
    }

    @Bean(initMethod = "connect", destroyMethod = "close")
    public Client2 client2() {
        Client2 client2 = new Client2();
        client2.setHost("host2");
        System.out.println("client2() 실행");
        return client2;
    }
}
