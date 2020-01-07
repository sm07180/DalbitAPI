package com.demo.common.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagingVo {
    private int total;
    private int recordPerPage;
    private int page;
    private int prev;
    private int next;
    private int totalPage;
}
