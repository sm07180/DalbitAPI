package com.dalbit.event.vo;

import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ElectricSignFanListOutVo {
    private Long rankNo;
    private String mem_no;
    private Integer tot_score_cnt;
    private String mem_id;
    private String mem_nick;
    private String mem_sex;
    private String image_profile;
    private Integer mem_level;
    private Integer mem_state;
    private LocalDateTime ins_date;
    private LocalDateTime upd_date;

    private ImageVo profImg;
}
