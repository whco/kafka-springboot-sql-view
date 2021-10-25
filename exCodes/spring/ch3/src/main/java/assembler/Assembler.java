package assembler;

import spring.ChangePasswordService;
import spring.MemberDao;
import spring.MemberLookUpService;
import spring.MemberRegisterService;

public class Assembler {

    private MemberDao memberDao;
    private MemberRegisterService registerService;
    private ChangePasswordService changePasswordService;
    private MemberLookUpService memberLookUpService;

    public Assembler() {
        memberDao = new MemberDao();
        registerService = new MemberRegisterService(memberDao);
        changePasswordService = new ChangePasswordService();
        changePasswordService.setMemberDao(memberDao);
        memberLookUpService = new MemberLookUpService(memberDao);
    }

    public MemberDao getMemberDao() {
        return memberDao;
    }

    public MemberRegisterService getRegisterService() {
        return registerService;
    }

    public ChangePasswordService getChangePasswordService() {
        return changePasswordService;
    }

    public MemberLookUpService getMemberLookUpService() {
        return memberLookUpService;
    }
}
