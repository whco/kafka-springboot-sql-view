package transaction;

public class TransactionTest1 {
    public void step1(boolean success) throws Step1Exception {

        if(success)
            System.out.println("step1 successful!!");
        else
            throw new Step1Exception();
    }
    public void step2(boolean success) throws Step2Exception {
        if(success)
            System.out.println("step2 successful!!");
        else
            throw new Step2Exception();
    }
    public void step3(boolean success) throws Step3Exception {
        if(success)
            System.out.println("step3 successful!!");
        else
            throw new Step3Exception();
    }

    public void startProcedure() {
        try {
            step1(true);
            step2(false);
            step3(true);
        } catch (StepException e) {
            System.out.println("All steps cancelled");
        }
    }

    public static void main(String[] args) {
        TransactionTest1 transactionTest1 = new TransactionTest1();

        transactionTest1.startProcedure();
    }
}
