package lhj.studycafe_kiosk.main.dto;

import lombok.Getter;

@Getter
public class MainInOutResponse {

    private boolean mainInOut;

    public void setMainInOut(boolean mainInOut) {
        this.mainInOut = mainInOut;
    }
}
