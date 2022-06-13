package com.dalbit.event.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class FestivalStoryVo {
    private String photoUrl = DalbitUtil.getProperty("server.photo.url");

    private int auto_no;           // 자동등록 번호
    private String story_conts;    // 스토리내용
    private String mem_no;         // 회원 번호
    private String mem_id;         // 회원 아이디
    private String mem_nick;       // 회원 닉네임
    private String mem_sex;        // 회원성별
    private String image_profile;  // 프로필
    private int mem_level;         // 레벨
    private String mem_state;      // 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...)
    private String ins_date;       // 등록일자
    private String upd_date;       // 수정일자
    private ImageVo imageProfileData;  // 프로필 이미지

    public void setImage_profile(String image_profile) {
        this.image_profile = image_profile;
        if(!StringUtils.isEmpty(this.mem_sex)) {
            this.imageProfileData = new ImageVo(image_profile, this.mem_sex, photoUrl);
        }
    }

    public void setMem_sex(String mem_sex) {
        this.mem_sex = mem_sex;
        if(!StringUtils.isEmpty(this.image_profile)) {
            this.imageProfileData = new ImageVo(this.image_profile, mem_sex, photoUrl);
        }
    }

    public void setIns_date(String ins_date) {
        if(ins_date != null) {
            this.ins_date = ins_date.split("\\.")[0];
        }
    }
}
