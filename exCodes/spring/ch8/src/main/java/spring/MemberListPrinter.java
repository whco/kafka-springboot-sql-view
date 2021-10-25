package spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("listPrinter")
public class MemberListPrinter {
    private MemberDao memberDao;
    private MemberPrinter memberPrinter;

//    public MemberListPrinter() {
//    }
//    @Autowired
//    public void setMemberDao(MemberDao memberDao) {
//        this.memberDao = memberDao;
//    }
//    @Autowired
//    public void setMemberPrinter(MemberSummaryPrinter memberPrinter) {
//        this.memberPrinter = memberPrinter;
//    }

    public MemberListPrinter(MemberDao memberDao, MemberPrinter memberPrinter) {
        this.memberDao = memberDao;
        this.memberPrinter = memberPrinter;
    }

    public void printAll() {
        Collection<Member> members = memberDao.selectAll();
        members.forEach(m -> memberPrinter.print(m));
    }
}
