package com.demo.service;

import com.demo.util.Base64Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;


@Service
@Slf4j
public class PhotoService {

    @Value("${photo.upload.path.temp}")
    private String tempPath;
    @Value("${photo.upload.path}")
    private String imgPath;
    @Value("${photo.default.img.path}")
    private String defaultImgPath;


    private HashMap<String, String> mapDataURLPreFix = new HashMap<String, String>() {
        {
            // DataURL : 확장자
            put("data:image/png;base64,", "png");
            put("data:image/jpg;base64,", "jpg");
            put("data:image/jpeg;base64,", "jpeg");
        }
    };




    /**
     * @param file
     * @return
     * @throws IOException
     *
     *      Temp 이미지 파일 업로드 (MultipartFile)
     *
     *          1. 디렉토리 생성 (디폴트 Temp 디렉토리 생성, 존재시 무시)
     *          2. 업로드 이미지 파일 Temp로 복사
     *
     */
    public Map<String, String> uploadTempImage(MultipartFile file) throws IOException {

        String fileTempName = uploadFile(file, tempPath);

        Map<String, String> jsonReturn = new HashMap<String, String>();
        jsonReturn.put("result", "success");
        jsonReturn.put("fileTempName", "/temp/" + fileTempName);

        return jsonReturn;
    }


    /**
     * @param file
     * @return
     * @throws IOException
     *
     *      Temp 이미지 파일 업로드 (DataUrl)
     *
     *          1. 디렉토리 생성 (디폴트 Temp 디렉토리 생성, 존재시 무시)
     *          2. 업로드 이미지 파일 Temp로 복사
     *
     */
    public Map<String, String> uploadTempImageFromDataURL(String file) throws IOException {
        Map<String, String> jsonReturn = new HashMap<String, String>();

        String fileTempName = uploadFile(file, tempPath);

        jsonReturn.put("result", "success");
        jsonReturn.put("fileTempName", "/temp/" + fileTempName);

        return jsonReturn;
    }





    /**
     * @param files File 객체 전달 (MultipartFile)
     * @return
     * @throws IOException Done 이미지 파일 업로드 ( 다중 )
     *                     <p>
     *                     1. 디렉토리 생성 (디폴트 Done 디렉토리 생성, 존재시 무시)
     *                     2. 파일 확장자 통일
     *                     3. 업로드 이미지 파일 Done 디렉토리로 복사
     */
    public Map<String, String> uploadDoneImage(MultipartFile[] files) throws IOException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY/MM/dd/HH/");    //done 디렉토리 경로 생성 (현재일시 기준 년/월/일/시)
        String middlePath = dateFormat.format(new Date());

        String donPath = imgPath + middlePath;    // DefaultPath + YYYY/MM/dd/HH/
        File doneDir = new File(donPath);
        log.info("DonePath : {}\nDoneDir Exists : {}", donPath, doneDir.exists());
        doneDir.mkdirs();
        //done 디렉토리 생성
        System.out.println(doneDir.exists());
        if (!doneDir.exists()) {
            doneDir.mkdirs();
        }


        Map<String, String> jsonReturn = new HashMap<String, String>();
        jsonReturn.put("result", "success");
//		jsonReturn.put("fileTempName", fileTempName);

