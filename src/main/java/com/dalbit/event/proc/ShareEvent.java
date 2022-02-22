package com.dalbit.event.proc;

import com.dalbit.event.vo.ShareEventInputVo;
import com.dalbit.event.vo.ShareEventVo;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
public interface ShareEvent {
    /**********************************************************************************************
    * @Method 설명 : # 공유 이벤트 댓글 삭제
    * @작성일   : 2022-02-22
    * @작성자   : 박성민
    * @Param  :
    *   tailNo BIGINT UNSIGNED					-- 댓글번호
     * 	,tailMemNo BIGINT UNSIGNED				-- 댓글 등록 회원번호
    * @return : s_return: #0: 에러, 1: 정상
    **********************************************************************************************/
    @Select("CALL rd_data.p_tb_event_rbd_share_tail_del(#{tailNo}, #{tailMemNo})")
    Integer shareTailDel(ShareEventInputVo shareEventInputVo);

    /**********************************************************************************************
     * @Method 설명 : # 공유 이벤트 댓글 등록
     * @작성일   : 2022-02-22
     * @작성자   : 박성민
     * @Param  :
     *   tailMemNo BIGINT UNSIGNED				-- 댓글 등록 회원번호
     * 	,tailMemId VARCHAR(50)			-- 댓글 등록 회원아이디
     * 	,tailMemSex CHAR(1)			-- 댓글 등록 회원성별
     * 	,tailMemIp VARCHAR(40)			-- 댓글 등록 회원아이피
     * 	,tailConts VARCHAR(300)			-- 댓글 등록 내용
     * 	,tailLoginMedia CHAR(1)			-- 댓글 등록 미디어
     * @return : s_return: #0: 에러, 1: 정상
     **********************************************************************************************/
    @Select("CALL rd_data.p_tb_event_rbd_share_tail_ins(#{tailMemNo}, #{tailMemId}, #{tailMemSex}, #{tailMemIp}, #{tailConts}, #{tailLoginMedia})")
    Integer shareTailIns(ShareEventInputVo shareEventInputVo);

    /**********************************************************************************************
     * @Method 설명 : # 공유 이벤트 댓글 변경
     * @작성일   : 2022-02-22
     * @작성자   : 박성민
     * @Param  :
     *   tailNo BIGINT UNSIGNED				-- 댓글번호
     * 	,tailMemNo BIGINT UNSIGNED				-- 댓글 등록 회원번호
     * 	,tailMemId VARCHAR(50)				-- 댓글 등록 회원아이디
     * 	,tailMemSex CHAR(1)				-- 댓글 등록 회원성별
     * 	,tailMemIp VARCHAR(40)				-- 댓글 등록 회원아이피
     * 	,tailConts VARCHAR(300)				-- 댓글 등록 내용
     * 	,tailLoginMedia CHAR(1)				-- 댓글 등록 미디어
     * @return : s_return: #0: 에러, 1: 정상
     **********************************************************************************************/
    @Select("CALL rd_data.p_tb_event_rbd_share_tail_upd(#{tailNo}, #{tailMemNo}, #{tailMemId}, #{tailMemSex}, #{tailMemIp}, #{tailConts}, #{tailLoginMedia})")
    Integer shareTailUpd(ShareEventInputVo shareEventInputVo);

    /**********************************************************************************************
     * @Method 설명 : # 공유 이벤트 댓글 목록
     * @작성일   : 2022-02-22
     * @작성자   : 박성민
     * @Param  :
     *   memNo BIGINT UNSIGNED		-- 회원번호
     * 	, pageNo INT UNSIGNED		-- 페이지 번호
     * 	, pagePerCnt INT UNSIGNED	-- 페이지 당 노출 건수 (Limit)
     * @return :
     *  [0]
     * 	cnt			INT			# 등록수
     *
     *  [1]
     * 	tail_no			BIGINT				-- 댓글번호
     * 	tail_mem_no		BIGINT				-- 회원번호
     * 	tail_mem_id		VARCHAR(50)			-- 회원아이디
     * 	tail_mem_sex		CHAR(1)				-- 회원성별
     * 	tail_mem_ip		VARCHAR(40)			-- 회원아이피
     * 	tail_conts		VARCHAR(300)			-- 댓글내용
     * 	login_media		CHAR(1)				-- 미디어
     * 	ins_date		DATETIME			-- 등록일자
     * 	mem_nick		VARCHAR(20)			-- 닉네임
     * 	mem_id			VARCHAR(50)			-- 아이디
     * 	mem_userid		VARCHAR(20)			-- 추가아이디
     * 	image_profile		VARCHAR(256)			-- 프로필이미지정보
     **********************************************************************************************/
    @ResultMap({"ResultMap.integer", "ResultMap.ShareEventVo"})
    @Select("CALL rd_data.p_tb_event_rbd_share_tail_list(#{memNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> shareTailList(ShareEventVo shareEventVo);
}
