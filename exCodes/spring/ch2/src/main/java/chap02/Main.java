package chap02;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppContext.class);
        Greeter g1 = ctx.getBean("greeter", Greeter.class);
        Greeter g2 = ctx.getBean("greeter2", Greeter.class);
        System.out.println(g1==g2);
        System.out.println(g1);
        System.out.println(g2);
//        String msg = g.greet("spring");
//        System.out.println(msg);
        ctx.close();
    }
}
