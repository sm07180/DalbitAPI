package com.dalbit.security.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.BlockVo;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.ProcedureOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.exception.CustomUsernameNotFoundException;
import com.dalbit.member.dao.MemberDao;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.service.ProfileService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.ProfileInfoOutVo;
import com.dalbit.member.vo.procedure.P_LoginVo;
import com.dalbit.member.vo.procedure.P_ProfileInfoVo;
import com.dalbit.security.dao.LoginDao;
import com.dalbit.security.vo.MemberReportInfoVo;
import com.dalbit.security.vo.SecurityUserVo;
import com.dalbit.util.DalbitUtil;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private LoginDao loginDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ProfileService profileService;

    /*@Autowired
    private RedisUtil redisUtil;*/

    @Autowired
    private HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        HashMap map = DalbitUtil.getParameterMap(request);

        if(DalbitUtil.isEmpty(map.get("memId"))) {
            throw new CustomUsernameNotFoundException(Status.로그인실패_파라메터이상);
        }

        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        SecurityUserVo securityUserVo = new SecurityUserVo(DalbitUtil.getStringMap(map, "memId"), DalbitUtil.getStringMap(map, "memId"), authorities);
        return securityUserVo;
    }

    public UserDetails loadUserByUsername() throws CustomUsernameNotFoundException{

        HashMap map = DalbitUtil.getParameterMap(request);

        if("p".equals(DalbitUtil.getStringMap(map, "memType")) && DalbitUtil.isEmpty(DalbitUtil.getStringMap(map, "memPwd"))){
            throw new CustomUsernameNotFoundException(Status.파라미터오류);
        }

        DeviceVo deviceVo = new DeviceVo(request);
        //LocationVo locationVo = DalbitUtil.getLocation(deviceVo.getIp());

        int adminBlockCnt = memberDao.selectAdminBlock(new BlockVo(deviceVo));
        if(0 < adminBlockCnt){
            throw new CustomUsernameNotFoundException(Status.로그인실패_운영자차단);
        }


        P_LoginVo pLoginVo = new P_LoginVo(
                DalbitUtil.getStringMap(map, "memType")
                , DalbitUtil.getStringMap(map, "memId")
                , DalbitUtil.getStringMap(map, "memPwd")
                , deviceVo.getOs()
                , deviceVo.getDeviceUuid()
                , deviceVo.getDeviceToken()
                , deviceVo.getAppVersion()
                , deviceVo.getAdId()
                , "" //locationVo.getRegionName()
                , deviceVo.getIp()
                , DalbitUtil.getUserAgent(request)
                , deviceVo.getAppBuild()
        );
        pLoginVo.setRoom_no(request.getParameter("room_no") == null ? "" : request.getParameter("room_no"));

        ProcedureOutputVo LoginProcedureVo = memberService.callMemberLogin(pLoginVo);
        log.debug("로그인 결과 : {}", new Gson().toJson(LoginProcedureVo));

        HashMap loginExt = new Gson().fromJson(LoginProcedureVo.getExt(), HashMap.class);
        String memNo = DalbitUtil.getStringMap(loginExt, "mem_no");

        if(LoginProcedureVo.getRet().equals(Status.로그인실패_회원가입필요.getMessageCode())) {
            if("p".equals(DalbitUtil.getStringMap(map, "memType"))){
                throw new CustomUsernameNotFoundException(Status.로그인실패_패스워드틀림);
            }else{
                throw new CustomUsernameNotFoundException(Status.로그인실패_회원가입필요);
            }

        }else if(LoginProcedureVo.getRet().equals(Status.로그인실패_패스워드틀림.getMessageCode())) {
            throw new CustomUsernameNotFoundException(Status.로그인실패_패스워드틀림);

        }else if(LoginProcedureVo.getRet().equals(Status.로그인실패_파라메터이상.getMessageCode())) {
            throw new CustomUsernameNotFoundException(Status.로그인실패_파라메터이상);

        }else if(LoginProcedureVo.getRet().equals(Status.로그인실패_블럭상태.getMessageCode())) {

            HashMap resultMap = new Gson().fromJson(LoginProcedureVo.getExt(), HashMap.class);
            String blockDay = DalbitUtil.getStringMap(resultMap, "block_day");
            String blockEndDt = DalbitUtil.getUTCFormat(DalbitUtil.getDateMap(resultMap, "expected_end_date"));
            long blockEndTs = DalbitUtil.getUTCTimeStamp(DalbitUtil.getDateMap(resultMap, "expected_end_date"));
            HashMap returnMap = new HashMap();
            returnMap.put("blockDay", blockDay);
            returnMap.put("blockEndDt", blockEndDt);
            returnMap.put("blockEndTs", blockEndTs);

            //제재 사유조회
            MemberReportInfoVo memberReportInfoVo = loginDao.selectReportData(DalbitUtil.getStringMap(loginExt, "mem_no"));
            if(memberReportInfoVo == null){
                returnMap.put("opCode", "");
                returnMap.put("opMsg", "");
            }else{
                returnMap.put("opCode", memberReportInfoVo.getOp_code());
                returnMap.put("opMsg", memberReportInfoVo.getOp_msg());
            }

            throw new CustomUsernameNotFoundException(Status.로그인실패_블럭상태, returnMap);

        }else if(LoginProcedureVo.getRet().equals(Status.로그인실패_탈퇴.getMessageCode())) {
            throw new CustomUsernameNotFoundException(Status.로그인실패_탈퇴);

        }else if(LoginProcedureVo.getRet().equals(Status.로그인실패_영구정지.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(LoginProcedureVo.getExt(), HashMap.class);

            HashMap returnMap = new HashMap();
            //제재 사유조회
            MemberReportInfoVo memberReportInfoVo = loginDao.selectReportData(DalbitUtil.getStringMap(resultMap, "mem_no"));
            if(memberReportInfoVo == null){
                returnMap.put("opCode", "");
                returnMap.put("opMsg", "");
            }else{
                returnMap.put("opCode", memberReportInfoVo.getOp_code());
                returnMap.put("opMsg", memberReportInfoVo.getOp_msg());
            }

            throw new CustomUsernameNotFoundException(Status.로그인실패_영구정지, returnMap);
        }else if(LoginProcedureVo.getRet().equals(Status.로그인실패_청취방존재.getMessageCode())){
            HashMap resultMap = new Gson().fromJson(LoginProcedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("memNo", DalbitUtil.getStringMap(resultMap, "mem_no"));

            throw new CustomUsernameNotFoundException(Status.로그인실패_청취방존재, returnMap);
        }else if(LoginProcedureVo.getRet().equals(Status.로그인성공.getMessageCode())){
            MemberVo paramMemberVo = new MemberVo();
            paramMemberVo.setMemId(DalbitUtil.getStringMap(map, "memId"));
            paramMemberVo.setMemSlct(DalbitUtil.getStringMap(map, "memType"));

            ProcedureVo profileProcedureVo = profileService.getProfile(new P_ProfileInfoVo(1, memNo));

            MemberVo memberVo = null;
            if(profileProcedureVo.getRet().equals(Status.회원정보보기_성공.getMessageCode())) {

                P_ProfileInfoVo profileInfo = new Gson().fromJson(profileProcedureVo.getExt(), P_ProfileInfoVo.class);
                memberVo = new MemberVo(new ProfileInfoOutVo(profileInfo, memNo, memNo, null));
                memberVo.setMemSlct(DalbitUtil.getStringMap(map, "memType"));
                memberVo.setMemPasswd(DalbitUtil.getStringMap(map, "memPwd"));
            }else{
                throw new CustomUsernameNotFoundException(Status.로그인오류);
            }

            //todo - 프로시저에 관리자 구분 추가되어야함. 임시로 특정 아이디에 관리자 권한 부여
            //boolean isAdmin = memberVo.getMemSlct().equals("m");
            boolean isAdmin = DalbitUtil.isEmpty(DalbitUtil.isEmpty(memberVo) || DalbitUtil.isEmpty(memberVo.getMemId())) ? false : memberVo.getMemId().equals("huhazv74");

            SecurityUserVo securityUserVo = new SecurityUserVo(
                    memberVo.getMemId()
                    , memberVo.getMemPasswd()
                    , isAdmin ? DalbitUtil.getAdminAuthorities() : DalbitUtil.getAuthorities());
            securityUserVo.setMemberVo(memberVo);

            return securityUserVo;

        }else{
            throw new CustomUsernameNotFoundException(Status.로그인오류);
        }
    }

    /*public UserDetails loadUserBySsoCookieFromRedis(String memNo) throws UsernameNotFoundException {

        MemberVo memberVo = redisUtil.getMemberInfoFromRedis(memNo);

        if(memberVo == null) {
            return null;
        }

        SecurityUserVo securityUserVo = new SecurityUserVo(memberVo.getMemId(), memberVo.getMemId(), DalbitUtil.getAuthorities());
        securityUserVo.setMemberVo(memberVo);

        return securityUserVo;
    }*/
}
