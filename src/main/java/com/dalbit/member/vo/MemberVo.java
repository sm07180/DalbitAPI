package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@Getter
@Setter
public class MemberVo extends ProfileInfoOutVo {

    private static final long serialVersionUID = 1L;

    public static String getMyMemNo() {
        try{
            return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }catch (Exception e){
            log.warn("MemberVo.getMyMemNo() return null : {}", e.getMessage());
            return null;
        }
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
