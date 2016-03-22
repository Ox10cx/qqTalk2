package gx.com.qqtalk2.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/1/24.
 */
public class Talkbean {
    //说说的内容
    private String  content;
    //说说的用户名
    private String userName;
    //说说当前的时间
    private String currentTime;
    //发说说的地址
    private String address;
    //说说中是否含有图片
    private List<PictureBean> picitems=new ArrayList<>();

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public List<PictureBean> getPicitems() {
        return picitems;
    }

    public void setPicitems(List<PictureBean> picitems) {
        this.picitems = picitems;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
