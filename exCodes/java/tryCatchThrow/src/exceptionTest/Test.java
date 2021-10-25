package exceptionTest;

public class Test {
    public void sayNick(String nick) {
        try {
            if ("fool".equals(nick)) {
                throw new FoolException();
            }

            System.out.println("당신의 별명은 " + nick + "입니다");
        } catch (FoolException e) {
            System.err.println("FoolException Occurred");
        }
    }

    public static void main(String[] args) {
        Test test = new Test();
            test.sayNick("fool");
            System.out.println("test");
//            test.sayNick("genius");
    }
}
