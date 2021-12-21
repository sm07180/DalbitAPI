package com.dalbit.event.vo;

public class LikeTreeRankingVO {
    private Integer rankNo = 0; //    BIGINT    -- 순위
    private Integer seqNo = 0; //    BIGINT    -- 회차 번호
    private Integer sendLikeCnt = 0; //  BIGINT    -- 좋아요수
    private Integer sendLikeScoreCnt = 0; // BIGINT    -- 좋아요점수
    private Integer sendBoosterCnt = 0; //  BIGINT    -- 부스터수
    private Integer sendBoosterScoreCnt = 0; // BIGINT  -- 부스터점수
    private Integer totScoreCnt = 0; //  BIGINT    -- 총점수
    private String memNo = ""; //    BIGINT    -- 회원 번호
    private String memId = ""; //    VARCHAR  -- 회원 아이디
    private String memNick = ""; //  VARCHAR  -- 회원 닉네임
    private String memSex = ""; //    CHAR    -- 회원성별
    private String imageProfile = ""; //  VARCHAR  -- 프로필
    private Integer memLevel = 0; //  BIGINT    -- 레벨
    private Integer memState = 0; //  BIGINT    -- 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...)
    private String insDate; //    DATETIME  -- 등록일자
    private String updDate; //   DATETIME  -- 수정일자

    public Integer getRankNo() {
        return rankNo;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public Integer getSendLikeCnt() {
        return sendLikeCnt;
    }

    public Integer getSendLikeScoreCnt() {
        return sendLikeScoreCnt;
    }

    public Integer getSendBoosterCnt() {
        return sendBoosterCnt;
    }

    public Integer getSendBoosterScoreCnt() {
        return sendBoosterScoreCnt;
    }

    public Integer getTotScoreCnt() {
        return totScoreCnt;
    }

    public String getMemNo() {
        return memNo;
    }

    public String getMemId() {
        return memId;
    }

    public String getMemNick() {
        return memNick;
    }

    public String getMemSex() {
        return memSex;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public Integer getMemLevel() {
        return memLevel;
    }

    public Integer getMemState() {
        return memState;
    }

    public String getInsDate() {
        return insDate;
    }

    public String getUpdDate() {
        return updDate;
    }

    public void setRankNo(Integer rankNo) {
        this.rankNo = rankNo;
    }

    public void setSeq_no(Integer seq_no) {
        this.seqNo = seq_no;
    }

    public void setSend_like_cnt(Integer send_like_cnt) {
        this.sendLikeCnt = send_like_cnt;
    }

    public void setSend_like_score_cnt(Integer send_like_score_cnt) {
        this.sendLikeScoreCnt = send_like_score_cnt;
    }

    public void setSend_booster_cnt(Integer send_booster_cnt) {
        this.sendBoosterCnt = send_booster_cnt;
    }

    public void setSend_booster_score_cnt(Integer send_booster_score_cnt) {
        this.sendBoosterScoreCnt = send_booster_score_cnt;
    }

    public void setTot_score_cnt(Integer tot_score_cnt) {
        this.totScoreCnt = tot_score_cnt;
    }

    public void setMem_no(String mem_no) {
        this.memNo = mem_no;
    }

    public void setMem_id(String mem_id) {
        this.memId = mem_id;
    }

    public void setMem_nick(String mem_nick) {
        this.memNick = mem_nick;
    }

    public void setMem_sex(String mem_sex) {
        this.memSex = mem_sex;
    }

    public void setImage_profile(String image_profile) {
        this.imageProfile = image_profile;
    }

    public void setMem_level(Integer mem_level) {
        this.memLevel = mem_level;
    }

    public void setMem_state(Integer mem_state) {
        this.memState = mem_state;
    }

    public void setIns_date(String ins_date) {
        this.insDate = ins_date;
    }

    public void setUpd_date(String upd_date) {
        this.updDate = upd_date;
    }
}
