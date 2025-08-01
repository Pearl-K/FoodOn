package com.foodon.foodon.member.dto;

import com.foodon.foodon.member.domain.Member;

public record MemberDto(
        Long id,
        String nickname,
        String email
) {
    public static MemberDto from(Member member) {
        return new MemberDto(
                member.getId(),
                member.getNickname(),
                member.getEmail()
        );
    }
}
