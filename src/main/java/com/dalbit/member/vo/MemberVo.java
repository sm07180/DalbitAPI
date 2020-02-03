package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;

@Getter
@Setter
public class MemberVo extends ProfileInfoOutVo {

    private static final long serialVersionUID = 1L;

    public static String getMyMemNo() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private String memSlct;
    private String memPasswd;

    public MemberVo(){}
    public MemberVo(ProfileInfoOutVo target) {
        setMemNo(target.getMemNo());
        setNickNm(target.getNickNm());
        setGender(target.getGender());
        setAge(target.getAge());
        setMemId(target.getMemId());
        setBgImg(target.getBgImg());
        setProfImg(target.getProfImg());
        setProfMsg(target.getProfMsg());
        setLevel(target.getLevel());
        setFanCnt(target.getFanCnt());
        setStarCnt(target.getStarCnt());
        setIsFan(target.getIsFan());
        setExp(target.getExp());
        setExpNext(target.getExpNext());
        setGrade(target.getGrade());
    }
}
