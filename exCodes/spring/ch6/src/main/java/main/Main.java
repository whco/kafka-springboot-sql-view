package main;

import config.AppCtx;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import spring.Client;

public class Main {
    public static void main(String[] args) {
        AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(AppCtx.class);

        Client client = ctx.getBean(Client.class);
        Client client1 = ctx.getBean(Client.class);
        System.out.println(client == client1);
        client.send();

        ctx.close();
    }
}