        return jsonReturn;
    }


    /**
     * @param request
     * @param response
     * @param onlyReSize        Resize 모드 (false:사용X , true:사용O)
     * @throws IOException
     *
     *      이미지 가져오기
     *        1.요청 URL의 경로에서 이미지 파일 조회
     *          1-1.없을 경우 오류
     *          1-2.존재할 경우 Stream 전달
     *        2.요쳥 URL의 경로가 "/" default일 경우 default이미지 파일 Stream 전달
     *
     */
    public void getImg(HttpServletRequest request, HttpServletResponse response, boolean onlyReSize) throws IOException {
        String url = request.getRequestURI();
        System.out.println(url);

        String replacePath = "";
        if (onlyReSize) {
            replacePath += "/resize";
        }
        //이미지 파일 경로
        String filePath = url.replaceAll(replacePath, "");
        System.out.println(imgPath);

        //이미지 옵션 값 확인
        Enumeration<String> options = request.getParameterNames();
        List<String> list = Collections.list(options);
        String thumbnailSize = null;
        if (list.size() > 0) {
            Pattern p = Pattern.compile("[0-9]+[x,X][0-9]+");
            for (String option : list) {
                System.out.println(option + ": " + p.matcher(option).find());
                if (p.matcher(option).find()) {
                    thumbnailSize = option;
                }
            }
        }

        log.info("File Search name : {}", filePath);
        String path = imgPath + filePath;
        log.info("File Search path : {}", path);

        File file = new File(path);
        log.info("FILE exists : {}", file.exists());
        if (!file.exists()) {
            file = new File(defaultImgPath);
        }

        log.info("FILE size : {}", file.length());

        int fileSIze = (int) file.length();

        String contentType = Files.probeContentType(file.toPath());
        response.setContentType(contentType);
        OutputStream out = response.getOutputStream();

        InputStream in;

        if (thumbnailSize != null) {
            String[] subStr = thumbnailSize.toUpperCase().split("X");
            int width = Integer.parseInt(subStr[0]);
            int height = Integer.parseInt(subStr[1]);
            in = makeThumbnail(path, width, height, null, onlyReSize);
        } else {
            in = new FileInputStream(file);
        }
        // copy from in to out
        try {
            IOUtils.copy(in, out, fileSIze);
        } finally {
            in.close();
            out.close();
        }
    }




    /**
     * @param imgUrl  Target 이미지 URL (경로 + 파일명 + 파일 확장자)
     * @param width   Thumbnail Width Size
     * @param height  Thumbnail height Size
     * @param outPath Thumbnail 파일 저장 경로 (null 일 경우 파일 저장 하지 않음.)
     * @return
     * @throws IOException Thumbnail 생성
     */
    private InputStream makeThumbnail(String imgUrl, int width, int height, String outPath, boolean onlyResize) throws IOException {
        log.debug("## makeThumbnail Start ");

        // 저장된 원본파일로부터 BufferedImage 객체를 생성합니다.
        File imgFile = new File(imgUrl);
        BufferedImage srcImg = ImageIO.read(imgFile);

        // 썸네일의 너비와 높이 입니다.
        int dw = width;
        int dh = height;

        // 원본 이미지의 너비와 높이 입니다.
        int ow = srcImg.getWidth();
        int oh = srcImg.getHeight();

        // 원본 너비를 기준으로 하여 썸네일의 비율로 높이를 계산합니다.
        int nw = ow;
        int nh = (ow * dh) / dw;

        // 계산된 높이가 원본보다 높다면 crop이 안되므로 원본 높이를 기준으로 썸네일의 비율로 너비를 계산합니다.
        if (nh > oh) {
            nw = (oh * dw) / dh;
            nh = oh;
        }
        log.debug("## makeThumbnail ( dw : {} dh : {} ow : {} oh : {} nw : {} nh : {} ) ####", dw, dh, ow, oh, nw, nh);

        BufferedImage destImg;
        if (onlyResize) {
            destImg = Scalr.resize(srcImg, dw, dh);
        } else {
            // 계산된 크기로 원본이미지를 가운데에서 crop 합니다.
            BufferedImage cropImg = Scalr.crop(srcImg, (ow - nw) / 2, (oh - nh) / 2, nw, nh);
            // crop된 이미지로 썸네일을 생성합니다.
            destImg = Scalr.resize(cropImg, dw, dh);
        }


        // 썸네일이 저장 경로가 들어왔을 경우 파일 생성
        if (outPath != null && !outPath.trim().equals("")) {
            // 썸네일을 저장합니다. 이미지 이름 앞에 "THUMB_" 를 붙여 표시했습니다.
            String thumbName = imgPath + outPath + "THUMB_" + imgFile.getName();
            File thumbFile = new File(thumbName);
            thumbFile.mkdirs();
            ImageIO.write(destImg, FilenameUtils.getExtension(imgFile.getName()), thumbFile);
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(destImg, FilenameUtils.getExtension(imgFile.getName()), os);

        InputStream is = new ByteArrayInputStream(os.toByteArray());
        return is;
    }


    /**
     * @param inputFile 업로드할 파일 객체 (MultipartFile, File 두개만 가능)
     * @return
     * @throws IOException 서버 파일 업로드
     */
    public String uploadFile(Object inputFile, String uploadPath) throws IOException {
        InputStream inputStream = null;
        String fileRealName = "";        //실제 업로드 파일 이름 + 확장자
        String fileExtension = "";        //실제 업로드 파일 확장자

        // MultipartFile
        if (inputFile instanceof MultipartFile) {
            MultipartFile multipartFile = (MultipartFile) inputFile;

            inputStream = new ByteArrayInputStream(multipartFile.getBytes());
            fileRealName = multipartFile.getOriginalFilename();
            fileExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        }
        // File
        else if (inputFile instanceof File) {
            File file = (File) inputFile;

            inputStream = new FileInputStream(file);
            fileRealName = file.getName();
            fileExtension = FilenameUtils.getExtension(file.getName());
        }
        // DataUrl
        else if (inputFile instanceof String) {
            String dataUrl = (String) inputFile;

            String prefix = dataUrl.substring(0, dataUrl.indexOf(",") + 1);
            System.out.println("#######################################" + prefix);

            if (mapDataURLPreFix.get(prefix) != null) {
                dataUrl = dataUrl.replace(prefix, "");
                //Base64Util.decodeBase64ToBytes(dataUrl)
                inputStream = new ByteArrayInputStream(Base64Util.decodeBase64ToBytes(dataUrl));
                fileRealName = "TempUploadIMG";
                fileExtension = mapDataURLPreFix.get(prefix);
            } else {
                //오류처리
            }
        } else {
            //오류 처리
            log.error("지원하지 않는 형식 입니다. :{}", inputFile.getClass().getName());
            return null;
//			throw new Exception("지원하지 않는 형식 입니다. :"+ inputFile.getClass().getName());
        }



        //Upload 디렉토리 생성
        File tempDir = new File(uploadPath);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }

        String fileTempName = UUID.randomUUID().toString() + "." + fileExtension;        // Photo 서버에 올라가게 될 파일 이름 + 확장자  ****** YYYYMMDD+randem(3)
        log.info("upload PATH : {}!!", uploadPath + "/" + fileTempName);
        File tmp = new File(uploadPath + "/" + fileTempName);    // 파일 생성
        try {
            FileUtils.copyToFile(inputStream, tmp);
        } catch (IOException e) {
            log.error("Error while copying.", e);
            throw e;
        }

        return fileTempName;
    }

}
