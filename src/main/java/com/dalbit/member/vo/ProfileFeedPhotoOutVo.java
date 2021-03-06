package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileFeedPhotoOutVo {
    private String img_name;	//String            이미지 이름  (등록시 필요)
    private Long photo_no;		//BIGINT            사진번호
    private Long feed_reg_no;	//BIGINT            피드번호
    private Long mem_no;		//BIGINT            회원번호
    private String ins_date;	//DATETIME          등록일


    private ImageVo imgObj;     //URL 정보가 담긴 obj (Service 에서 따로 처리)
}
