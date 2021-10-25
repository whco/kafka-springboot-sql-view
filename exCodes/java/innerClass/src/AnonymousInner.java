abstract class Person{
    abstract void eat();
}

interface Eatable {
    void eat();
}
public class AnonymousInner {
    public static void main(String[] args) {
        Person p = new Person(){
            void eat(){
                System.out.println("nice fruits!");
            }
        };
        p.eat();

        Eatable e = new Eatable() {
            @Override
            public void eat() {
                System.out.println("Eatable eat");
            }
        };
        e.eat();
    }
}
