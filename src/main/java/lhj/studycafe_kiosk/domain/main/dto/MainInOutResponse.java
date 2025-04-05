package lhj.studycafe_kiosk.domain.main.dto;

import lhj.studycafe_kiosk.domain.member.domain.MemberGrade;
import lombok.Getter;

@Getter
public class MainInOutResponse {

    private boolean mainInOut;
    private boolean login;
    private MemberGrade memberGrade;

    public void setMainInOut(boolean mainInOut) {
        this.mainInOut = mainInOut;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public void setMemberGrade(MemberGrade memberGrade) {
        this.memberGrade = memberGrade;
    }
}
