package com.dgut.main.manager.assist.impl;

import com.dgut.common.web.session.SessionProvider;
import com.dgut.main.dao.assist.DBFileDao;
import com.dgut.main.entity.assist.DBFile;
import com.dgut.main.manager.assist.DBFileMng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by PUNK on 2017/1/21.
 */
@Service
@Transactional
public class DBFileMngImpl implements DBFileMng {
    @Override
    public void saveFileByPath(String path, String name, boolean isValid) {
        DBFile file = new DBFile();
        file.setFilePath(path);
        file.setFileName(name);
        file.setFileIsvalid(isValid);
        save(file);
    }

    @Override
    public DBFile save(DBFile bean) {
        return dao.save(bean);
    }

    @Override
    public List<DBFile> getList(Boolean isValid) {
        return dao.getList(isValid);

    }

    @Override
    public void updateInvalidFiles(List<String> filenames) {

        dao.updateInvalidFiles(filenames);
    }

    @Override
    public DBFile findByFileName(String fileName) {
        return dao.findByFileName(fileName);
    }


    @Autowired
    private DBFileDao dao;

    @Autowired
    private SessionProvider sessionProvider;


}
