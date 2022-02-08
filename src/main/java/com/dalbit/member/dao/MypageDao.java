package com.dalbit.member.dao;

import com.dalbit.admin.vo.MemberInfoVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.vo.*;
import com.dalbit.member.vo.procedure.*;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface MypageDao {
    ProcedureVo callProfileEdit(ProcedureVo procedureVo);
    ProcedureVo callFanstarInsert(ProcedureVo procedureVo);
    ProcedureVo callFanstarDelete(ProcedureVo procedureVo);

    ProcedureVo callMemberInfo(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    ProcedureVo callBroadBasic(ProcedureVo procedureVo);

    ProcedureVo callBroadBasicEdit(ProcedureVo procedureVo);
    ProcedureVo callMemberReportAdd(ProcedureVo procedureVo);
    ProcedureVo callMemberBlockAdd(ProcedureVo procedureVo);
    ProcedureVo callMemberBlockDel(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    ProcedureVo callMemberNotify(ProcedureVo procedureVo);

    ProcedureVo callMemberNotifyEdit(ProcedureVo procedureVo);

    List<P_MemberShortCutVo> callMemberShortCut(ProcedureVo procedureVo);

    ProcedureVo callMemberShortCutEdit(ProcedureVo procedureVo);
    ProcedureVo callMemberGiftRuby(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_NotificationVo> callMemberNotification(ProcedureVo procedureVo);

    ProcedureVo callMypageNoticeAdd(ProcedureVo procedureVo);
    ProcedureVo callMypageNoticeEdit(ProcedureVo procedureVo);
    ProcedureVo callMypageNoticeDel(ProcedureVo procedureVo);
    ProcedureVo callMypageNoticeRead(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_MypageNoticeSelectVo> callMypageNoticeSelect(ProcedureVo procedureVo);

    ProcedureVo callMyPageNoticeReplyAdd(ProcedureVo procedureVo);

    ProcedureVo callMyPageNoticeReplyDelete(ProcedureVo procedureVo);

    ProcedureVo callMyPageNoticeReplyEdit(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_MypageNoticeReplyListVo> callMyPageNoticeReplySelect(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_MypageReportBroadVo> callMypageMypageReportBroad(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_MypageReportListenVo> callMypageMypageReportListen(ProcedureVo procedureVo);

    ProcedureVo callMyapgeGetBanWord(ProcedureVo procedureVo);

    ProcedureVo callMypageInsertBanWord(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_SearchUserVo> callMypageSearchUser(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_MypageManagerVo> callMypageManager(ProcedureVo procedureVo);

    ProcedureVo callMypageManagerAdd(ProcedureVo procedureVo);
    ProcedureVo callMypageManagerEdit(ProcedureVo procedureVo);
    ProcedureVo callMypageManagerDel(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_MypageBlackVo> callMypageBlackListView(ProcedureVo procedureVo);

    ProcedureVo callMypageBlackListAdd(ProcedureVo procedureVo);
    ProcedureVo callMypageBlackListDel(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_ChangeItemListVo> changeItemSelect(ProcedureVo procedureVo);

    ProcedureVo changeItem(ProcedureVo procedureVo);

    ProcedureVo callAutoChangeSettingEdit(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    ProcedureVo callAutoChangeSettingSelect(ProcedureVo procedureVo);

    ProcedureVo callReadNotification(ProcedureVo procedureVo);

    void callReadALLNotification(P_NotificationVo pNotificationVo);

    @Transactional(readOnly = true)
    long selectMyByeolCnt(String mem_no);

    @Transactional(readOnly = true)
    HashMap selectExistsSpecialReq(String mem_no);

    @Transactional(readOnly = true)
    HashMap selectExistsPhoneSpecialReq(String mem_no);

    void insertSpecialReq(P_SpecialDjReq pSpecialDjReq);

    @Transactional(readOnly = true)
    long selectSpecialDjBroadcastTime(SpecialDjConditionSearchVo specialDjConditionSearchVo);

    @Transactional(readOnly = true)
    int selectSpecialDjLikeCnt(SpecialDjConditionSearchVo specialDjConditionSearchVo);

    @Transactional(readOnly = true)
    int selectSpecialDjBroadcastCnt(SpecialDjConditionSearchVo specialDjConditionSearchVo);

    @Transactional(readOnly = true)
    SpecialDjRegManageVo selectSpecialDjReqManage(SpecialDjRegManageVo specialDjRegManageVo);

    @Transactional(readOnly = true)
    List<SpecialDjContentVo> selectSpecialDjReqContent(SpecialDjRegManageVo specialDjRegManageVo);

    @Transactional(readOnly = true)
    MemberInfoVo selectMemberLevel(String mem_no);

    @Transactional(readOnly = true)
    int selectMemberFanCnt(String mem_no);

    @Transactional(readOnly = true)
    int selectListenerCnt(SpecialDjConditionSearchVo specialDjConditionSearchVo);

    int selectStarCnt(SpecialDjConditionSearchVo specialDjConditionSearchVo);

    int selectBroadDateCnt(SpecialDjConditionSearchVo specialDjConditionSearchVo);

    int selectNewFanCnt(SpecialDjConditionSearchVo specialDjConditionSearchVo);

    @Transactional(readOnly = true)
    List<LevelVo> selectLevel();

    @Transactional(readOnly = true)
    int callNewAlarm(P_MemberNotifyVo pMemberNotifyVo);

    ProcedureVo memberShortCutAdd(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    ProcedureVo memberShortCutExtend(ProcedureVo procedureVo);

    ProcedureVo msgClickUpdate(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    int selectAdminBadge(String value);

    @Transactional(readOnly = true)
    List<P_EmoticonListVo> callMemberEmoticon(ProcedureVo procedureVo);

    ProcedureVo callMemberNotificationDelete(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<EmoticonCategoryListVo> selectEmoticonCategory();

    @Transactional(readOnly = true)
    HashMap selectMyPageNew(HashMap params);

    @Transactional(readOnly = true)
    Long selectMyPageFanBoard(String mem_no);

    @Transactional(readOnly = true)
    HashMap selectMyPageWallet(String mem_no);

    ProcedureVo callBroadcastTitleAdd(ProcedureVo procedureVo);

    ProcedureVo callBroadcastTitleEdit(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_BroadcastOptionListVo> callBroadcastTitleSelect(ProcedureVo procedureVo);

    ProcedureVo callBroadcastTitleDelete(ProcedureVo procedureVo);

    ProcedureVo callBroadcastWelcomeMsgAdd(ProcedureVo procedureVo);

    ProcedureVo callBroadcastWelcomeMsgEdit(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_BroadcastOptionListVo> callBroadcastWelcomeMsgSelect(ProcedureVo procedureVo);

    ProcedureVo callBroadcastWelcomeMsgDelete(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_GoodListVo> callMemberGoodList(ProcedureVo procedureVo);

    ProcedureVo callBroadcastSettingSelect(ProcedureVo procedureVo);

    ProcedureVo callBroadcastSettingEdit(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    HashMap callMemberBoardCount(HashMap params);

    @Transactional(readOnly = true)
    ProcedureVo callWalletPopupListView(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_StoryVo> callMemberBoardStory(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_StorySendVo> callMemberBoardStorySend(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    List<P_WalletListVo> callWalletList(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    Integer selectIsSpecial(String mem_no);

    ProcedureVo callExchangeCancel(ProcedureVo procedureVo);

    /**
     * 피드 리스트 조회
     *
     * @Param
     * memNo 		BIGINT		-- 회원번호 (프로필 주인)
     * viewMemNo 		BIGINT		-- 회원번호 (프로필 보고있는 유저)
     * ,pageNo 		INT 		-- 페이지 번호
     * ,pageCnt 	INT		-- 페이지 당 노출 건수 (Limit)
     *
     * @Return
     * Multi Rows
     * #1
     * cnt		INT		--총 수
     * #2
     * noticeIdx		BIGINT		-- 번호
     * mem_no		BIGINT		-- 회원번호
     * nickName	VARCHAR	--닉네임
     * memSex		VARCHAR	-- 성별
     * image_profile	VARCHAR	-- 프로필
     * title		VARCHAR	-- 제목
     * contents		VARCHAR	-- 내용
     * imagePath	VARCHAR	-- 대표사진
     * topFix		BIGINT		-- 고정여부[0:미고정 ,1:고정]
     * writeDate		DATETIME	-- 수정일자
     * readCnt		BIGINT		-- 읽은수
     * replyCnt		BIGINT		-- 댓글수
     * rcv_like_cnt	BIGINT		-- 좋아요수
     * rcv_like_cancel_cnt BIGINT		-- 취소 좋아요수
     */
    @ResultMap({"ResultMap.integer", "ResultMap.ProfileFeedOutVO"})
    @Select("call rd_data.p_member_feed_list(#{memNo}, #{viewMemNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> pMemberFeedList(Map<String, Object> param);

    /**
     * 피드 상세 조회
     *
     * @Param
     * feedNo 		INT		-- 피드번호
     * ,memNo 		BIGINT		-- 회원번호 (프로필 주인)
     * viewMemNo 		BIGINT		-- 회원번호 (프로필 보고있는 유저)
     *
     * @Return
     * noticeIdx        BIGINT		-- 번호
     * mem_no		BIGINT		-- 회원번호
     * nickName	VARCHAR	--닉네임
     * memSex		VARCHAR	-- 성별
     * image_profile	VARCHAR	-- 프로필
     * title		VARCHAR	-- 제목
     * contents		VARCHAR	-- 내용
     * imagePath	VARCHAR	-- 대표사진
     * topFix		BIGINT		-- 고정여부[0:미고정 ,1:고정]
     * writeDate		DATETIME	-- 수정일자
     * readCnt		BIGINT		-- 읽은수
     * replyCnt		BIGINT		-- 댓글수
     * rcv_like_cnt	BIGINT		-- 좋아요수
     * rcv_like_cancel_cnt BIGINT		-- 취소 좋아요수
     */
    @Select("CALL rd_data.p_member_feed_sel(#{feedNo}, #{memNo}, #{viewMemNo})")
    ProfileFeedOutVo pMemberFeedSel(Map<String, Object> param);

    /**
     * 피드 등록
     * @Param
     * memNo        BIGINT			-- 회원번호
     * ,feedTitle 	VARCHAR(20)		-- 피드 등록글 제목
     * ,feedContents 	VARCHAR(1024)		-- 피드 등록글 내용
     * ,feedTopFix 	INT			-- 피드 상단고정[0:기본,1:고정]
     * @Return
     * s_return		INT		-- # -1:상단 고정  개수 초과, 0:에러, 1: 정상
     * */
    @Select("CALL rd_data.p_member_feed_ins(#{memNo}, #{feedTitle}, #{feedContents}, #{feedTopFix})")
    int pMemberFeedIns(Map<String, Object> param);

    /**
     * 피드 수정
     * @Param
     * feedNo 		INT			-- 피드번호
     * ,memNo 		BIGINT			-- 회원번호
     * ,feedTitle 	VARCHAR(20)		-- 피드 등록글 제목
     * ,feedContents	VARCHAR(1024)		-- 피드 등록글 내용
     * @Return
     * s_return		INT		-- # -1:상단 고정  개수 초과, 0:에러, 1: 정상
     * */
    @Select("CALL rd_data.p_member_feed_upd(#{feedNo}, #{memNo}, #{feedTitle}, #{feedContents}, #{feedTopFix})")
    int pMemberFeedUpd(Map<String, Object> param);

    /**
     * 피드 삭제
     * @Param
     * feedNo        INT		-- 피드번호
     * ,delChrgrName 	VARCHAR(40)	-- 삭제 관리자명
     * @Return
     * s_return		INT		-- #  0:에러, 1: 정상
     * */
    @Select("CALL rd_data.p_member_feed_del(#{noticeIdx}, #{delChrgrName})")
    int pMemberFeedDel(ProfileFeedDelVo param);

    /**
     * 피드 사진 리스트 조회
     *
     * @Param
     * regNo 		TEXT			-- 피드 등록글 번호
     *
     * @Return
     * #1
     * photo_no		BIGINT		-- 사진번호
     * feed_reg_no	BIGINT		-- 피드번호
     * mem_no		BIGINT		-- 회원번호
     * img_name	BIGINT		-- 이미지 이름
     * ins_date		DATETIME	--  등록일
     * */
    @Select("CALL rd_data.p_member_feed_photo_list(#{regNo})")
    List<ProfileFeedPhotoOutVo> pMemberFeedPhotoList(String param);

    /**
     * 피드 사진 리스트 등록
     *
     * @Param
     * regNo        INT			-- 피드 등록글 번호
     * ,memNo 		BIGINT			-- 회원번호
     * ,imgName 	VARCHAR(100)		-- 등록사진명
     *
     * @Return
     * s_return        INT		-- -2: 등록글 없음, -1: 등록사진 개수 초과, 0:에러, 1: 정상
     */
    @Select("CALL rd_data.p_member_feed_photo_ins(#{regNo}, #{memNo}, #{imgName})")
    int pMemberFeedPhotoIns(Map<String, Object> param);

    /**
     * 피드 사진 삭제
     *
     * @Param
     * photoNo 	INT		-- 사진고유번호
     * ,regNo 		INT		-- 피드 등록글 번호
     * ,imgName 	VARCHAR(100)	-- 등록사진명
     * ,delChrgrName 	VARCHAR(40)	-- 삭제 관리자명
     *
     * @Return
     * s_return		INT		-- #  -1: 삭제할 파일 없음, 0:에러, 1: 정상
     */
    @Select("CALL rd_data.p_member_feed_photo_del(#{photoNo}, #{regNo}, #{imageName}, #{delChrgrName})")
    int pMemberFeedPhotoDel(Map<String, Object> param);

    /**
     * 피드 좋아요 등록 (미적용)
     *
     * @Param
     * regNo        INT		-- 피드 등록글 번호
     * ,mMemNo 	BIGINT		-- 피드 회원번호
     * ,vMemNo 	BIGINT		-- 방문자 회원번호
     *
     * @Return
     * s_return        INT		-- #   -1:이미좋아요함, 0:에러 , 1:최초좋아요, 2: 좋아요취소후 다시 좋아요
     * */
    @Select("CALL rd_data.p_member_feed_like_log_ins(#{regNo}, #{mMemNo}, #{vMemNo})")
    int pMemberFeedLikeLogIns(Map<String, Object> param);

    /**
     * 피드 좋아요 취소 (미적용)
     *
     * @Param
     * regNo        INT			-- 피드 등록글 번호
     * ,mMemNo 	BIGINT			-- 피드 회원번호
     * ,vMemNo 	BIGINT			-- 방문자 회원번호
     *
     * @Return
     * s_return		INT		-- # -1:좋아요하지않은 등록글, 0:에러 , 1:취소완료
     * */
    @Select("CALL p_member_feed_like_cancel_ins(#{regNo}, #{mMemNo}, #{vMemNo})")
    int pMemberFeedLikeCancelIns(Map<String, Object> param);
}
