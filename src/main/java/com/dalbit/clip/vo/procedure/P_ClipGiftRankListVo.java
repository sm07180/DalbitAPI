package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.ClipGiftRankListVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ClipGiftRankListVo extends P_ApiVo {

    public P_ClipGiftRankListVo(){}
    public P_ClipGiftRankListVo(ClipGiftRankListVo clipGiftRankListVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(clipGiftRankListVo.getPage()) ? 1 : clipGiftRankListVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(clipGiftRankListVo.getRecords()) ? 10 : clipGiftRankListVo.getRecords();

        setMem_no(MemberVo.getMyMemNo(request));
        setCast_no(clipGiftRankListVo.getClipNo());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    /* InPut */
    private String mem_no;
    private String cast_no;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private String nickName;            //닉네임
    private String memSex;              //성별
    private String profileImage;        //프로필이미지
    private int enableFan;              //팬 여부 (0: 팬, 1: 아님)
    private int giftDal;                //선물한 달 수


}
