import java.sql.Timestamp;
import java.util.Date;

public class TimestampToDatetime {
    public static void main(String[] args) {
        Timestamp ts = new Timestamp(1632204559000L);
        System.out.println(ts.toString());
        Date date = new Date(ts.getTime());
        System.out.println(date);

    }
}
