package com.dalbit.admin.vo.procedure;

import com.dalbit.common.vo.BaseVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_QuestionDetailOutputVo extends BaseVo {
    private int qnaIdx;
    private int slct_type;
    private String platform;
    private String browser;
    private String ip;
    private String write_date;
    private int state;
    private String add_file;
    private String op_name;
    private String op_date;
    private String mem_no;
    private String mem_userid;
    private String mem_nick;
    private int mem_level;
    private String mem_grade;
    private String question_title;
    private String question_contents;
    private String email;

    private String answer;
}
