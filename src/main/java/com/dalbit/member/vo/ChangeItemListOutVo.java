package com.dalbit.member.vo;

import com.dalbit.member.vo.procedure.P_ChangeItemListVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChangeItemListOutVo {

    private String itemCode;
    private String itemName;
    private String itemImg;
    private String itemThumbnail;
    private int dalCnt;
    private int byeolCnt;
    private String desc;

    public ChangeItemListOutVo(){}
    public ChangeItemListOutVo(P_ChangeItemListVo target) {
        setItemCode(target.getItemCode());
        setItemName(target.getItemName());
        setItemImg(target.getItemImage());
        setItemThumbnail(target.getItemThumbnail());
        setDalCnt(target.getDalCnt());
        setByeolCnt(target.getByeolCnt());
        setDesc(target.getDesc());
    }
}
