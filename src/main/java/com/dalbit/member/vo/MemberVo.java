package com.dalbit.member.vo;

import com.dalbit.util.DalbitUtil;
import com.dalbit.util.JwtUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Getter
@Setter
public class MemberVo extends ProfileInfoOutVo {

    private static final long serialVersionUID = 1L;

    public static String getMyMemNo(HttpServletRequest request) {
        try{
            JwtUtil jwtUtil = new JwtUtil(DalbitUtil.getProperty("spring.jwt.secret"), Integer.parseInt(DalbitUtil.getProperty("spring.jwt.duration")));
            TokenVo tokenVo = jwtUtil.getTokenVoFromJwt(request.getHeader(DalbitUtil.getProperty("sso.header.cookie.name")));
            if(tokenVo != null){
                return tokenVo.getMemNo();
            }
        }catch (Exception e){
            log.debug("MemberVo.getMyMemNo(request) return null : {}", e.getMessage());
        }
        return null;
    }

    public static String getMyMemNo() {
        try{
            return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }catch (Exception e){
            log.debug("MemberVo.getMyMemNo() return null : {}", e.getMessage());
            return null;
        }
    }

    public static boolean isAdmin(){
        try{
            return 0 < SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().filter(auth -> auth.getAuthority().equals("ROLE_ADMIN")).count();
        }catch (Exception e){
            log.debug("MemberVo.isAdmin() return false : {}", e.getMessage());
            return false;
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
