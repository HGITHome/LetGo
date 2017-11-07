package com.dgut.main.entity.assist;

import com.dgut.main.entity.assist.base.BaseDBFile;

/**
 * Created by PUNK on 2017/1/20.
 */
public class DBFile extends BaseDBFile{

    public DBFile() {
    }

    public DBFile(String filePath, String fileName, boolean fileIsvalid) {
        super(filePath, fileName, fileIsvalid);
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof DBFile)) return false;
        else {
            DBFile dbFile = (DBFile) obj;
            if (null == this.getFilePath() || null == dbFile.getFilePath()) return false;
            else return (this.getFilePath().equals(dbFile.getFilePath()));
        }
    }
}
