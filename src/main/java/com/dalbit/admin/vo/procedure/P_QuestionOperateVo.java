package com.dalbit.admin.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_QuestionOperateVo extends P_ApiVo {
    private int pageNo;
    private int qnaIdx;
    private String qnaTitle;
    private String answer;
    private String memo;
    private String mem_no;
    private String title;
    private int noticeType;
    private String phone;
    private String email;
    private String qnaType;
    private String qnaContent;
    private String fileName;

    private String opName;
}
