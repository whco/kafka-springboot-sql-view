package spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import java.time.format.DateTimeFormatter;

public class MemberPrinter {
    private DateTimeFormatter dateTimeFormatter;

    public MemberPrinter(){
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
    }

    public void print(Member member) {
        if(dateTimeFormatter == null) {
            System.out.println("MemberPrinter : " + member.toString());
            System.out.println("null");
            System.out.printf("%tF\n", member.getRegisterDateTime());
        } else {
            System.out.println("MemberPrinter : " + member.toString());
            System.out.println("not null");
            System.out.printf("%s\n", dateTimeFormatter.format(member.getRegisterDateTime()));
        }
    }

    @Autowired
    public void setDateTimeFormatter(@Nullable DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }
}
