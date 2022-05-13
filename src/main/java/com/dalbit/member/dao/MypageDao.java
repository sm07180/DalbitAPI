package com.dalbit.member.dao;

import com.dalbit.admin.vo.MemberInfoVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.vo.*;
import com.dalbit.member.vo.procedure.*;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface MypageDao {
    ProcedureVo callProfileEdit(ProcedureVo procedureVo);

    ProcedureVo callFanstarInsert(ProcedureVo procedureVo);

    ProcedureVo callFanstarDelete(ProcedureVo procedureVo);

    ProcedureVo callMemberInfo(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    ProcedureVo callBroadBasic(ProcedureVo procedureVo);

    ProcedureVo callBroadBasicEdit(ProcedureVo procedureVo);

    ProcedureVo callMemberReportAdd(ProcedureVo procedureVo);

    ProcedureVo callMemberBlockAdd(ProcedureVo procedureVo);

    ProcedureVo callMemberBlockDel(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    ProcedureVo callMemberNotify(ProcedureVo procedureVo);

    ProcedureVo callMemberNotifyEdit(ProcedureVo procedureVo);

    List<P_MemberShortCutVo> callMemberShortCut(ProcedureVo procedureVo);

    ProcedureVo callMemberShortCutEdit(ProcedureVo procedureVo);

    ProcedureVo callMemberGiftRuby(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_NotificationVo> callMemberNotification(ProcedureVo procedureVo);

    ProcedureVo callMypageNoticeAdd(ProcedureVo procedureVo);

    ProcedureVo callMypageNoticeEdit(ProcedureVo procedureVo);

    ProcedureVo callMypageNoticeDel(ProcedureVo procedureVo);

    ProcedureVo callMypageNoticeRead(ProcedureVo procedureVo);

    /**
     * 모바일용 방송방공지 정보
     *
     * @param memNo BIGINT  -- 회원번호
     * @Return auto_no          INT         -- 자동증가 번호
     * mem_no           BIGINT      -- 회원 번호
     * conts            VARCHAR     -- 회원 아이디
     * ins_date         DATETIME    -- 등록 일자
     * upd_date         DATETIME    -- 수정 일자
     */
    @ResultMap({"ResultMap.MobileBroadcastNoticeListOutVo"})
    @Select("CALL rd_data.p_broadcast_room_notice_sel(#{memNo})")
    List<Object> pMobileBroadcastNoticeListSel(Map<String, Object> param);

    // @Transactional(readOnly = true)
    List<P_MypageNoticeSelectVo> callMypageNoticeSelect(ProcedureVo procedureVo);

    ProcedureVo callMyPageNoticeReplyAdd(ProcedureVo procedureVo);

    ProcedureVo callMyPageNoticeReplyDelete(ProcedureVo procedureVo);

    ProcedureVo callMyPageNoticeReplyEdit(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_MypageNoticeReplyListVo> callMyPageNoticeReplySelect(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_MypageReportBroadVo> callMypageMypageReportBroad(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_MypageReportListenVo> callMypageMypageReportListen(ProcedureVo procedureVo);

    ProcedureVo callMyapgeGetBanWord(ProcedureVo procedureVo);

    ProcedureVo callMypageInsertBanWord(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_SearchUserVo> callMypageSearchUser(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_MypageManagerVo> callMypageManager(ProcedureVo procedureVo);

    ProcedureVo callMypageManagerAdd(ProcedureVo procedureVo);

    ProcedureVo callMypageManagerEdit(ProcedureVo procedureVo);

    ProcedureVo callMypageManagerDel(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_MypageBlackVo> callMypageBlackListView(ProcedureVo procedureVo);

    ProcedureVo callMypageBlackListAdd(ProcedureVo procedureVo);

    ProcedureVo callMypageBlackListDel(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_ChangeItemListVo> changeItemSelect(ProcedureVo procedureVo);

    ProcedureVo changeItem(ProcedureVo procedureVo);

    ProcedureVo callAutoChangeSettingEdit(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    ProcedureVo callAutoChangeSettingSelect(ProcedureVo procedureVo);

    ProcedureVo callReadNotification(ProcedureVo procedureVo);

    void callReadALLNotification(P_NotificationVo pNotificationVo);

    // @Transactional(readOnly = true)
    long selectMyByeolCnt(String mem_no);

    // @Transactional(readOnly = true)
    HashMap selectExistsSpecialReq(String mem_no);

    // @Transactional(readOnly = true)
    HashMap selectExistsPhoneSpecialReq(String mem_no);

    void insertSpecialReq(P_SpecialDjReq pSpecialDjReq);

    // @Transactional(readOnly = true)
    long selectSpecialDjBroadcastTime(SpecialDjConditionSearchVo specialDjConditionSearchVo);

    // @Transactional(readOnly = true)
    int selectSpecialDjLikeCnt(SpecialDjConditionSearchVo specialDjConditionSearchVo);

    // @Transactional(readOnly = true)
    int selectSpecialDjBroadcastCnt(SpecialDjConditionSearchVo specialDjConditionSearchVo);

    // @Transactional(readOnly = true)
    SpecialDjRegManageVo selectSpecialDjReqManage(SpecialDjRegManageVo specialDjRegManageVo);

    // @Transactional(readOnly = true)
    List<SpecialDjContentVo> selectSpecialDjReqContent(SpecialDjRegManageVo specialDjRegManageVo);

    // @Transactional(readOnly = true)
    MemberInfoVo selectMemberLevel(String mem_no);

    // @Transactional(readOnly = true)
    int selectMemberFanCnt(String mem_no);

    // @Transactional(readOnly = true)
    int selectListenerCnt(SpecialDjConditionSearchVo specialDjConditionSearchVo);

    int selectStarCnt(SpecialDjConditionSearchVo specialDjConditionSearchVo);

    int selectBroadDateCnt(SpecialDjConditionSearchVo specialDjConditionSearchVo);

    int selectNewFanCnt(SpecialDjConditionSearchVo specialDjConditionSearchVo);

    // @Transactional(readOnly = true)
    List<LevelVo> selectLevel();

    // @Transactional(readOnly = true)
    int callNewAlarm(P_MemberNotifyVo pMemberNotifyVo);

    ProcedureVo memberShortCutAdd(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    ProcedureVo memberShortCutExtend(ProcedureVo procedureVo);

    ProcedureVo msgClickUpdate(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    int selectAdminBadge(String value);

    // @Transactional(readOnly = true)
    List<P_EmoticonListVo> callMemberEmoticon(ProcedureVo procedureVo);

    ProcedureVo callMemberNotificationDelete(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<EmoticonCategoryListVo> selectEmoticonCategory();

    // @Transactional(readOnly = true)
    HashMap selectMyPageNew(HashMap params);

    // @Transactional(readOnly = true)
    Long selectMyPageFanBoard(String mem_no);

    // @Transactional(readOnly = true)
    HashMap selectMyPageWallet(String mem_no);

    ProcedureVo callBroadcastTitleAdd(ProcedureVo procedureVo);

    ProcedureVo callBroadcastTitleEdit(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_BroadcastOptionListVo> callBroadcastTitleSelect(ProcedureVo procedureVo);

    ProcedureVo callBroadcastTitleDelete(ProcedureVo procedureVo);

    ProcedureVo callBroadcastWelcomeMsgAdd(ProcedureVo procedureVo);

    ProcedureVo callBroadcastWelcomeMsgEdit(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_BroadcastOptionListVo> callBroadcastWelcomeMsgSelect(ProcedureVo procedureVo);

    ProcedureVo callBroadcastWelcomeMsgDelete(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_GoodListVo> callMemberGoodList(ProcedureVo procedureVo);

    ProcedureVo callBroadcastSettingSelect(ProcedureVo procedureVo);

    ProcedureVo callBroadcastSettingEdit(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    HashMap callMemberBoardCount(HashMap params);

    // @Transactional(readOnly = true)
    ProcedureVo callWalletPopupListView(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_StoryVo> callMemberBoardStory(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_StorySendVo> callMemberBoardStorySend(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_WalletListVo> callWalletList(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    Integer selectIsSpecial(String mem_no);

    ProcedureVo callExchangeCancel(ProcedureVo procedureVo);

    /**
     * 방송공지 리스트 조회
     *
     * @Param memNo        BIGINT		-- 회원번호 (프로필 주인)
     * viewMemNo 		BIGINT		-- 회원번호 (프로필 보고있는 유저)
     * ,pageNo 		INT 		-- 페이지 번호
     * ,pageCnt 	INT		-- 페이지 당 노출 건수 (Limit)
     * @Return Multi Rows
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
    // @Transactional(readOnly = false)
    @ResultMap({"ResultMap.integer", "ResultMap.ProfileFeedOutVO"})
    @Select("call p_member_broadcast_notice_list(#{memNo}, #{viewMemNo}, #{pageNo}, #{pageCnt})")
    List<Object> pMemberFeedList(Map<String, Object> param);

    /**
     * 방송공지 리스트(고정) 조회
     *
     * @Param memNo        BIGINT      -- 회원번호
     * viewMemNo    BIGINT      -- 회원번호(접속자)
     * ,pageNo      INT         -- 페이지 번호
     * ,pageCnt     INT         -- 페이지 당 노출 건수 (Limit)
     * @Return Multi Rows
     * #1
     * cnt          INT         -- 총 수
     * <p>
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
    // @Transactional(readOnly = false)
    @ResultMap({"ResultMap.integer", "ResultMap.ProfileFeedOutVO"})
    @Select("CALL p_member_broadcast_notice_fix_list(#{memNo}, ${viewMemNo}, #{pageNo}, #{pageCnt})")
    List<Object> pMemberFeedFixList(Map<String, Object> param);

    /**
     * 방송공지 상세 조회
     *
     * @Param noticeNo        INT		-- 피드번호
     * ,memNo 		BIGINT		-- 회원번호 (프로필 주인)
     * viewMemNo 		BIGINT		-- 회원번호 (프로필 보고있는 유저)
     * @Return noticeIdx        BIGINT		-- 번호
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
    @Select("CALL rd_data.p_member_broadcast_notice_sel(#{noticeNo}, #{memNo}, #{viewMemNo})")
    ProfileFeedOutVo pMemberFeedSel(Map<String, Object> param);

    /**
     * 방송공지 등록
     *
     * @Param memNo        BIGINT			-- 회원번호
     * ,noticeTitle 	VARCHAR(20)		-- 피드 등록글 제목
     * ,noticeContents 	VARCHAR(1024)		-- 피드 등록글 내용
     * ,imgName         VARCHAR(100)        -- 등록사진명
     * ,noticeTopFix 	INT			-- 피드 상단고정[0:기본,1:고정]
     * @Return s_return        INT		-- # -1:상단 고정  개수 초과, 0:에러, 1: 정상
     */
    @Select("CALL rd_data.p_member_broadcast_notice_ins(#{memNo}, #{noticeTitle}, #{noticeContents}, #{imgName}, #{noticeTopFix})")
    Integer pMemberFeedIns(Map<String, Object> param);

    /**
     * 방송공지 수정
     *
     * @Param noticeNo        INT			-- 피드번호
     * ,memNo 		BIGINT			-- 회원번호
     * ,noticeTitle 	VARCHAR(20)		-- 피드 등록글 제목
     * ,noticeContents	VARCHAR(1024)		-- 피드 등록글 내용
     * ,imgName`        VARCHAR(100)        -- 등록사진명
     * ,noticeTopFix    INT         -- 공지 상단고정[0:기본,1:고정]
     * @Return s_return        INT		-- # -1:상단 고정  개수 초과, 0:에러, 1: 정상
     */
    @Select("CALL rd_data.p_member_broadcast_notice_upd(#{noticeNo}, #{memNo}, #{noticeTitle}, #{noticeContents}, #{imgName}, #{noticeTopFix})")
    Integer pMemberFeedUpd(Map<String, Object> param);

    /**
     * 방송공지 삭제
     *
     * @Param noticeNo        INT		-- 피드번호
     * ,delChrgrName 	VARCHAR(40)	-- 삭제 관리자명
     * @Return s_return        INT		-- #  0:에러, 1: 정상
     */
    @Select("CALL rd_data.p_member_broadcast_notice_del(#{noticeNo}, #{delChrgrName})")
    Integer pMemberFeedDel(ProfileFeedDelVo param);

    /**
     * 방송공지 사진 리스트 조회
     *
     * @Param regNo        TEXT			-- 피드 등록글 번호
     * @Return #1
     * photo_no		BIGINT		-- 사진번호
     * feed_reg_no	BIGINT		-- 피드번호
     * mem_no		BIGINT		-- 회원번호
     * img_name	BIGINT		-- 이미지 이름
     * ins_date		DATETIME	--  등록일
     */
    // @Transactional(readOnly = false)
    @Select("CALL rd_data.p_member_feed_photo_list(#{regNo})")
    List<ProfileFeedPhotoOutVo> pMemberFeedPhotoList(String param);

    /**
     * 방송공지 사진 리스트 등록
     *
     * @Param regNo        INT			-- 피드 등록글 번호
     * ,memNo 		BIGINT			-- 회원번호
     * ,imgName 	VARCHAR(100)		-- 등록사진명
     * @Return s_return        INT		-- -2: 등록글 없음, -1: 등록사진 개수 초과, 0:에러, 1: 정상
     */
    @Select("CALL rd_data.p_member_feed_photo_ins(#{regNo}, #{memNo}, #{imgName})")
    Integer pMemberFeedPhotoIns(Map<String, Object> param);

    /**
     * 방송공지 사진 삭제
     *
     * @Param photoNo    INT		-- 사진고유번호
     * ,regNo 		INT		-- 피드 등록글 번호
     * ,imgName 	VARCHAR(100)	-- 등록사진명
     * ,delChrgrName 	VARCHAR(40)	-- 삭제 관리자명
     * @Return s_return        INT		-- #  -1: 삭제할 파일 없음, 0:에러, 1: 정상
     */
    @Select("CALL rd_data.p_member_feed_photo_del(#{photoNo}, #{regNo}, #{imageName}, #{delChrgrName})")
    Integer pMemberFeedPhotoDel(Map<String, Object> param);

    /**
     * 방송공지 좋아요 등록
     *
     * @Param regNo        INT		-- 피드 등록글 번호
     * ,mMemNo 	BIGINT		-- 피드 회원번호
     * ,vMemNo 	BIGINT		-- 방문자 회원번호
     * @Return s_return        INT		-- #   -1:이미좋아요함, 0:에러 , 1:최초좋아요, 2: 좋아요취소후 다시 좋아요
     */
    @Select("CALL rd_data.p_member_broadcast_notice_like_log_ins(#{regNo}, #{mMemNo}, #{vMemNo})")
    Integer pMemberFeedLikeLogIns(Map<String, Object> param);

    /**
     * 방송공지 좋아요 취소
     *
     * @Param regNo        INT			-- 피드 등록글 번호
     * ,mMemNo 	BIGINT			-- 피드 회원번호
     * ,vMemNo 	BIGINT			-- 방문자 회원번호
     * @Return s_return        INT		-- # -1:좋아요하지않은 등록글, 0:에러 , 1:취소완료
     */
    @Select("CALL p_member_broadcast_notice_like_cancel_ins(#{regNo}, #{mMemNo}, #{vMemNo})")
    Integer pMemberFeedLikeCancelIns(Map<String, Object> param);

    /**
     * 피드 등록
     *
     * @Param memNo            BIGINT      -- 회원번호
     * feedContents     VARCHAR     -- 피드 등록글 내용
     * @Return s_return         INT         -- # 0: 에러, 1: 정상
     */
    @Select("CALL p_member_feed_ins_v1(#{memNo}, #{feedContents})")
    Integer pMyPageFeedIns(Map<String, Object> param);

    /**
     * 피드 수정
     *
     * @Param feedNo           INT         -- 피드번호
     * memNo            BIGINT      -- 회원번호
     * feedContents     VARCHAR     -- 피드 등록글 내용
     * @Return s_return         INT         -- # -1: 데이터 없음, 0: 에러, 1: 정상
     */
    @Select("CALL rd_data.p_member_feed_upd_v1(#{feedNo}, #{memNo}, #{feedContents})")
    Integer pMyPageFeedUpd(Map<String, Object> param);

    /**
     * 피드 삭제
     *
     * @Param feedNo           INT         -- 피드번호
     * delChrgrName     VARCHAR     -- 삭제 관리자명
     * @Return s_return         INT         -- # -1: 데이터 없음, 0: 에러, 1: 정상
     */
    @Select("CALL rd_data.p_member_feed_del_v1(#{feedNo}, #{delChrgrName})")
    Integer pMyPageFeedDel(MyPageFeedDelVo param);

    /**
     * 피드 리스트
     *
     * @Param memNo            BIGINT      -- 회원번호
     * viewMemNo        BIGINT      -- 회원번호(접속자)
     * pageNo           INT         -- 페이지 번호
     * pageCnt          INT         -- 페이지 당 노출 건수(Limit)
     * @Return Multi Rows
     * #1
     * cnt              INT         -- 총 수
     * <p>
     * #2
     * reg_no           BIGINT      -- 번호
     * mem_no           BIGINT      -- 회원번호
     * mem_nick         VARCHAR     -- 닉네임
     * mem_sex          VARCHAR     -- 성별
     * image_profile    VARCHAR     -- 프로필
     * feed_conts       VARCHAR     -- 내용
     * image_path       VARCHAR     -- 대표사진
     * tail_cnt         BIGINT      -- 댓글수
     * rcv_like_cnt     BIGINT      -- 좋아요수
     * rcv_like_cancel_cnt  BIGINT  -- 취소 좋아요 수
     * like_yn          CHAR        -- 좋아요 확인[y, n]
     * ins_date         DATETIME    -- 등록일자
     */
    // @Transactional(readOnly = false)
    @ResultMap({"ResultMap.integer", "ResultMap.MyPageFeedOutVo"})
    @Select("CALL rd_data.p_member_feed_list_v1(#{memNo}, #{viewMemNo}, #{pageNo}, #{pageCnt})")
    List<Object> pMyPageFeedList(Map<String, Object> param);

    /**
     * 피드 상세
     *
     * @Param feedNo           INT         -- 피드번호
     * memNo            BIGINT      -- 회원번호
     * viewMemNo        BIGINT      -- 회원번호(접속자)
     * @Return reg_no           BIGINT      -- 번호
     * mem_no           BIGINT      -- 회원번호
     * mem_nick         VARCHAR     -- 닉네임
     * mem_sex          VARCHAR     -- 성별
     * image_profile    VARCHAR     -- 프로필
     * feed_conts       VARCHAR     -- 내용
     * image_path       VARCHAR     -- 대표사진
     * tail_cnt         BIGINT      -- 댓글수
     * rcv_like_cnt     BIGINT      -- 좋아요수
     * rcv_like_cancel_cnt  BIGINT  -- 취소 좋아요수
     * like_yn          CHAR        -- 좋아요 확인[y, n]
     * ins_date         DATETIME    -- 등록일자
     */
    @Select("CALL rd_data.p_member_feed_sel_v1(#{feedNo}, #{memNo}, #{viewMemNo})")
    MyPageFeedOutVo pMyPageFeedSel(Map<String, Object> param);

    /**
     * 피드 사진등록
     *
     * @Param feedNo           INT         -- 피드번호
     * memNo            BIGINT      -- 회원번호
     * imgName          VARCHAR     -- 등록사진명
     * @Return s_return         INT         -- # -2: 등록글 없음, -1: 등록사진 개수 초과, 0: 에러, 1: 정상
     */
    @Select("CALL rd_data.p_member_feed_photo_ins_v1(#{feedNo}, #{memNo}, #{imgName})")
    Integer pMyPageFeedPictureIns(Map<String, Object> param);

    /**
     * 피드 사진삭제
     *
     * @Param photoNo          INT         -- 사진고유번호
     * feedNo           INT         -- 피드 등록글 번호
     * imgName          VARCHAR     -- 등록사진명
     * delChrgrName     VARCHAR     -- 삭제 관리자명
     * @Return s_return         INT         -- # -1: 삭제할 파일 없음, 0: 에러, 1: 정상
     */
    @Select("CALL rd_data.p_member_feed_photo_del_v1(#{photoNo}, #{feedNo}, #{imgName}, #{delChrgrName})")
    Integer pMyPageFeedPictureDel(Map<String, Object> param);

    /**
     * 피드 사진리스트
     *
     * @Param feedNo           TEXT        -- 피드 등록글 번호
     * @Return photo_no         BIGINT      -- 사진번호
     * feed_reg_no      BIGINT      -- 피드번호
     * mem_no           BIGINT      -- 회원번호
     * img_name         BIGINT      -- 이미지 이름
     * ins_date         DATETIME    -- 등록일
     */
    // @Transactional(readOnly = false)
    @Select("CALL rd_data.p_member_feed_photo_list_v1(#{feedNo})")
    List<MyPageFeedPictureOutVo> pMyPageFeedPictureList(String param);

    /**
     * 피드 댓글 등록
     *
     * @Param regNo            INT         -- 피드 고유번호
     * memNo            BIGINT      -- 회원번호(bj)
     * tmemNo           BIGINT      -- 등록회원번호
     * tmemConts        VARCHAR     -- 등록내용
     * @Return s_return         INT         -- # 0: 에러, 1: 정상
     */
    @Select("CALL rd_data.p_member_feed_tail_ins(#{regNo}, #{memNo}, #{tmemNo}, #{tmemConts})")
    Integer pMyPageFeedReplyIns(Map<String, Object> param);

    /**
     * 피드 댓글 수정
     *
     * @Param tailNo           INT         -- 댓글번호
     * tmemConts        VARCHAR     -- 등록내용
     * @Return s_return         INT         -- # 0: 에러, 1: 정상상
     */
    @Select("CALL rd_data.p_member_feed_tail_upd(#{tailNo}, #{tmemConts})")
    Integer pMyPageFeedReplyUpd(Map<String, Object> param);

    /**
     * 피드 댓글 삭제
     *
     * @Param regNo            INT         -- 피드 고유번호
     * tailNo           INT         -- 댓글번호
     * chrgrName        VARCHAR     -- 삭제자명(아이디)
     * @Return s_return         INT         -- # 0: 에러, 1: 정상
     */
    @Select("CALL rd_data.p_member_feed_tail_del(#{regNo}, #{tailNo}, #{chrgrName})")
    Integer pMyPageFeedReplyDel(MyPageFeedReplyDelVo param);

    /**
     * 피드 댓글 리스트
     *
     * @Param feedNo           BIGINT      -- 피드글 번호
     * pageNo           INT         -- 페이지 번호
     * pageCnt          INT         -- 페이지 당 노출 건수(Limit)
     * @Return Multi Rows
     * <p>
     * #1
     * cnt              INT         -- 총 수
     * <p>
     * #2
     * tail_no          BIGINT      -- 번호
     * parent_no        BIGINT      -- 번호
     * mem_no           BIGINT      -- 회원번호
     * tail_mem_no      BIGINT      -- 회원번호
     * tail_conts       VARCHAR     -- 내용
     * tail_mem_nick    VARCHAR     -- 닉네임
     * tail_mem_sex     VARCHAR     -- 성별
     * tail_image_profile   VARCHAR -- 프로필
     * ins_date         DATETIME    -- 등록일자
     * upd_date         DATETIME    -- 수정일자
     */
    // @Transactional(readOnly = false)
    @ResultMap({"ResultMap.integer", "ResultMap.MyPageFeedReplyOutVo"})
    @Select("CALL rd_data.p_member_feed_tail_list(#{feedNo}, #{pageNo}, #{pageCnt})")
    List<Object> pMyPageFeedReplyList(Map<String, Object> param);

    /**
     * 피드 댓글 상세
     *
     * @Param regNo            BIGINT      -- 피드글 번호
     * tailNo           BIGINT      -- 댓글번호
     * @Return tail_no          BIGINT      -- 번호
     * parent_no        BIGINT      -- 번호
     * mem_no           BIGINT      -- 회원번호
     * tail_mem_no      BIGINT      -- 회원번호
     * tail_conts       VARCHAR     -- 내용
     * tail_mem_nick    VARCHAR     -- 닉네임
     * tail_mem_sex     VARCHAR     -- 성별
     * tail_image_profile   VARCHAR -- 프로필
     * ins_date         DATETIME    -- 등록일자
     * upd_date         DATETIME    -- 수정일자
     */
    @Select("CALL rd_data.p_member_feed_tail_sel(#{regNo}, #{tailNo})")
    MyPageFeedReplyOutVo pMyPageFeedReplySel(Map<String, Object> param);

    /**
     * 피드 좋아요
     *
     * @Param feedNo           INT         -- 피드 등록글 번호
     * mMemNo           BIGINT      -- 피드 회원번호
     * vMemNo           BIGINT      -- 방문자 회원번호
     * @Return s_return         INT         -- # -1: 이미 좋아요함, 0: 에러, 1: 최초 좋아요 2: 좋아요 취소 후 다시 좋아요
     */
    @Select("CALL rd_data.p_member_feed_like_log_ins_v1(#{feedNo}, #{mMemNo}, #{vMemNo})")
    Integer pMyPageFeedLike(Map<String, Object> param);

    /**
     * 피드 좋아요 취소
     *
     * @Param feedNo           INT         -- 피드 등록글 번호
     * mMemNo           BIGINT      -- 피드 회원번호
     * vMeMNo           BIGINT      -- 방문자 회원번호
     * @Return s_return         INT         -- # -1: 좋아요 하지 않은 등록글, 0: 에러, 1: 취소 완료
     */
    @Select("CALL rd_data.p_member_feed_like_cancel_ins_v1(#{feedNo}, #{mMemNo}, #{vMemNo})")
    Integer pMyPageFeedLikeCancel(Map<String, Object> param);

    /**
     * 방송방공지 등록
     *
     * @param memNo BIGINT  -- 회원 번호
     *              ,roomNo          BIGINT  -- 방 번호[0: 기본, 방 있을시에만]
     *              ,roomNoticeConts VARCHAR -- 방송공지 등록글 내용
     * @Return s_return         INT     -- #-1: 회원번호 없음 또는 공지글 등록있음, 0: 에러, 1: 정상
     */
    @Select("CALL rd_data.p_broadcast_room_notice_ins(#{memNo}, #{roomNo}, #{roomNoticeConts})")
    Integer pMemberBroadcastNoticeIns(Map<String, Object> param);

    /**
     * 방송방공지 수정
     *
     * @param roomNoticeNo BIGINT  -- 방송공지 키값
     *                     ,memNo           BIGINT  -- 회원번호
     *                     ,roomNo          BIGINT  -- 방번호[0: 기본, 방 있을시에만]
     *                     ,roomNoticeConts VARCHAR -- 방송공지 등록글 내용
     * @Return s_return         INT     -- #-1: 공지글 등록 없음, 0: 에러, 1: 정상
     */
    @Select("CALL rd_data.p_broadcast_room_notice_upd(#{roomNoticeNo}, #{memNo}, #{roomNo}, #{roomNoticeConts})")
    Integer pMemberBroadcastNoticeUpd(Map<String, Object> param);

    /**
     * 방송방공지 삭제
     *
     * @param roomNoticeNo BIGINT  -- 방송공지 키값
     *                     ,memNo           BIGINT  -- 회원번호
     *                     ,roomNo          BIGINT  -- 방번호[0: 기본, 방 있을시에만]
     *                     ,delChrgrName     VARCHAR -- 삭제 관리자명
     * @Return s_return         INT     -- #-1: 공지글 등록 없음, 0: 에러, 1: 정상
     */
    @Select("CALL rd_data.p_broadcast_room_notice_del(#{roomNoticeNo}, #{memNo}, #{roomNo}, #{delChrgrName})")
    Integer pMemberBroadcastNoticeDel(BroadcastNoticeDelVo param);

    /**
     * 방송방공지 정보
     *
     * @param memNo BIGINT  -- 회원번호
     * @Return auto_no          INT         -- 자동증가 번호
     * mem_no           BIGINT      -- 회원 번호
     * conts            VARCHAR     -- 회원 아이디
     * ins_date         DATETIME    -- 등록 일자
     * upd_date         DATETIME    -- 수정 일자
     */
    @ResultMap({"ResultMap.BroadcastNoticeListOutVo"})
    @Select("CALL rd_data.p_broadcast_room_notice_sel(#{memNo}, #{roomNo})")
    List<Object> pMemberBroadcastNoticeList(Map<String, Object> param);

    /**
     * ##### 회원 방송방에서 받은 사연 리스트 (마이페이지에서 사용)
     *
     * @param memNo BIGINT			-- 회원번호
     *              ,pageNo 		INT UNSIGNED	-- 페이지 번호
     *              ,pagePerCnt 	INT UNSIGNED	-- 페이지 당 노출 건수 (Limit)
     * @Return Multi Rows
     * <p>
     * #1
     * cnt		BIGINT		-- 전체 수
     * <p>
     * #2
     * idx                  BIGINT          -- 자동등록 번호
     * dj_mem_no            BIGINT          -- 회원 번호(방장)
     * room_no              BIGINT          -- 방 번호
     * contents             VARCHAR         -- 내용
     * plus_yn              CHAR            -- 플러스[y,n]
     * status               BIGINT          -- 상태 0 정상,1삭제
     * writer_no            BIGINT          -- 회원 번호(보낸이)
     * writer_mem_id        VARCHAR         -- 회원 아이디(보낸이)
     * writer_mem_nick      VARCHAR         -- 회원 닉네임(보낸이)
     * writer_mem_sex       CHAR            -- 회원성별(보낸이)
     * writer_mem_profile   VARCHAR         -- 프로필(보낸이)
     * write_date           DATETIME        -- 등록일자
     */
    @ResultMap({"ResultMap.integer", "ResultMap.StoryResultVo"})
    @Select("CALL rd_data.p_broadcast_room_story_mem_list(#{memNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> pBroadcastRoomStoryMemList(Map<String, Object> param);
}
