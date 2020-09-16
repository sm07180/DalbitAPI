package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@ToString
public class AdminBaseVo implements Serializable {

    public void setPagingInfo(){
        this.searchStart = currentPage <= 1 ? 0 : (currentPage - 1) * pageCount;
        this.searchEnd = pageCount + 1;
    }

    int currentPage;
    int pageCount;
    boolean isEndPage;

    int searchStart;
    int searchEnd;

    int pageNo;
    int pageCnt;
    int totalCnt;

    String notificationYn;

    List<AdminMenuVo> adminMenuList;
}
