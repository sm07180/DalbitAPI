package com.dalbit.clip.vo;

import com.dalbit.clip.vo.procedure.P_ClipRecommendLeaderListVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

@Getter @Setter
public class ClipRecommendLeaderListVo {

    public ClipRecommendLeaderListVo() {}

    public ClipRecommendLeaderListVo(P_ClipRecommendLeaderListVo target) {
        setRecDate(target.getRec_date());
        setClipNo(target.getCast_no());
        setTitleMsg(target.getTitle_msg());
        setThumbUrl(target.getThumb_url());
        setTitle(target.getTitle());
        setMemNo(target.getMem_no());
        setNickNm(target.getMem_nick());

        Calendar calendar = Calendar.getInstance();
        String date = target.getRec_date();
        String[] dates = date.replace(" 00:00:00", "").split("-");
        int year = Integer.parseInt(dates[0]);
        int month = Integer.parseInt(dates[1]);
        int day = Integer.parseInt(dates[2]);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.set(year, month - 1, day);

        setTime(date.substring(5,7).replace("-"," ")+"월 "+calendar.get(Calendar.WEEK_OF_MONTH)+"주차");
    }
    private String recDate;         // 날짜
    private String time;            // 화면에 보일 월, 주차
    private String clipNo;          // 클립번호
    private String titleMsg;        // 소개제목
    private String thumbUrl;       // 썸네일 url
    private String title;           // 클립 제목
    private String memNo;           // 요청회원번호
    private String nickNm;          // 회원닉네임

}
