package com.dalbit.event.vo;

public class WelcomeUserVO {
    private Integer stepNo; //		INT		-- 단계 번호
    private String memNo; //		INT		-- 회원 번호
    private Integer viewTime; //		INT		-- 시청시간(초)
    private Integer sendLikeCnt; //	INT		-- 보낸좋아요
    private Integer payDalCnt; //	INT		-- 결재한달
    private String memGiftReqYn; //	CHAR		-- 경품신청 완료
    private String memGiftRcvYn; //	CHAR		-- 경품받기 완료
    private String memGiftTheMonth; // DATE		-- 경품 일자(월)
    private Integer memGiftNo; //	INT		-- 경품 번호
    private String memGiftName; //	VARCHAR	-- 경품 이름
    private String insDate; //		DATETIME	-- 등록일자
    private String updDate; //		DATETIME	-- 수정일자

    public Integer getStepNo() {
        return stepNo;
    }

    public String getMemNo() {
        return memNo;
    }

    public Integer getViewTime() {
        return viewTime;
    }

    public Integer getSendLikeCnt() {
        return sendLikeCnt;
    }

    public Integer getPayDalCnt() {
        return payDalCnt;
    }

    public String getMemGiftReqYn() {
        return memGiftReqYn;
    }

    public String getMemGiftRcvYn() {
        return memGiftRcvYn;
    }

    public String getMemGiftTheMonth() {
        return memGiftTheMonth;
    }

    public Integer getMemGiftNo() {
        return memGiftNo;
    }

    public String getMemGiftName() {
        return memGiftName;
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

    public void setView_time(Integer view_time) {
        this.viewTime = view_time;
    }

    public void setSend_like_cnt(Integer send_like_cnt) {
        this.sendLikeCnt = send_like_cnt;
    }

    public void setPay_dal_cnt(Integer pay_dal_cnt) {
        this.payDalCnt = pay_dal_cnt;
    }

    public void setMem_gift_req_yn(String mem_gift_req_yn) {
        this.memGiftReqYn = mem_gift_req_yn;
    }

    public void setMem_gift_rcv_yn(String mem_gift_rcv_yn) {
        this.memGiftRcvYn = mem_gift_rcv_yn;
    }

    public void setMem_gift_the_month(String mem_gift_the_month) {
        this.memGiftTheMonth = mem_gift_the_month;
    }

    public void setMem_gift_no(Integer mem_gift_no) {
        this.memGiftNo = mem_gift_no;
    }

    public void setMem_gift_name(String mem_gift_name) {
        this.memGiftName = mem_gift_name;
    }

    public void setIns_date(String ins_date) {
        this.insDate = ins_date;
    }

    public void setUpd_date(String upd_date) {
        this.updDate = upd_date;
    }
}
