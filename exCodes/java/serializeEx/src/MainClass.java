import java.io.*;
import java.util.ArrayList;

public class MainClass {
    private static final String USERINFO_SER = "user.ser";

    public static void main(String[] args) {
        serialize();
        deserialized();
    }

    public static void serialize() {
        try {
            FileOutputStream fos = new FileOutputStream(USERINFO_SER);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            Member m1 = new Member("hj", "hj@naver.com", "1234", 26);
            Member m2 = new Member("m2", "m2@naver.com", "123", 22);

            ArrayList list = new ArrayList<>();
            list.add(m1);
            list.add(m2);

            oos.writeObject(m1);
            oos.writeObject(m2);
            oos.writeObject(list);
            oos.close();
            System.out.println("직렬화 완료");
            System.out.println((bos.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deserialized() {
        try {
            FileInputStream fis = new FileInputStream(USERINFO_SER);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);

            Member m1 = (Member) ois.readObject();
            Member m2 = (Member) ois.readObject();
            ArrayList list = (ArrayList) ois.readObject();

            System.out.println(m1.toString());
            System.out.println(m2.toString());
            System.out.println(list.toString());

            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
