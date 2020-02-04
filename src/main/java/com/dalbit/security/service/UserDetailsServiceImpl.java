package com.dalbit.security.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.LocationVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.exception.CustomUsernameNotFoundException;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.service.ProfileService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.P_LoginVo;
import com.dalbit.member.vo.P_ProfileInfoVo;
import com.dalbit.member.vo.ProfileInfoOutVo;
import com.dalbit.security.dao.LoginDao;
import com.dalbit.security.vo.SecurityUserVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.RedisUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private LoginDao loginDao;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        HashMap map = getParameterMap(request);

        if(DalbitUtil.isEmpty(map.get("memId")) || DalbitUtil.isEmpty(map.get("memPwd"))) {
            throw new CustomUsernameNotFoundException(Status.로그인실패_파라메터이상);
        }

        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        SecurityUserVo securityUserVo = new SecurityUserVo(DalbitUtil.getStringMap(map, "memId"), DalbitUtil.getStringMap(map, "memPwd"), authorities);
        return securityUserVo;
    }


    public UserDetails loadUserByUsername() throws UsernameNotFoundException {

        HashMap map = getParameterMap(request);

        LocationVo locationVo = DalbitUtil.getLocation(request);

        P_LoginVo pLoginVo = new P_LoginVo(
                DalbitUtil.getStringMap(map, "memType")
                , DalbitUtil.getStringMap(map, "memId")
                , DalbitUtil.getStringMap(map, "memPwd")
                , DalbitUtil.getIntMap(map, "os")
                , DalbitUtil.getStringMap(map, "deviceId")
                , DalbitUtil.getStringMap(map, "deviceToken")
                , DalbitUtil.getStringMap(map, "appVer")
                , DalbitUtil.getStringMap(map, "appAdId")
                , locationVo.getRegionName()
        );

        ProcedureVo LoginProcedureVo = memberService.callMemberLogin(pLoginVo);
        log.debug("로그인 결과 : {}", new Gson().toJson(LoginProcedureVo));

        HashMap loginExt = new Gson().fromJson(LoginProcedureVo.getExt(), HashMap.class);
        String memNo = DalbitUtil.getStringMap(loginExt, "mem_no");

        if(LoginProcedureVo.getRet().equals(Status.로그인실패_회원가입필요.getMessageCode())) {
            throw new CustomUsernameNotFoundException(Status.로그인실패_회원가입필요);

        }else if(LoginProcedureVo.getRet().equals(Status.로그인실패_패스워드틀림.getMessageCode())) {
            throw new CustomUsernameNotFoundException(Status.로그인실패_패스워드틀림);

        }else if(LoginProcedureVo.getRet().equals(Status.로그인실패_파라메터이상.getMessageCode())) {
            throw new CustomUsernameNotFoundException(Status.로그인실패_파라메터이상.getMessageKey());
        }

        MemberVo paramMemberVo = new MemberVo();
        paramMemberVo.setMemId(DalbitUtil.getStringMap(map, "memId"));
        paramMemberVo.setMemSlct(DalbitUtil.getStringMap(map, "memType"));

        ProcedureVo profileProcedureVo = profileService.getProfile(new P_ProfileInfoVo(1, memNo));

        MemberVo memberVo = null;
        if(profileProcedureVo.getRet().equals(Status.회원정보보기_성공.getMessageCode())) {

            P_ProfileInfoVo profileInfo = new Gson().fromJson(profileProcedureVo.getExt(), P_ProfileInfoVo.class);
            memberVo = new MemberVo(new ProfileInfoOutVo(profileInfo, memNo));
            memberVo.setMemSlct(DalbitUtil.getStringMap(map, "memType"));
            memberVo.setMemPasswd(DalbitUtil.getStringMap(map, "memPwd"));
        }else{
            new CustomUsernameNotFoundException(Status.로그인실패_패스워드틀림);
        }

        SecurityUserVo securityUserVo = new SecurityUserVo(memberVo.getMemId(), memberVo.getMemPasswd(), DalbitUtil.getAuthorities());
        securityUserVo.setMemberVo(memberVo);

        return securityUserVo;
    }


    /*@Deprecated
    public UserDetails loadUserBySsoCookieFromDb(String memNo) throws UsernameNotFoundException {

        MemberVo memberVo = loginDao.loginUseMemNo(memNo);

        if(memberVo == null) {
            throw new CustomUsernameNotFoundException(Status.로그인실패_패스워드틀림);
        }

        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        SecurityUserVo securityUserVo = new SecurityUserVo(memberVo.getMemId(), memberVo.getMemPasswd(), authorities);
        securityUserVo.setMemberVo(memberVo);

        return securityUserVo;
    }*/

    public UserDetails loadUserBySsoCookieFromRedis(String memNo) throws UsernameNotFoundException {

        MemberVo memberVo = redisUtil.getMemberInfoFromRedis(memNo);

        if(memberVo == null) {
            return null;
        }

        SecurityUserVo securityUserVo = new SecurityUserVo(memberVo.getMemId(), "", DalbitUtil.getAuthorities());
        securityUserVo.setMemberVo(memberVo);

        return securityUserVo;
    }

    public HashMap getParameterMap(HttpServletRequest request){

        String memType = DalbitUtil.convertRequestParamToString(request,"memType");
        String memId = DalbitUtil.convertRequestParamToString(request,"memId");
        String memPwd = DalbitUtil.convertRequestParamToString(request,"memPwd");
        int os = DalbitUtil.convertRequestParamToInteger(request,"os");
        String deviceId = DalbitUtil.convertRequestParamToString(request,"deviceId");
        String deviceToken = DalbitUtil.convertRequestParamToString(request,"deviceToken");
        String appVer = DalbitUtil.convertRequestParamToString(request,"appVer");
        String appAdId = DalbitUtil.convertRequestParamToString(request,"appAdId");

        HashMap map = new HashMap();
        map.put("memType", memType);
        map.put("memId", memId);
        map.put("memPwd", memPwd);
        map.put("os", os);
        map.put("deviceId", deviceId);
        map.put("deviceToken", deviceToken);
        map.put("appVer", appVer);
        map.put("appAdId", appAdId);
        return map;
    }
}
