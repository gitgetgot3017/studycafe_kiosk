package lhj.studycafe_kiosk.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_name")
    private String name;

    @Column(unique = true)
    private String phone;

    private String password;

    private String salt;

    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    private MemberGrade grade;

    private boolean optionalClause;

    public Member(String name, String phone, String password, LocalDate birth, boolean optionalClause) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.birth = birth;
        this.optionalClause = optionalClause;
    }

    public void changeGeneralInfo(String name, LocalDate birth) {
        this.name = name;
        this.birth = birth;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
