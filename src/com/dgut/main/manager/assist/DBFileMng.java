package com.dgut.main.manager.assist;

import com.dgut.main.entity.assist.DBFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by PUNK on 2017/1/21.
 */

public interface DBFileMng {
    void saveFileByPath(String path, String name, boolean isVaild);

    public DBFile save(DBFile bean);

    List<DBFile> getList(Boolean isValid);

    void updateInvalidFiles(List<String> fileNames);

    DBFile findByFileName(String fileName);
}
