package com.dalbit.common.vo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParentsAgreeEmailVo {
    public ParentsAgreeEmailVo() {}
    public ParentsAgreeEmailVo(String agreeAllowUserName, String agreeRcvUserName, String agreeRcvUserId,
                               String agreeDuration, String agreeExpireDate, String email) {
        this.agreeAllowUserName = agreeAllowUserName;
        this.agreeRcvUserName = agreeRcvUserName;
        this.agreeRcvUserId = agreeRcvUserId;
        this.agreeDuration = agreeDuration;
        this.agreeExpireDate = agreeExpireDate;
        this.email = email;
    }
    private String agreeAllowUserName; // 법정대리인 이름
    private String agreeRcvUserName; // 유저 이름
    private String agreeRcvUserId; // 유저 아이디
    private String agreeDuration; // 동의 기간
    private String agreeExpireDate; // 만료 기간
    private String email; // 법정대리인 이메일
}
