package spring;

public class MemberInfoPrinter {
    private MemberDao memberDao;
    private MemberPrinter memberPrinter;

    public void printMemmberInfo(String email) {
        Member member = memberDao.selectByEmail(email);
        if (member == null) {
            System.out.println("no data");
        }

        memberPrinter.print(member);
        System.out.println();
    }

    public void setMemberDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public void setMemberPrinter(MemberPrinter memberPrinter) {
        this.memberPrinter = memberPrinter;
    }
}
