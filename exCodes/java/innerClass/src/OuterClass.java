public class OuterClass {
    private int data = 30;

    class InnerClass {
        void msg(){
            System.out.println("data is " + data);
        }
    }

    public static void main(String[] args) {
        OuterClass obj = new OuterClass();
        OuterClass.InnerClass in = obj.new InnerClass();
        in.msg();

    }
}
