package com.dalbit.event.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DrawWinningVO {
    private List<DrawGiftVO> giftInfo = new ArrayList<>();

    private Integer[] giftCountInfo; // 정해진 당첨 목록
    private Integer[] useCountInfo; // 당첨된 목록
    private Integer[] remainCountInfo; // 당첨될 수 있는 목록

    private Integer totalRemain = 0;


    public void calRemainInfo () {
        this.giftCountInfo = new Integer[this.giftInfo.size()];
        this.useCountInfo = new Integer[this.giftInfo.size()];
        this.remainCountInfo = new Integer[this.giftInfo.size()];
        for (int i = 0; i < this.giftInfo.size(); i++) {
            this.giftCountInfo[i] = this.giftInfo.get(i).getGift_cnt();
            this.useCountInfo[i] = this.giftInfo.get(i).getUse_coupon_cnt();
            this.remainCountInfo[i] = this.giftCountInfo[i] - this.useCountInfo[i];
            this.totalRemain += remainCountInfo[i];
        }
    }

    // 남은 당첨 개수 계산
    public void calTotalRemain () {
        this.totalRemain = 0;
        for (int i = 0; i < this.giftInfo.size(); i++) {
            this.totalRemain += remainCountInfo[i];
        }
    }

    // 당첨항목 계산
    public DrawTempResultVO getResult (Integer tempResult) {
        DrawTempResultVO result = new DrawTempResultVO();
        Integer sumList[] = new Integer[this.giftInfo.size()];
        Integer totalSum = 0;
        for (int i = 0; i < this.giftInfo.size(); i++) {
            totalSum += this.remainCountInfo[i];
            sumList[i] = totalSum;
        }
        /*Integer sum1 = this.remain1;
        Integer sum2 = this.remain1 + this.remain2;
        Integer sum3 = this.remain1 + this.remain2 + this.remain3;
        Integer sum4 = this.remain1 + this.remain2 + this.remain3 + this.remain4;
        Integer sum5 = this.remain1 + this.remain2 + this.remain3 + this.remain4 + this.remain5;
        Integer sum6 = this.remain1 + this.remain2 + this.remain3 + this.remain4 + this.remain5 + this.remain6;
        Integer sum7 = this.remain1 + this.remain2 + this.remain3 + this.remain4 + this.remain5 + this.remain6 + this.remain7;
        Integer sum8 = this.remain1 + this.remain2 + this.remain3 + this.remain4 + this.remain5 + this.remain6 + this.remain7 + this.remain8;
*/

        for (int i = 0; i < this.giftInfo.size(); i++) {
            if (tempResult <= sumList[i]) {
                result.setBbopgi_gift_name(this.giftInfo.get(i).getBbopgi_gift_name());
                result.setBbopgi_gift_no(this.giftInfo.get(i).getBbopgi_gift_no());
                this.remainCountInfo[i]--;
                break;
            }
        }

        this.calTotalRemain();

        return result;
    }
}
