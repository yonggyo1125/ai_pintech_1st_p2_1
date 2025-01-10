package org.koreait.member.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.koreait.file.entities.FileInfo;
import org.koreait.global.entities.BaseEntity;
import org.koreait.member.constants.Gender;
import org.koreait.member.social.constants.SocialChannel;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Member extends BaseEntity implements Serializable {
    @Id @GeneratedValue
    private Long seq; // 회원 번호

    @Column(length=65, nullable = false, unique = true)
    private String email; // 이메일

    @Column(length=65)
    private String password;

    @Column(length=40, nullable = false)
    private String name;

    @Column(length=40, nullable = false)
    private String nickName;

    @Column(nullable = false)
    private LocalDate birthDt; // 생년월일

    @Enumerated(EnumType.STRING)
    @Column(length=10, nullable = false)
    private Gender gender;

    @Column(length=10, nullable = false)
    private String zipCode;

    @Column(length=100, nullable = false)
    private String address;

    @Column(length=100)
    private String addressSub;

    private boolean requiredTerms1;

    private boolean requiredTerms2;

    private boolean requiredTerms3;

    @Column(length=50)
    private String optionalTerms; // 선택 약관

    @Enumerated(EnumType.STRING)
    @Column(length=20)
    private SocialChannel socialChannel; // 소설 로그인 채널

    @Column(length=65)
    private String socialToken; // 소셜 로그인 기본 ID

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "member")
    private List<Authorities> authorities;

    // 비밀번호 변경 일시
    private LocalDateTime credentialChangedAt;

    @Transient
    private FileInfo profileImage;

    // 카카오 로그인 연동된 상태인지
    public boolean isKakaoConnected() {
        return socialChannel != null && socialChannel == socialChannel.KAKAO && StringUtils.hasText(socialToken);
    }
}
