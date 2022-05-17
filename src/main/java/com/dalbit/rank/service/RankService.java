package com.dalbit.rank.service;

import com.dalbit.common.code.CommonStatus;
import com.dalbit.common.code.MemberStatus;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.member.dao.MypageDao;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.rank.proc.Rank;
import com.dalbit.rank.proc.StarDjPage;
import com.dalbit.rank.vo.StarDjPointVO;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RankService {
    @Autowired Rank rank;
    @Autowired GsonUtil gsonUtil;
    @Autowired StarDjPage starDjPage;
    @Autowired MypageDao mypageDao;

    /**********************************************************************************************
     * @Method 설명 : 파트너DJ리스트
     * @작성일 : 2022-04-19
     * @작성자 : 강알찬
     * @변경이력 :
     **********************************************************************************************/
    public String getPartnerDjList(Map map, HttpServletRequest request){
        String memNo = MemberVo.getMyMemNo(request);
        map.put("memNo", memNo);
        List<Object> djResult = rank.getPartnerDjList(map);
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.조회, djResult));
    }

    /**********************************************************************************************
     * @Method 설명 : 내 스디 점수
     * @작성일 : 2022-04-19
     * @작성자 : 강알찬
     * @변경이력 :
     **********************************************************************************************/
    public String getStarDjScore(Map map, HttpServletRequest request){
        String memNo = MemberVo.getMyMemNo(request);
        map.put("memNo", memNo);
        StarDjPointVO result = starDjPage.getStarDjScore(map);
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.조회, result));
    }

    /**********************************************************************************************
     * @Method 설명 : 스타디제이 신청
     * @작성일 : 2022-04-19
     * @작성자 : 강알찬
     * @변경이력 :
     **********************************************************************************************/
    public String starDjIns(Map map, HttpServletRequest request){
        String result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.스페셜DJ_신청실패));
        String memNo = MemberVo.getMyMemNo(request);
        map.put("memNo", memNo);
        try{
            HashMap starMap = mypageDao.selectExistsSpecialReq(memNo);
            if (starMap.get("is_already") != null){
                if (Integer.parseInt(starMap.get("is_already").toString()) < 1){
                    starDjPage.starDjIns(map);
                    result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.스페셜DJ_신청성공));
                } else {
                    result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.스페셜DJ_신청실패));
                }
            } else {
                result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.스페셜DJ_신청실패));
            }

        }catch(Exception e){
            log.info("스페셜 DJ DB 오류 {}", e.getMessage());
        }
        return result;
    }

    /**********************************************************************************************
     * @Method 설명 : 스타디제이 약력
     * @작성일 : 2022-04-20
     * @작성자 : 강알찬
     * @변경이력 :
     **********************************************************************************************/
    public String getStarDjLog(Map map, HttpServletRequest request){
        String memNo = MemberVo.getMyMemNo(request);
        if (map.get("memNo") == null){
            map.put("memNo", memNo);
        }
        List<Object> result = starDjPage.getStarDjLog(map);
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.조회, result));
    }

}
