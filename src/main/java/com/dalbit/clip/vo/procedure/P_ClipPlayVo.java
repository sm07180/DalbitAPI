package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.ClipPlayVo;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ClipPlayVo extends P_ApiVo {

    private int memLogin;
    private String mem_no;
    private String cast_no;
    private int os;
    private String ip;

    public P_ClipPlayVo(){}
    public P_ClipPlayVo(ClipPlayVo clipPlayVo, HttpServletRequest request){
        setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        setMem_no(MemberVo.getMyMemNo(request));
        setCast_no(clipPlayVo.getClipNo());
        DeviceVo deviceVo = new DeviceVo(request);
        setOs(deviceVo.getOs());
        setIp(deviceVo.getIp());
    }

    public P_ClipPlayVo(P_ClipReplyAddVo pClipReplyListVo, HttpServletRequest request){
        setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        setMem_no(MemberVo.getMyMemNo(request));
        setCast_no(pClipReplyListVo.getCast_no());
        DeviceVo deviceVo = new DeviceVo(request);
        setOs(deviceVo.getOs());
        setIp(deviceVo.getIp());
    }

    public P_ClipPlayVo(P_ClipReplyEditVo pClipReplyEditVo, HttpServletRequest request){
        setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        setMem_no(MemberVo.getMyMemNo(request));
        setCast_no(pClipReplyEditVo.getCast_no());
        DeviceVo deviceVo = new DeviceVo(request);
        setOs(deviceVo.getOs());
        setIp(deviceVo.getIp());
    }

    public P_ClipPlayVo(P_ClipReplyDeleteVo clipReplyDeleteVo, HttpServletRequest request){
        setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        setMem_no(MemberVo.getMyMemNo(request));
        setCast_no(clipReplyDeleteVo.getCast_no());
        DeviceVo deviceVo = new DeviceVo(request);
        setOs(deviceVo.getOs());
        setIp(deviceVo.getIp());
    }

}
