package com.dalbit.admin.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_QuestionOperateVo {
    private int pageNo;
    private int qnaIdx;
    private String answer;
    private String memo;
    private String mem_no;

    private String opName;
}