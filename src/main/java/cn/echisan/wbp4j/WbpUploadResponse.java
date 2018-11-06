package cn.echisan.wbp4j;

import cn.echisan.wbp4j.Entity.ImageInfo;

/**
 * Created by echisan on 2018/11/5
 */
public class WbpUploadResponse implements UploadResponse {
    private ResultStatus resultStatus;
    private String message;
    private ImageInfo imageInfo;

    @Override
    public void setResult(ResultStatus rs) {
        this.resultStatus = rs;
    }

    @Override
    public ResultStatus getResult() {
        return this.resultStatus;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public ImageInfo getImageInfo() {
        return this.imageInfo;
    }

    @Override
    public void setImageInfo(ImageInfo imageInfo) {
        this.imageInfo = imageInfo;
    }
}
