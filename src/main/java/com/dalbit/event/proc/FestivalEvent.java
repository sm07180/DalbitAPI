package com.dalbit.event.proc;

import com.dalbit.event.vo.FestivalGroundDjInfoVo;
import com.dalbit.event.vo.FestivalGroundInputVo;
import com.dalbit.event.vo.FestivalGroundViewerInfoVo;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
public interface FestivalEvent {
    /**********************************************************************************************
    * @Method 설명 : ##### 이주년 이벤트 선물박스 지급 및 인증 체크
    * @작성일   : 2022-06-08
    * @작성자   : 박성민
    * @param   : memNo, memPhone
    * @return  : -3:본인 미인증  -2:다른 계정으로 선물 받음, -1: 회원 없음,  0: 에러, 1:정상
    **********************************************************************************************/
    @Select("CALL p_two_year_evt_gift_box_ins(#{memNo}, #{memPhone})")
    Integer twoYearGiftBoxIns(String memNo, String memPhone);

    /**********************************************************************************************
     * @Method 설명 : ##### 이주년 이벤트 선물박스 지급 회원 체크
     * @작성일   : 2022-06-08
     * @작성자   : 박성민
     * @param   : memNo
     * @return  : -1: 선물 받음,  0: 에러, 1:지급 대상 회원
     **********************************************************************************************/
    @Select("CALL p_two_year_evt_gift_box_mem_chk(#{memNo})")
    Integer twoYearGiftBoxCheck(String memNo);

    /**********************************************************************************************
     * @Method 설명 : ##### 이주년 이벤트 사연  리스트
     * @작성일   : 2022-06-08
     * @작성자   : 박성민
     * @param   : memNo (0: 전체), pageNo, pagePerCnt
     * @return  :
     *  #1
     *   cnt		BIGINT		-- 전체 수
     *  #2
     *   auto_no           자동등록 번호
     *   story_conts       스토리내용
     *   mem_no            회원 번호
     *   mem_id            회원 아이디
     *   mem_nick          회원 닉네임
     *   mem_sex           회원성별
     *   image_profile     프로필
     *   mem_level         레벨
     *   mem_state         회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...)
     *   ins_date          등록일자
     *   upd_date          수정일자
     **********************************************************************************************/
    @ResultMap({"ResultMap.integer", "ResultMap.FestivalStoryVo"})
    @Select("CALL p_two_year_evt_mem_story_list(#{memNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> twoYearStorySelect(String memNo, String pageNo, String pagePerCnt);

    /**********************************************************************************************
     * @Method 설명 : ##### 이주년 이벤트 사연 등록
     * @작성일   : 2022-06-08
     * @작성자   : 박성민
     * @param   : memNo, storyConts: 사연 내용
     * @return  : 0: 에러, 1:정상
     **********************************************************************************************/
    @Select("CALL p_two_year_evt_mem_story_ins(#{memNo}, #{storyConts})")
    Integer twoYearStoryIns(String memNo, String storyConts);

    /**********************************************************************************************
     * @Method 설명 : ##### 이주년 이벤트 사연 수정
     * @작성일   : 2022-06-08
     * @작성자   : 박성민
     * @param   : memNo, storyConts: 사연 내용
     * @return  : 0: 에러, 1:정상
     **********************************************************************************************/
    @Select("CALL p_two_year_evt_mem_story_upd(#{storyNo}, #{storyConts})")
    Integer twoYearStoryUpd(String storyNo, String storyConts);

    /**********************************************************************************************
     * @Method 설명 : ##### 이주년 이벤트 사연 삭제
     * @작성일   : 2022-06-08
     * @작성자   : 박성민
     * @param   : memNo, storyConts: 사연 내용
     * @return  : -1:데이터 없음,0: 에러, 1:정상
     **********************************************************************************************/
    @Select("CALL p_two_year_evt_mem_story_del(#{storyNo}, #{delChrgrName})")
    Integer twoYearStoryDel(String storyNo, String delChrgrName);

    /**********************************************************************************************
     * @Method 설명 : ##### 이주년 이벤트 달라그라운드 dj 리스트
     * @작성일   : 2022-06-08
     * @작성자   : 박성민
     * @param   : seqNo: 회차번호[1,2], pageNo, pagePerCnt
     **********************************************************************************************/
    @ResultMap({"ResultMap.integer", "ResultMap.FestivalGroundDjListVo"})
    @Select("CALL p_two_year_evt_dalla_ground_dj_list(#{seqNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> twoYearGroundDjList(FestivalGroundInputVo param);

    /**********************************************************************************************
     * @Method 설명 : ##### 이주년 이벤트 달라그라운드 dj 회원정보
     * @작성일   : 2022-06-08
     * @작성자   : 박성민
     * @param   : seqNo: 회차번호[1,2], memNo
     **********************************************************************************************/
    @Select("CALL p_two_year_evt_dalla_ground_dj_sel(#{seqNo}, #{memNo})")
    FestivalGroundDjInfoVo twoYearGroundDjInfoSel(FestivalGroundInputVo param);

    /**********************************************************************************************
     * @Method 설명 : ##### 이주년 이벤트 달라그라운드 회원 리스트
     * @작성일   : 2022-06-08
     * @작성자   : 박성민
     * @param   : seqNo: 회차번호[1,2], pageNo, pagePerCnt
     **********************************************************************************************/
    @ResultMap({"ResultMap.integer", "ResultMap.FestivalGroundViewerListVo"})
    @Select("CALL p_two_year_evt_dalla_ground_mem_list(#{seqNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> twoYearGroundViewerList(FestivalGroundInputVo param);

    /**********************************************************************************************
     * @Method 설명 : ##### 이주년 이벤트 달라그라운드 회원정보
     * @작성일   : 2022-06-08
     * @작성자   : 박성민
     * @param   : seqNo: 회차번호[1,2], memNo
     **********************************************************************************************/
    @Select("CALL p_two_year_evt_dalla_ground_mem_sel(#{seqNo}, #{memNo})")
    FestivalGroundViewerInfoVo twoYearGroundViewerInfoSel(FestivalGroundInputVo param);
}
