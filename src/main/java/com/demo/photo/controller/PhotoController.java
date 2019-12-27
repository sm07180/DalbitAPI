package com.demo.photo.controller;


import com.demo.photo.service.PhotoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
@Slf4j
public class PhotoController {

    @Autowired
    PhotoService service;


    @GetMapping("/photo")
    public String initFiles() {
        return "uploadFiles";
    }


    /**
     * @param request
     * @param response
     * @throws IOException 저장된 이미지 요청
     */
    //@GetMapping(value = {"/**/*.jpeg", "/**/*.jpg", "/**/*.png"})
    public void getImg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("get Image - request ImageUrl : {}", request.getRequestURI());

        // Resize 사용 여부 체크 (URL에 resize를 포함할 경우 사용 (ex. "/files/img/resize/"))
        boolean isResize = false;
        if (request.getRequestURL().indexOf("/resize/") > -1) {
            isResize = true;
        }

        service.getImg(request, response, isResize);
    }


    /**
     * @param files
     * @return
     * @throws IOException 이미지 파일 임시 저장 (Temp)
     */
    @ResponseBody
    @PostMapping("/upload")
    public Map<String, String> uploadTempImage(@RequestParam Object files) throws IOException {
        log.info("Temp Image Upload start {}", files.getClass().getName());

        log.info("#### Temp 이미지 저장 요청");
        log.info("#### 요청 파일 형식:{}", files.getClass().getName());
        if (files instanceof MultipartFile) {
            return service.uploadTempImage((MultipartFile) files);
        } else if (files instanceof String) {
            return service.uploadTempImageFromDataURL((String) files);
        } else {
            //오류처리 필요
            return null;
        }

    }


    /**
     * @param files
     * @return
     * @throws IOException 이미지 파일 저장 완료 (done)
     */
    @ResponseBody
    @PostMapping("/done")
//	public Map<String, String> uploadDoneImage(@RequestParam MultipartFile[] files) throws IOException {
    public Map<String, String> uploadDoneImage(MultipartFile[] files) throws IOException {
        log.info("Done Image Upload start");
        return service.uploadDoneImage(files);
    }

}
