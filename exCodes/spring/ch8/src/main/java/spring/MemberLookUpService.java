package spring;

public class MemberLookUpService {
    private MemberDao memberDao;

    public MemberLookUpService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member getMember(String email, String password) {
        Member member = memberDao.selectByEmail(email);
        if (member == null) {
            throw new MemberNotFoundException();
        }
        if (!member.getPassword().equals(password)) {
            throw new WrongIdPasswordException();
        }
        return member;
    }
}
