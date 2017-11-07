package com.dgut.main.entity.assist.base;

import java.io.Serializable;

/**
 * 数据库备份实体
 * Created by PUNK on 2017/1/20.
 */
public class BaseDBFile  implements Serializable{

    private int hashCode = Integer.MIN_VALUE;

    // primary key
    private java.lang.String filePath;  //文件保存路径

    // fields
    private java.lang.String fileName;  //文件名
    private boolean fileIsvalid;        //文件的有效性

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isFileIsvalid() {
        return fileIsvalid;
    }

    public void setFileIsvalid(boolean fileIsvalid) {
        this.fileIsvalid = fileIsvalid;
    }

    public int hashCode () {
        if (Integer.MIN_VALUE == this.hashCode) {
            if (null == this.getFilePath()) return super.hashCode();
            else {
                String hashStr = this.getClass().getName() + ":" + this.getFilePath().hashCode();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }

    public BaseDBFile(String filePath, String fileName, boolean fileIsvalid) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileIsvalid = fileIsvalid;
    }

    public BaseDBFile() {
    }
}
