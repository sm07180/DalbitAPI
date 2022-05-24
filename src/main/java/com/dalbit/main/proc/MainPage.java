package com.dalbit.main.proc;

import com.dalbit.main.vo.MainSwiperVO;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
@Repository
public interface MainPage {
    /**********************************************************************************************
     * @프로시저 설명    : 메인 마이스타 리스트
     * @Date                : 2022-03-14
     * @Author            : 강알찬
     * @param                : memNo   BIGINT 			-- 회차 번호
     * ,pageNo INT UNSIGNED		-- 페이지 번호
     * ,pagePerCnt INT UNSIGNED	-- 페이지 당 노출 건수 (Limit)
     * @return              :
    #1
    cnt		BIGINT		-- 전체 수


    #2

    mem_no		BIGINT		-- 회원 번호
    mem_nick	VARCHAR	-- 회원 닉네임
    mem_sex		CHAR		-- 회원성별
    image_profile	VARCHAR	-- 프로필
    room_no		BIGINT		-- 방송방 번호
    listen_room_no	BIGINT		-- 청취방방 번호
    last_login_date	DATETIME	-- 접속일자
     **********************************************************************************************
     */
    // @Transactional(readOnly = true)
    @ResultMap({"ResultMap.integer", "ResultMap.P_MainStarVo"})
    @Select("CALL rd_data.p_main_my_stat_list(#{memNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> getMyStar(Map map);

    /**********************************************************************************************
     * @프로시저 설명    : 메인 관리자 배너
     * @Date                : 2022-03-23
     * @Author            : 강알찬
     * @param                : IN `m_memNo` BIGINT UNSIGNED,
     * IN `m_device` TINYINT,
     * IN `m_platform` CHAR(3),
     * IN `m_position` INT
     * @return              :
     **********************************************************************************************
     */
    @Transactional(readOnly = true)
    @Select("CALL rd_data.p_main_banner_select(#{memNo}, #{device}, #{platform}, #{position})")
    List<MainSwiperVO> getAdminBanner(Map map);

    /**********************************************************************************************
     * @프로시저 설명    : 파트너dj 리스트[메인]
     * @Date                : 2022-03-23
     * @Author            : 강알찬
     * @param                : memNo 		BIGINT		-- 회원번호
     * @return              :
     * mem_no		BIGINT		-- 회원 번호
     * room_no		BIGINT		-- 방송방 번호
     * mem_nick	VARCHAR	-- 회원 닉네임
     * mem_sex		CHAR		-- 회원성별
     * image_profile	VARCHAR	-- 프로필
     * title		VARCHAR	-- 방송방 제목
     * subject_type	VARCHAR	-- 방송주제
     * image_background	VARCHAR	-- 배경이미지
     * is_wowza		TINYINT		-- 와우자여부
     **********************************************************************************************
     */
    @Select("CALL rd_data.p_main_partner_dj_live_list(#{memNo})")
    List<MainSwiperVO> getMainPartnerList(Map map);

    /**********************************************************************************************
     * @프로시저 설명    : 스타dj 리스트[메인]
     * @Date                : 2022-03-23
     * @Author            : 강알찬
     * @param                : memNo 		BIGINT		-- 회원번호
     * @return              :
     * mem_no		BIGINT		-- 회원 번호
     * room_no		BIGINT		-- 방송방 번호
     * mem_nick	VARCHAR	-- 회원 닉네임
     * mem_sex		CHAR		-- 회원성별
     * image_profile	VARCHAR	-- 프로필
     * title		VARCHAR	-- 방송방 제목
     * subject_type	VARCHAR	-- 방송주제
     * image_background	VARCHAR	-- 배경이미지
     * is_wowza		TINYINT		-- 와우자여부
     **********************************************************************************************
     */
    @Transactional(readOnly = true)
    @Select("CALL rd_data.p_main_star_dj_live_list(#{memNo})")
    List<MainSwiperVO> getMainStarList(Map map);

    /**********************************************************************************************
     * @프로시저 설명    : 일간dj 리스트[메인]
     * @Date                : 2022-03-23
     * @Author            : 강알찬
     * @param                : memNo 		BIGINT		-- 회원번호
     * @return              :
     * mem_no		BIGINT		-- 회원 번호
     * room_no		BIGINT		-- 방송방 번호
     * mem_nick	VARCHAR	-- 회원 닉네임
     * mem_sex		CHAR		-- 회원성별
     * image_profile	VARCHAR	-- 프로필
     * title		VARCHAR	-- 방송방 제목
     * image_background	VARCHAR	-- 배경이미지
     **********************************************************************************************
     */
    @Transactional(readOnly = true)
    @Select("CALL rd_data.p_main_rank_dj_list(#{memNo})")
    List<MainSwiperVO> getDayRankDjList(Map map);

    /**********************************************************************************************
     * @프로시저 설명    : 동시청취자 높은방 리스트[메인]
     * @Date                : 2022-03-23
     * @Author            : 강알찬
     * @param                : memNo 		BIGINT		-- 회원번호
     * @return              :
     * mem_no		BIGINT		-- 회원 번호
     * room_no		BIGINT		-- 방송방 번호
     * mem_nick	VARCHAR	-- 회원 닉네임
     * mem_sex		CHAR		-- 회원성별
     * image_profile	VARCHAR	-- 프로필
     * title		VARCHAR	-- 방송방 제목
     * subject_type	VARCHAR	-- 방송주제
     * image_background	VARCHAR	-- 배경이미지
     * is_wowza		TINYINT		-- 와우자여부
     **********************************************************************************************
     */
    @Transactional(readOnly = true)
    @Select("CALL rd_data.p_main_top_view_room_list(#{memNo})")
    List<MainSwiperVO> getTopViewList(Map map);

    /**********************************************************************************************
     * @프로시저 설명    : 동시청취자 높은방 리스트[메인]
     * @Date                : 2022-03-23
     * @Author            : 강알찬
     * @param                : memNo 		BIGINT		-- 회원번호
     * @return              :
     * mem_no		BIGINT		-- 회원 번호
     * room_no		BIGINT		-- 방송방 번호
     * mem_nick	VARCHAR	-- 회원 닉네임
     * mem_sex		CHAR		-- 회원성별
     * image_profile	VARCHAR	-- 프로필
     * title		VARCHAR	-- 방송방 제목
     * subject_type	VARCHAR	-- 방송주제
     * image_background	VARCHAR	-- 배경이미지
     * is_wowza		TINYINT		-- 와우자여부
     **********************************************************************************************
     */
    @Transactional(readOnly = true)
    @Select("CALL rd_data.p_main_top_like_room_list(#{memNo})")
    List<MainSwiperVO> getTopLikeList(Map map);

    /**********************************************************************************************
     * @프로시저 설명    : 메인  실시간 라이브 순위
     * @Date                : 2022-03-23
     * @Author            : 강알찬
     * @param                : memNo 		BIGINT		-- 회원번호
     * @return              :
     **********************************************************************************************
     */
    @Transactional(readOnly = true)
    @Select("CALL rd_data.p_main_top_live_list(#{memNo})")
    List<MainSwiperVO> getTopLiveList(Map map);

}
