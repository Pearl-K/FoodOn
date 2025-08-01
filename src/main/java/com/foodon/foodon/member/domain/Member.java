package com.foodon.foodon.member.domain;

import com.foodon.foodon.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "members")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = true, length = 100)
    private String email;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(length = 1000)
    private String profileImage;

    @Column(nullable = false)
    private boolean profileUpdated;

    private LocalDate birthday;

    private Gender gender;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    private Member (
            String nickname,
            String email,
            String profileImgUrl,
            LocalDate birthday
    ){
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImgUrl;
        this.birthday = birthday;
    }

    public static Member createMember(
            String nickname,
            String email,
            String profileImgUrl
    ) {
        return new Member(
                nickname,
                email,
                profileImgUrl,
                LocalDate.now().minusYears(40)
        );
    }

    public void updateProfile(
            Gender gender
    ) {
        this.gender = gender;
    }

    public int getAge(){
        return Period.between(birthday, LocalDate.now()).getYears();
    }

    public void markProfileUpdated() {
        this.profileUpdated = true;
    }

    public void updateLastLoginTime() {
        this.lastLoginAt = LocalDateTime.now();
    }
}
