package com.dalbit.event.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Getter
@Setter
public class ShareEventVo {
    /* input */
    private String memNo;
    private String pageNo;
    private String pagePerCnt;

    /* output */
    private String tail_no; // 댓글번호
    private String tail_mem_no; // 회원번호
    private String tail_mem_id; // 회원아이디
    private String tail_mem_sex; // 회원성별
    private String tail_mem_ip; // 회원아이피
    private String tail_conts; // 댓글내용
    private String login_media; // 미디어
    private String mem_nick; // 닉네임
    private String mem_id; // 아이디
    private String mem_userid; // 추가아이디
    private String image_profile; // 프로필이미지정보
    private Date ins_date; // 등록일자
    private ImageVo profImg;
    private String photoUrl = DalbitUtil.getProperty("server.photo.url");

    public void setTail_mem_sex(String tail_mem_sex) {
        if(!StringUtils.isEmpty(this.image_profile)) {
            this.profImg = new ImageVo(this.image_profile, tail_mem_sex, this.photoUrl);
        }
    }

    public void setImage_profile(String image_profile) {
        if(!StringUtils.isEmpty(this.tail_mem_sex)) {
            this.profImg = new ImageVo(image_profile, this.tail_mem_sex, this.photoUrl);
        }
    }
}
