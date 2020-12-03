package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.ClipPlayListEditVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ClipPlayListEditVo extends P_ApiVo {

    public P_ClipPlayListEditVo(){}
    public P_ClipPlayListEditVo(ClipPlayListEditVo clipPlayListEditVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setSort_type(clipPlayListEditVo.getSortType());
        setDeleted_cast_no_list(clipPlayListEditVo.getDeleteClipNoList());
        setSorted_cast_no_list(clipPlayListEditVo.getSortClipNoList());
    }

    /* InPut */
    private String mem_no;                  //요청회원번호
    private int sort_type;                  //정렬구분 (0: 사용자설정[default], 1: 재생순, 2: 좋아요순, 3: 선물순, 4: 제목순, 5: 추가순)
    private String deleted_cast_no_list;
    private String sorted_cast_no_list;

}
