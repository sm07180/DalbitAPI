package com.dalbit.event.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_EventPageWinnerAddInfoListOutputVo {
    /* Output */
    private int state;
    private String prize_name;
    private int tax_amount;
    private String winner_name;
    private String winner_social_no;
    private String winner_phone;
    private String winner_email;
    private String winner_post_code;
    private String winner_address_1;
    private String winner_address_2;
    private String winner_add_file_1;
    private String winner_add_file_1_name;
    private String winner_add_file_2;
    private String winner_add_file_2_name;
}
