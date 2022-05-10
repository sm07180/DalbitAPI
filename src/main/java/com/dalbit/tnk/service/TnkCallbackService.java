package com.dalbit.tnk.service;

import com.dalbit.common.code.ErrorStatus;
import com.dalbit.common.code.MemberStatus;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.tnk.dao.TnkCallbackDao;
import com.dalbit.tnk.vo.DBTnkCallbackVo;
import com.dalbit.tnk.vo.TnkCallbackVo;
import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Slf4j
@Service
public class TnkCallbackService {

    @Value("${tnk.aos.app.key}")
    private String TNK_AOS_KEY;
    @Value("${tnk.ios.app.key}")
    private String TNK_IOS_KEY;

    @Autowired
    private TnkCallbackDao tnkCallbackDao;

    public void callbackTnk(TnkCallbackVo tnkCallbackVo, String os, HttpServletResponse response) throws GlobalException{
        String tnkKey = "";
        if("aos".equals(os)) {
            tnkKey = TNK_AOS_KEY;
        }else if("ios".equals(os)){
            tnkKey = TNK_IOS_KEY;
        }

        if(DalbitUtil.isEmpty(tnkKey) || DalbitUtil.isEmpty(tnkCallbackVo.getMd_user_nm()) || DalbitUtil.isEmpty(tnkCallbackVo.getSeq_id()) || DalbitUtil.isEmpty(tnkCallbackVo.getMd_chk()) || tnkCallbackVo.getPay_pnt() < 1){
            response.setStatus(503);
            throw new GlobalException(ErrorStatus.잘못된파람, "post");
        }

        String verifyKey = DigestUtils.md5Hex(tnkKey + tnkCallbackVo.getMd_user_nm() + tnkCallbackVo.getSeq_id());
        log.error("{} : {}", verifyKey, tnkCallbackVo.getMd_chk());
        if(verifyKey == null || !verifyKey.equals(tnkCallbackVo.getMd_chk())){
            response.setStatus(403);
            throw new GlobalException(ErrorStatus.권한없음, "post");
        }

        ProcedureVo procedureVo = new ProcedureVo(new DBTnkCallbackVo(tnkCallbackVo));
        tnkCallbackDao.callTnkCallback(procedureVo);
        if(MemberStatus.TNK_성공.getMessageCode().equals(procedureVo.getRet())) {
        } else if(MemberStatus.TNK_회원아님.getMessageCode().equals(procedureVo.getRet())) {
        } else if(MemberStatus.TNK_이미받음.getMessageCode().equals(procedureVo.getRet())) {
        } else {
            response.setStatus(503);
            throw new GlobalException(MemberStatus.TNK_실패, "post");
        }
    }

    public HashMap callTnkConfirm(HttpServletRequest request) throws GlobalException{
        HashMap result = new HashMap();
        result.put("chargedDal", 0);

        HashMap reqMap = new HashMap();
        reqMap.put("mem_no", MemberVo.getMyMemNo(request));
        ProcedureVo procedureVo = new ProcedureVo(reqMap);
        tnkCallbackDao.callTnkConfirm(procedureVo);
        if("0".equals(procedureVo.getRet())){
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            result.put("chargedDal", DalbitUtil.getIntMap(resultMap, "chargeDal"));
        }

        return result;
    }
}
