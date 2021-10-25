package transaction;

//wrong transaction architecture
public class TransactionTest2 {
    public void step1(boolean success) throws Step1Exception {
        try {
            if (success)
                System.out.println("step1 successful!!");
            else
                throw new Step1Exception();
        } catch (Step1Exception e) {
            System.out.println("step1 cancelled");
        }
    }
    public void step2(boolean success) throws Step2Exception {
        try {
            if (success)
                System.out.println("step2 successful!!");
            else
                throw new Step2Exception();
        } catch (Step2Exception e) {
            System.out.println("step2 cancelled");
        }
    }
    public void step3(boolean success) throws Step3Exception {
        try {
            if (success)
                System.out.println("step3 successful!!");
            else
                throw new Step3Exception();
        } catch (Step3Exception e) {
            System.out.println("step3 cancelled");
        }
    }

    public void startProcedure() {
            step1(true);
            step2(false);
            step3(true);
    }

    public static void main(String[] args) {
        TransactionTest2 transactionTest2 = new TransactionTest2();

        transactionTest2.startProcedure();
    }
}
