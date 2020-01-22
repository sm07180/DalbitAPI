package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagingVo {
    private int total;
    private int records;
    private int page;
    private int prev;
    private int next;
    private int totalPage;

    public PagingVo(int totalRecords,int pageNo, int pageCnt) {

        int endPage = (int) (Math.ceil(totalRecords / (double) pageCnt));

        if(totalRecords != 0){
            setTotal(totalRecords);
            setRecords(pageCnt);
            setPage(pageNo);
            setPrev(pageNo-1);
            setNext(pageNo+1);
            setTotalPage(endPage);
        }
    }
}
