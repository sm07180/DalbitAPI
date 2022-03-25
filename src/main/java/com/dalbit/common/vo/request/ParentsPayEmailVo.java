package com.dalbit.common.vo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParentsPayEmailVo {
    private String memNo;
    private String orderId;
    private String email;

    private String paymentUserName; // 결제자 이름
    private String paymentDate; // 결제 일시
    private String paymentMethod; // 결제 수단
    private String paymentAccount; // 결제정보(계좌)
    private String paymentBank; // 결제정보(은행)
    private String paymentProduct; // 결제 상품
    private String paymentQuantity; // 결제 수량
    private String paymentPrice; // 결제금액
}
