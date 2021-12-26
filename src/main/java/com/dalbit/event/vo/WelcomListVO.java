package com.dalbit.event.vo;

public class WelcomListVO {
    private Integer stepNo = 0; //		INT		-- 단계 번호
    private String memNo = ""; //		INT		-- 회원 번호
    private Integer playTime = 0; //		INT		-- 방송시간(초)
    private Integer rcvLikeCnt = 0; //	INT		-- 받은좋아요
    private Integer rcvDalCnt = 0; //	INT		-- 선물받은달
    private String djGiftReqYn = "n"; //	CHAR		-- 경품신청 완료
    private String djGiftRcvYn = "n"; //	CHAR		-- 경품받기 완료
    private String djGiftTheMonth = ""; //	DATE		-- 경품 일자(월)
    private Integer djGiftNo = 0; //	INT		-- 경품 번호
    private String djGiftName = ""; //	VARCHAR	-- 경품 이름
    private String insDate = ""; //		DATETIME	-- 등록일자
    private String updDate = ""; //		DATETIME	-- 수정일자

    public Integer getStepNo() {
        return stepNo;
    }

    public String getMemNo() {
        return memNo;
    }

    public Integer getPlayTime() {
        return playTime;
    }

    public Integer getRcvLikeCnt() {
        return rcvLikeCnt;
    }

    public Integer getRcvDalCnt() {
        return rcvDalCnt;
    }

    public String getDjGiftReqYn() {
        return djGiftReqYn;
    }

    public String getDjGiftRcvYn() {
        return djGiftRcvYn;
    }

    public String getDjGiftTheMonth() {
        return djGiftTheMonth;
    }

    public Integer getDjGiftNo() {
        return djGiftNo;
    }

    public String getDjGiftName() {
        return djGiftName;
    }

    public String getInsDate() {
        return insDate;
    }

    public String getUpdDate() {
        return updDate;
    }

    public void setStep_no(Integer step_no) {
        this.stepNo = step_no;
    }

    public void setMem_no(String mem_no) {
        this.memNo = mem_no;
    }

    public void setPlay_time(Integer play_time) {
        this.playTime = play_time;
    }

    public void setRcv_like_cnt(Integer rcv_like_cnt) {
        this.rcvLikeCnt = rcv_like_cnt;
    }

    public void setRcv_dal_cnt(Integer rcv_dal_cnt) {
        this.rcvDalCnt = rcv_dal_cnt;
    }

    public void setDj_gift_req_yn(String dj_gift_req_yn) {
        this.djGiftReqYn = dj_gift_req_yn;
    }

    public void setDj_gift_rcv_yn(String dj_gift_rcv_yn) {
        this.djGiftRcvYn = dj_gift_rcv_yn;
    }

    public void setDj_gift_the_month(String dj_gift_the_month) {
        this.djGiftTheMonth = dj_gift_the_month;
    }

    public void setDj_gift_no(Integer dj_gift_no) {
        this.djGiftNo = dj_gift_no;
    }

    public void setDj_gift_name(String dj_gift_name) {
        this.djGiftName = dj_gift_name;
    }

    public void setIns_date(String ins_date) {
        this.insDate = ins_date;
    }

    public void setUpd_date(String upd_date) {
        this.updDate = upd_date;
    }
}
