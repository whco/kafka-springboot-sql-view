import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

//serializable 인터페이스를 상속받은 객체만 직렬화 가능
public class Member implements Serializable {
    private String name;
    private String email;
    //직렬화 시 제외
    private transient String password;
    private int age;
    //Object 멤버변수로 가질 시 Object 멤버변수도 직렬화가능해야 본 클래스도 직렬화가능

    public Member(String name, String email, String password, int age) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format("Member{name='%s, email='%s', password='%s', age='%s'}", name, email, password, age);
    }
}
