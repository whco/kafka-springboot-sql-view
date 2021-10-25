package spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ChangePasswordService {
    @Autowired
    private MemberDao memberDao;

    @Transactional
    public void changePassword(String email, String oldPwd, String newPwd) {
        Member member = memberDao.selectByEmail(email);

        if (member == null) {
            throw new MemberNotFoundException();
        }
        member.changePassword(oldPwd, newPwd);
        memberDao.update(member);
//        if(true)throw new MemberNotFoundException();
    }

    public void setMemberDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }
}
