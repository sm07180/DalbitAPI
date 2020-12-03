package com.dalbit.event.vo.procedure;

import com.dalbit.common.code.EventCode;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.event.vo.request.Apply004Vo;
import com.dalbit.event.vo.request.ApplyVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ApplyVo extends P_ApiVo {
    private String mem_no;
    private int event_idx;
    private boolean isMulti;
    private int platform;
    private String name;
    private String contact_no;
    private String recv_data_1;
    private String recv_data_2;
    private String recv_data_3;
    private String recv_data_4;
    private String recv_data_5;

    public P_ApplyVo(ApplyVo applyVo, HttpServletRequest request){
        DeviceVo deviceVo = new DeviceVo(request);
        this.mem_no = MemberVo.getMyMemNo(request);
        this.event_idx = applyVo.getEventIdx();
        this.name = applyVo.getName();
        this.contact_no = applyVo.getContactNo();
        this.recv_data_1 = applyVo.getRecvData1();
        this.recv_data_2 = applyVo.getRecvData2();
        this.recv_data_3 = applyVo.getRecvData3();
        this.recv_data_4 = applyVo.getRecvData4();
        this.recv_data_5 = applyVo.getRecvData5();
        this.platform = deviceVo.getOs();

        for(EventCode e : EventCode.values()){
            if(e.getEventIdx() == this.event_idx){
                this.isMulti = e.isMulti();
                break;
            }
        }
    }

    public P_ApplyVo(Apply004Vo applyVo, HttpServletRequest request){
        DeviceVo deviceVo = new DeviceVo(request);
        this.mem_no = MemberVo.getMyMemNo(request);
        this.event_idx = applyVo.getEventIdx();
        this.name = applyVo.getName();
        this.contact_no = applyVo.getContactNo();
        this.recv_data_1 = applyVo.getIntroduce();
        this.platform = deviceVo.getOs();

        for(EventCode e : EventCode.values()){
            if(e.getEventIdx() == this.event_idx){
                this.isMulti = e.isMulti();
                break;
            }
        }
    }
}
