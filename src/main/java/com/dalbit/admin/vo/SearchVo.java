package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchVo extends AdminBaseVo{
    private String reportYn;
    private String mem_no;
    private String room_no;
}
