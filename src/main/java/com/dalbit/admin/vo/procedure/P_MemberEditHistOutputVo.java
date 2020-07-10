package com.dalbit.admin.vo.procedure;

import com.dalbit.common.vo.BaseVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class P_MemberEditHistOutputVo extends BaseVo {
    private int rowNum;
    private String opName;
    private Date editDate;
    private String editDateFormat;
    private String editContents;


    public void setEditDate(Date editDate){
        this.editDate = editDate;
        this.editDateFormat = DalbitUtil.convertDateFormat(editDate, "yyyy.MM.dd HH:mm:ss");
    }
}
