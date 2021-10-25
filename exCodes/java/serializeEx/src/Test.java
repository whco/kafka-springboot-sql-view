import java.io.*;
import java.util.Base64;

public class Test {
    public static void main(String[] args) {
        Member member = new Member("hj", "235325", "26262", 3);
        byte[] serializedMember = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(member);

                serializedMember = baos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (byte b : serializedMember) {
            System.out.printf("%d ", b);
        }
        System.out.println();
        String serializedMemeberString = Base64.getEncoder().encodeToString(serializedMember);
        System.out.println(serializedMemeberString);

        //나이 변경 시 직렬화한 문자열이 다름
        String s1 = "rO0ABXNyAAZNZW1iZXKUyXEZi54whAIAA0kAA2FnZUwABWVtYWlsdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgABeHAAAAAadAAMaGpAbmF2ZXIuY29tdAACaGo=";
        String s2 = "rO0ABXNyAAZNZW1iZXKUyXEZi54whAIAA0kAA2FnZUwABWVtYWlsdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgABeHAAAAADdAAMaGpAbmF2ZXIuY29tdAACaGo=";
        System.out.println(s1.equals(s2));
        //password만 변경 시 같음
        String s3 = "rO0ABXNyAAZNZW1iZXKUyXEZi54whAIAA0kAA2FnZUwABWVtYWlsdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgABeHAAAAADdAAGMjM1MzI1dAACaGo=";
        String s4 = "rO0ABXNyAAZNZW1iZXKUyXEZi54whAIAA0kAA2FnZUwABWVtYWlsdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgABeHAAAAADdAAGMjM1MzI1dAACaGo=";
        System.out.println(s3.equals(s4));

        byte[] serializedMemeberStringDeserialized = Base64.getDecoder().decode(serializedMemeberString);
        for (byte b : serializedMemeberStringDeserialized) {
            System.out.printf("%d ", b);
        }
        System.out.println();
        System.out.println("Is serializedMember equal serializedMemberStringDeserialized? " + java.util.Arrays.equals(serializedMember, serializedMemeberStringDeserialized));

        try (ByteArrayInputStream bais = new ByteArrayInputStream(serializedMemeberStringDeserialized)) {
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                Object objectMemeber = ois.readObject();
                Member member1 = (Member) objectMemeber;
                System.out.println(member1);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
