package com.dalbit.event.service;

import com.dalbit.common.vo.ResMessage;
import com.dalbit.common.vo.ResVO;
import com.dalbit.event.proc.DrawEvent;
import com.dalbit.event.vo.*;
import com.dalbit.util.DBUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.InitialContext;
import java.util.*;

@Slf4j
@Service
public class DrawService {
    @Autowired
    DrawEvent drawEvent;

    // 랜덤 구하는 함수
    private static Integer getRandomNumber(Integer maxNum) {
        Integer result = null;

        if (maxNum < 1) {
            return null;
        }

        try {
            double randNum = Math.random();
            result = (int)(randNum * maxNum + 1);
        } catch (Exception e) {
            result = null;
        }

        return result;
    }

    // 추억의 뽑기(이벤트) 당첨내역 조회
    public ResVO getDrawWinningInfo(String memNo) {
        ResVO result = new ResVO();

        try {
            List<DrawGiftVO> winningInfo = drawEvent.getDrawWinningInfo(memNo);

            result.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), winningInfo);
        } catch (Exception e) {
            log.error("DrawService / getDrawTicketCnt => {}", e);
            result.setFailResVO();
        }

        return result;
    }

    // 추억의 뽑기(이벤트) 응모권 개수 조회
    public ResVO getDrawTicketCnt(String memNo) {
        ResVO result = new ResVO();

        try {
            DrawTicketVO ticketInfo = drawEvent.getDrawTicketCnt(memNo);

            if (ticketInfo != null) {
                result.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), ticketInfo.getCoupon_cnt());
            } else {
                result.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), 0);
            }
        } catch (Exception e) {
            log.error("DrawService / getDrawTicketCnt => {}", e);
            result.setFailResVO();
        }

        return result;
    }

    // 추억의 뽑기(이벤트) 뽑기 리스트 조회
    public ResVO getDrawListInfo(String memNo) {
        ResVO result = new ResVO();

        try {
            List<Object> data = drawEvent.getDrawListInfo(memNo);
            List<DrawListVO> temp = DBUtil.getList(data, 1, DrawListVO.class);
            List<DrawListVO> listInfo = new ArrayList<>(300);

            for (int i = 0; i < 300; i++) {
                listInfo.add(new DrawListVO());
                listInfo.get(i).setBbopgi_gift_pos_no(i + 1);
            }

            if (temp.size() > 0) {
                for (DrawListVO value : temp) {
                    listInfo.get((value.getBbopgi_gift_pos_no() - 1)).setAuto_no(value.getAuto_no());
                    listInfo.get((value.getBbopgi_gift_pos_no() - 1)).setMem_no(value.getMem_no());
                    listInfo.get((value.getBbopgi_gift_pos_no() - 1)).setBbopgi_gift_pos_no(value.getBbopgi_gift_pos_no());
                    listInfo.get((value.getBbopgi_gift_pos_no() - 1)).setBbopgi_gift_no(value.getBbopgi_gift_no());
                    listInfo.get((value.getBbopgi_gift_pos_no() - 1)).setIns_date(value.getIns_date());
                }
            }

            result.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), listInfo);

        } catch (Exception e) {
            log.error("DrawService / getDrawTicketCnt => {}", e);
            result.setFailResVO();
        }

        return result;
    }

    public ResVO putDrawSelect(Map<String, Object> param, String memNo) {
        ResVO result = new ResVO();
        Map<String, Object> outInfo = new HashMap<>();

        try {
            String[] selectList = ((String) param.get("selectList")).split(",");

            if (selectList.length < 1) {
                result.setFailResVO();
            } else {
                List<DrawGiftVO> resultInfo = new ArrayList<>();
                List<String> successList = new ArrayList<>(); // 성공 위치 배열
                Integer failCnt = 0;
                DrawWinningVO winningInfo = new DrawWinningVO(); // 당첨 내역 DB에서 조회
                winningInfo.setGiftInfo(drawEvent.getDrawWinningInfo(memNo));
                winningInfo.calRemainInfo(); // 남은 당첨 개수 계산
                int selectLength = selectList.length;

                // 사용할 수 있는 응모권 개수(남은 경품 개수) Check
                if (winningInfo.getTotalRemain().equals(0)) {
                    result.setResVO(ResMessage.C30102.getCode(), ResMessage.C30102.getCodeNM(), null);
                    return result;
                }

                // 입력한 뽑기 시도 회수보다 남은 응모권 개수
                if (winningInfo.getTotalRemain() < selectList.length) {
                    selectLength = winningInfo.getTotalRemain();
                    failCnt = selectList.length - winningInfo.getTotalRemain();
                }

                // 저장할 결과 데이터 공간 확보
                for (int i = 0; i < winningInfo.getGiftInfo().size(); i++) {
                    resultInfo.add(new DrawGiftVO());
                }

                // 뽑기 로직 시작
                for (int i = 0; i < selectLength; i++) {
                    Integer temp = getRandomNumber(winningInfo.getTotalRemain());
                    DrawTempResultVO tpResult = winningInfo.getResult(temp);

                    // 뽑기 결과 임시 저장
                    if (tpResult.getBbopgi_gift_name() != null) {
                        Integer targetNo = tpResult.getBbopgi_gift_no() - 1;

                        Integer insertRs = drawEvent.putDrawSelect(memNo, tpResult.getBbopgi_gift_no(), Integer.parseInt(selectList[i]));

                        if (insertRs == 1) {
                            successList.add(selectList[i]);
                            resultInfo.get(targetNo).setBbopgi_gift_no(tpResult.getBbopgi_gift_no());
                            resultInfo.get(targetNo).setBbopgi_gift_name(tpResult.getBbopgi_gift_name());
                            resultInfo.get(targetNo).setTemp_result_cnt(resultInfo.get(targetNo).getTemp_result_cnt() + 1);
                        } else {
                            log.error("DrawService / putDrawSelect DB ERROR insertRs => {}", insertRs);
                            failCnt++;
                        }

                    }
                }

                outInfo.put("successList", successList);
                outInfo.put("failCnt", failCnt);
                outInfo.put("resultInfo", resultInfo);
                if (failCnt > 0) {
                    result.setResVO(ResMessage.C30101.getCode(), ResMessage.C30101.getCodeNM(), outInfo);
                } else {
                    result.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), outInfo);
                }

            }
        } catch (Exception e) {
            log.error("DrawService / putDrawSelect => {}", e);
            result.setFailResVO();
        }

        return result;
    }

}
