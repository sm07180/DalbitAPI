package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotiInsertVo extends AdminBaseVo{

    private String mem_no;
    private int slctType;
    private String notiContents;
    private String notiMemo;

}
