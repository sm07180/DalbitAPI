package com.dalbit.common.controller;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import com.dalbit.common.vo.CodeVo;

@Slf4j
@RestController
public class CommonController {

    @Autowired
    GsonUtil gsonUtil;

    @GetMapping("splash")
    public String splash(){
        CodeVo[] memGubun = new CodeVo[6];
        CodeVo[] osGubun = new CodeVo[3];
        CodeVo[] roomType = new CodeVo[12];
        CodeVo[] roomState = new CodeVo[4];
        CodeVo[] roomRight = new CodeVo[5];
        CodeVo[] declarReason = new CodeVo[5];

        memGubun[0] = new CodeVo("p", "전화번호", 0);
        memGubun[1] = new CodeVo("f", "페이스북", 1);
        memGubun[2] = new CodeVo("g", "구글", 2);
        memGubun[3] = new CodeVo("k", "카카오", 3);
        memGubun[4] = new CodeVo("n", "네이버", 4);
        memGubun[5] = new CodeVo("a", "익명/비회원", 5);

        osGubun[0] = new CodeVo("1", "Android", 0);
        osGubun[1] = new CodeVo("2", "IOS", 1);
        osGubun[2] = new CodeVo("3", "PC", 2);

        roomType[0] = new CodeVo("0", "일상", 0);
        roomType[1] = new CodeVo("1", "노래-연주", 1);
        roomType[2] = new CodeVo("2", "노래방", 2);
        roomType[3] = new CodeVo("3", "수다-쳇", 3);
        roomType[4] = new CodeVo("4", "고민-사연", 4);
        roomType[5] = new CodeVo("5", "건강-스포츠", 5);
        roomType[6] = new CodeVo("6", "여행-힐링", 6);
        roomType[7] = new CodeVo("7", "외국어", 7);
        roomType[8] = new CodeVo("8", "책-스토리", 8);
        roomType[9] = new CodeVo("9", "연애-오락", 9);
        roomType[10] = new CodeVo("10", "ASMR", 10);
        roomType[11] = new CodeVo("11", "기타", 11);

        roomState[0] = new CodeVo("1", "방송중", 0);
        roomState[1] = new CodeVo("2", "방송준비중", 1);
        roomState[2] = new CodeVo("3", "통화중", 2);
        roomState[3] = new CodeVo("4", "방송종료", 3);

        roomRight[0] = new CodeVo("0", "일반", 0);
        roomRight[1] = new CodeVo("1", "메니저", 1);
        roomRight[2] = new CodeVo("2", "게스트", 2);
        roomRight[3] = new CodeVo("3", "방장", 3);

        declarReason[0] = new CodeVo("1", "프로필사진", 0);
        declarReason[1] = new CodeVo("2", "음란성", 1);
        declarReason[2] = new CodeVo("3", "광고 및 상업성", 2);
        declarReason[3] = new CodeVo("4", "욕설 및 비방성", 3);
        declarReason[4] = new CodeVo("5", "기타", 4);

        HashMap<String, Object> data = new HashMap<>();
        data.put("memGubun", memGubun);
        data.put("osGubun", osGubun);
        data.put("roomType", roomType);
        data.put("roomState", roomState);
        data.put("roomRight", roomRight);
        data.put("declarReason", declarReason);

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, data));
    }
}
