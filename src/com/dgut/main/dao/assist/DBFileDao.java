package com.dgut.main.dao.assist;

import com.dgut.main.entity.assist.DBFile;

import java.util.List;

/**
 * Created by PUNK on 2017/1/21.
 */
public interface DBFileDao {

    DBFile save(DBFile bean);

    List<DBFile> getList(Boolean isValid);

    void updateInvalidFiles(List<String > filenames);

    DBFile findByFileName(String fileName);
}
