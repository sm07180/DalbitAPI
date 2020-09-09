package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.ClipGiftListVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter @Setter
public class P_ClipGiftListVo {

    public P_ClipGiftListVo(){}
    public P_ClipGiftListVo(ClipGiftListVo clipGiftListVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(clipGiftListVo.getPage()) ? 1 : clipGiftListVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(clipGiftListVo.getRecords()) ? 10 : clipGiftListVo.getRecords();

        setMem_no(MemberVo.getMyMemNo(request));
        setCast_no(clipGiftListVo.getClipNo());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    /* InPut */
    private String mem_no;
    private String cast_no;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private String nickName;
    private String memSex;
    private String profileImage;
    private String item_code;
    private String item_name;
    private int byeol;
    private Date giftDate;
    private int enableFan;              //팬 여부 (0: 팬, 1: 아님)


}
