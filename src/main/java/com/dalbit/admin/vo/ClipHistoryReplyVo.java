package com.dalbit.admin.vo;

import com.dalbit.common.vo.PagingVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClipHistoryReplyVo extends AdminBaseVo {

    /* output */
    private String replyIdx;
    private String castNo;
    private String castMemNo;
    private String writerMemNo;
    private String contents;
    private String status;
    private String writeDate;
    private String lastUpdDate;
    private String memSex;
    private String memBirthYear;
    private String memNick;
    private String profileImage;

}
