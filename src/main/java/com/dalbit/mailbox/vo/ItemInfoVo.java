package com.dalbit.mailbox.vo;

import com.dalbit.mailbox.vo.procedure.P_MailBoxListVo;
import com.dalbit.mailbox.vo.procedure.P_MailBoxMsgListVo;
import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter @Setter
public class ItemInfoVo {
    public ItemInfoVo() {}
    public ItemInfoVo(P_MailBoxListVo target){
        setItemNo(target.getAddData1());
        setItemCnt(DalbitUtil.isEmpty(target.getAddData2()) ? 0 :Integer.parseInt(target.getAddData2()));
        setItemType(target.getAddData3());
        setItemNm(target.getAddData4());
        setItemImgUrl(target.getAddData5());
        setDalCnt(DalbitUtil.isEmpty(target.getAddData6()) ? 0 : Integer.parseInt(target.getAddData6()));
        setWebpUrl(target.getAddData7());
        setLottieUrl(target.getAddData8());
        setSoundFileUrl(target.getAddData9());
    }

    public ItemInfoVo(P_MailBoxMsgListVo target){
        if(target.getAddData4() != null && target.getAddData4().startsWith("{")){
            HashMap itemMap = new Gson().fromJson(target.getAddData4(), HashMap.class);
            setItemVoByApi(itemMap);
        } else {
            setItemNo(target.getAddData1());
            setItemCnt(DalbitUtil.isEmpty(target.getAddData2()) ? 0 :Integer.parseInt(target.getAddData2()));
            setItemType(target.getAddData3());
            setItemNm(target.getAddData4());
            setItemImgUrl(target.getAddData5());
            setDalCnt(DalbitUtil.isEmpty(target.getAddData6()) ? 0 : Integer.parseInt(target.getAddData6()));
            setWebpUrl(target.getAddData7());
            setLottieUrl(target.getAddData8());
            setSoundFileUrl(target.getAddData9());
        }
    }

    public ItemInfoVo(HashMap resultMap){
        HashMap itemMap = new Gson().fromJson(DalbitUtil.getStringMap(resultMap, "addData4"), HashMap.class);
        setItemVoByApi(itemMap);
     }

    public void setItemVoByApi(HashMap itemMap){
        setItemNo(DalbitUtil.getStringMap(itemMap, "item_code"));
        setItemCnt(DalbitUtil.getIntMap(itemMap, "item_cnt"));
        setItemType(DalbitUtil.getStringMap(itemMap, "item_type"));
        setItemNm(DalbitUtil.getStringMap(itemMap, "item_name"));
        setItemImgUrl(DalbitUtil.getStringMap(itemMap, "item_thumbnail"));
        setDalCnt(DalbitUtil.getIntMap(itemMap, "item_price"));
        setWebpUrl(DalbitUtil.getStringMap(itemMap, "webp_image"));
        setLottieUrl(DalbitUtil.getStringMap(itemMap, "jason_image"));
        setSoundFileUrl(DalbitUtil.getStringMap(itemMap, "sound_url"));
        setDuration(DalbitUtil.getIntMap(itemMap, "play_time"));

        if(DalbitUtil.getIntMap(itemMap, "use_area") == 2){
            setWidth(108);
            setHeight(108);
            setDeviceRate(1);
            setWidthRate(1);
            setHeightRate(1);
            setLocation("midLeft");
        }else if(DalbitUtil.getIntMap(itemMap, "use_area") == 3){
            setWidth(720);
            setHeight(504);
            setDeviceRate(1);
            setWidthRate(1);
            setHeightRate(0.7);
            setLocation("topLeft");
        }else if(DalbitUtil.getIntMap(itemMap, "use_area") == 5) {
            setWidth(720);
            setHeight(1080);
            setDeviceRate(1);
            setWidthRate(1);
            setHeightRate(1.5);
            setLocation("topLeft");
        }else{
            setWidth(360);
            setHeight(900);
            setDeviceRate(0.5);
            setWidthRate(1);
            setHeightRate(2.5);
            setLocation("bottomRight");
        }

        if(DalbitUtil.getIntMap(itemMap, "use_area") == 1) {
            setCategory("emoticon");
        }else if(DalbitUtil.getIntMap(itemMap, "use_area") == 2){
            setCategory("combo");
        }else{
            setCategory("normal");
        }

        if("G1827".equals(DalbitUtil.getStringMap(itemMap, "item_code")) || DalbitUtil.isEmpty(DalbitUtil.getStringMap(itemMap, "jason_image"))){
            setIosSelectType("webp");
        }else{
            setIosSelectType("lottie");
        }
    }
    private String itemNo;                  //addData1 (image: path, gift: itemCode)
    private int itemCnt;                    //addData2 (gift: itemCnt)
    private String itemType;                //addData3 (gift: itemType)
    private String itemNm;                  //addData4 (gift: itemNm)
    private String itemImgUrl;              //addData5 (gift: itemImgUrl)
    private int dalCnt;                     //addData6 (gift: dalCnt)
    private String webpUrl;                 //addData7 (gift: webpUrl)
    private String lottieUrl;               //addData8 (gift: lottieUrl)
    private String soundFileUrl;            //addData9 (gift: soundFileUrl)
    private Boolean isSecret = false;
    private int width;
    private int height;
    private double deviceRate;
    private int widthRate;
    private double heightRate;
    private String location;
    private int duration;
    private String iosSelectType;
    private String category;

}
