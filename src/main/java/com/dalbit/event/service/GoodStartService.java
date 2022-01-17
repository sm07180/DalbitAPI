package com.dalbit.event.service;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.common.vo.ResVO;
import com.dalbit.event.proc.GoodStartEvent;
import com.dalbit.event.vo.*;
import com.dalbit.event.vo.request.GoodStartInputVo;
import com.dalbit.util.DBUtil;
import com.dalbit.util.DalbitUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Slf4j
@Service
public class GoodStartService {
    @Autowired GoodStartEvent goodStartEvent;

    /**********************************************************************************************
    * @Method 설명 : goodStart 이벤트 회차
    * @작성일   : 2022-01-10
    * @작성자   : 박성민
    * @변경이력  : noSlct : 일정구분[1:해당회차, 2,전체회차]
    **********************************************************************************************/
    public List<GoodStartInfoVo> goodStartInfo(int noSlct) {
        List<GoodStartInfoVo> goodStartInfoList = new ArrayList<>();
        try {
            goodStartInfoList = goodStartEvent.goodStartNoSel(noSlct);
        } catch (Exception e) {
            log.error("GoodStartService / goodStartInfo error : ", e);
        }
        return goodStartInfoList;
    }

    /**********************************************************************************************
     * @Method 설명 : 굿스타트 DJ 페이지
     * @작성일   : 2022-01-08
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    public ResVO goodStartDjPage(GoodStartInputVo goodStartInputVo) {
        ResVO resVO = new ResVO();
        HashMap<String, Object> djPageData = new HashMap<>();

        // dj 랭킹
        djPageData.put("djRank", goodStartDjRank(goodStartInputVo).getData());

        // 전체 이벤트 회차
        djPageData.put("eventNoInfo", goodStartInfo(2));

        resVO.setSuccessResVO(djPageData);
        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 굿스타트 DJ 랭킹
     * @작성일   : 2022-01-10
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    public ResVO goodStartDjRank(GoodStartInputVo goodStartInputVo) {
        ResVO resVO = new ResVO();
        try {
            HashMap<String, Object> bjRankData = new HashMap<>();

            // 이벤트 회차
            GoodStartInfoVo goodStartInfoVo = goodStartInfo(1).get(0);
            goodStartInputVo.setGoodNo(goodStartInfoVo.getGood_no());

            // BJ 순위 리스트
            List<Object> rankBjObj = goodStartEvent.goodStartRankBjList(goodStartInputVo);
            Integer rankBjListCnt = DBUtil.getData(rankBjObj, 0, Integer.class);
            List<GoodStartRankVo> rankBjList = DBUtil.getList(rankBjObj, 1, GoodStartRankVo.class);
            for(GoodStartRankVo vo : rankBjList) {
                vo.setProfImg(new ImageVo(vo.getImage_profile(), vo.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
                int level = vo.getMem_level();
                int l = (level - 1) / 10;
                vo.setHolderBg(DalbitUtil.getLevelFrameBg(level));
                vo.setLevelColor(DalbitUtil.getProperty("level.color."+l).split(","));
            }
            bjRankData.put("rankDjListCnt", rankBjListCnt);
            bjRankData.put("rankDjList", rankBjList);

            // BJ 내순위
            GoodStartRankBjMySelVo goodStartRankBjMySelVo = goodStartEvent.goodStartRankBjMySel(goodStartInputVo);
            if(goodStartRankBjMySelVo != null) {
                goodStartRankBjMySelVo.setProfImg(
                    new ImageVo(goodStartRankBjMySelVo.getImage_profile()
                        , goodStartRankBjMySelVo.getMem_sex()
                        , DalbitUtil.getProperty("server.photo.url"))
                );

                int myLevel = goodStartRankBjMySelVo.getMem_level();
                int l = (myLevel - 1) / 10;
                goodStartRankBjMySelVo.setHolderBg(DalbitUtil.getLevelFrameBg(myLevel));
                goodStartRankBjMySelVo.setLevelColor(DalbitUtil.getProperty("level.color."+l).split(","));
            }
            bjRankData.put("rankDjMyInfo", goodStartRankBjMySelVo);

            resVO.setSuccessResVO(bjRankData);
        } catch (Exception e) {
            log.error("GoodStartService / goodStartDjRank ", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 굿스타트 DJ 신입 랭킹
     * @작성일   : 2022-01-10
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    public ResVO goodStartDjNewRank(GoodStartInputVo goodStartInputVo) {
        ResVO resVO = new ResVO();
        try {
            HashMap<String, Object> bjPageData = new HashMap<>();

            // 이벤트 회차
            GoodStartInfoVo goodStartInfoVo = goodStartInfo(1).get(0);
            goodStartInputVo.setGoodNo(goodStartInfoVo.getGood_no());

            // 신입랭킹 리스트
            List<Object> rankNewBjObj = goodStartEvent.goodStartRankNewBjSel(goodStartInputVo);
            Integer rankNewBjListCnt = DBUtil.getData(rankNewBjObj, 0, Integer.class);
            List<GoodStartRankVo> rankNewBjList = DBUtil.getList(rankNewBjObj, 1, GoodStartRankVo.class);
            for(GoodStartRankVo vo : rankNewBjList) {
                vo.setProfImg(new ImageVo(vo.getImage_profile(), vo.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
                int myLevel = vo.getMem_level();
                int l = (myLevel - 1) / 10;
                vo.setHolderBg(DalbitUtil.getLevelFrameBg(myLevel));
                vo.setLevelColor(DalbitUtil.getProperty("level.color."+l).split(","));
            }
            bjPageData.put("rankNewDjListCnt", rankNewBjListCnt);
            bjPageData.put("rankNewDjList", rankNewBjList);

            // 신입랭킹 내순위
            GoodStartRankNewBjMySelVo goodStartRankNewBjMySelVo = goodStartEvent.goodStartRankNewBjMySel(goodStartInputVo);
            if(goodStartRankNewBjMySelVo != null) {
                goodStartRankNewBjMySelVo.setProfImg(
                    new ImageVo(goodStartRankNewBjMySelVo.getImage_profile()
                        , goodStartRankNewBjMySelVo.getMem_sex()
                        , DalbitUtil.getProperty("server.photo.url"))
                );

                int myLevel = goodStartRankNewBjMySelVo.getMem_level();
                int l = (myLevel - 1) / 10;
                goodStartRankNewBjMySelVo.setHolderBg(DalbitUtil.getLevelFrameBg(myLevel));
                goodStartRankNewBjMySelVo.setLevelColor(DalbitUtil.getProperty("level.color."+l).split(","));
            }
            bjPageData.put("rankNewDjMyInfo", goodStartRankNewBjMySelVo);

            resVO.setSuccessResVO(bjPageData);
        } catch (Exception e) {
            log.error("GoodStartService / goodStartDjNewRank ", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 굿스타트 Fan 페이지
     * @작성일   : 2022-01-08
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    public ResVO goodStartFanPage(GoodStartInputVo goodStartInputVo) {
        ResVO resVO = new ResVO();
        try {
            HashMap<String, Object> fanPageData = new HashMap<>();

            // fan 순위 리스트
            fanPageData.put("fanRank", goodStartFanRank(goodStartInputVo).getData());

            // 전체 이벤트 회차
            fanPageData.put("eventNoInfo", goodStartInfo(2));

            resVO.setSuccessResVO(fanPageData);
        } catch (Exception e) {
            log.error("GoodStartService / goodStartFanPage ", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /**********************************************************************************************
    * @Method 설명 : 이벤트 팬 랭킹
    * @작성일   : 2022-01-10
    * @작성자   : 박성민
    * @변경이력  :
    **********************************************************************************************/
    public ResVO goodStartFanRank(GoodStartInputVo goodStartInputVo) {
        ResVO resVO = new ResVO();
        HashMap<String, Object> fanRankData = new HashMap<>();
        try {
            // 이벤트 회차
            GoodStartInfoVo goodStartInfoVo = goodStartInfo(1).get(0);
            goodStartInputVo.setGoodNo(goodStartInfoVo.getGood_no());

            List<Object> rankFanObj = goodStartEvent.goodStartRankFanList(goodStartInputVo);
            Integer rankFanListCnt = DBUtil.getData(rankFanObj, 0, Integer.class);

            List<GoodStartRankVo> rankFanList = DBUtil.getList(rankFanObj, 1, GoodStartRankVo.class);
            for(GoodStartRankVo vo : rankFanList) {
                vo.setProfImg(new ImageVo(vo.getImage_profile(), vo.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
                int myLevel = vo.getMem_level();
                int l = (myLevel - 1) / 10;
                vo.setHolderBg(DalbitUtil.getLevelFrameBg(myLevel));
                vo.setLevelColor(DalbitUtil.getProperty("level.color."+l).split(","));
            }
            fanRankData.put("rankFanListCnt", rankFanListCnt);
            fanRankData.put("rankFanList", rankFanList);

            // 팬 내순위
            GoodStartRankFanMySelVo goodStartRankFanMySel = goodStartEvent.goodStartRankFanMySel(goodStartInputVo);
            if(goodStartRankFanMySel != null) {
                goodStartRankFanMySel.setProfImg(
                    new ImageVo(goodStartRankFanMySel.getImage_profile()
                        , goodStartRankFanMySel.getMem_sex()
                        , DalbitUtil.getProperty("server.photo.url"))
                );

                int myLevel = goodStartRankFanMySel.getMem_level();
                int l = (myLevel - 1) / 10;
                goodStartRankFanMySel.setHolderBg(DalbitUtil.getLevelFrameBg(myLevel));
                goodStartRankFanMySel.setLevelColor(DalbitUtil.getProperty("level.color."+l).split(","));
            }
            fanRankData.put("rankFanMyInfo", goodStartRankFanMySel);
            resVO.setSuccessResVO(fanRankData);
        } catch (Exception e) {
            log.error("GoodStartService / goodStartFanRank ", e);
            resVO.setFailResVO();
        }

        return resVO;
    }
}
