package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@ToString
public class AdminProcedureBaseVo implements Serializable {

    public AdminProcedureBaseVo(){}

    public AdminProcedureBaseVo(String totalCnt){
        this.totalCnt = Integer.parseInt(totalCnt);
    }

    public AdminProcedureBaseVo(int totalCnt){
        setTotalCnt(totalCnt);
    }

    public AdminProcedureBaseVo(int totalCnt, int pageStart, int pageCnt){
        this.totalCnt = totalCnt;
        this.pageStart = pageStart;
        this.pageCnt = pageCnt;
    }

    /*output*/
    private int totalCnt;
    private String tableColumnName;
    private int orderColumnIdx;
    private String orderDir;
    private int pageStart;
    private int pageCnt;

    private int searchStartNo;
    private int searchEndNo;

    public void setTotalCnt(int totalCnt){
        this.totalCnt = totalCnt;
        this.searchStartNo = (int)(Math.floor(this.pageStart-1) * this.pageCnt) + 1;
        this.searchEndNo = this.pageStart * this.pageCnt;
    }
}
