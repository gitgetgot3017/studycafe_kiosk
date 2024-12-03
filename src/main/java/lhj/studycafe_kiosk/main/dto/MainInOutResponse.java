package lhj.studycafe_kiosk.main.dto;

import lombok.Getter;

@Getter
public class MainInOutResponse {

    private boolean mainInOut;
    private boolean login;

    public void setMainInOut(boolean mainInOut) {
        this.mainInOut = mainInOut;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }
}
