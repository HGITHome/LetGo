package com.dgut.main.manager.assist.impl;

import com.dgut.common.web.springmvc.RealPathResolver;
import com.dgut.main.entity.assist.DBFile;
import com.dgut.main.manager.assist.DBFileMng;
import com.dgut.main.manager.assist.DBResourceMng;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

/**
 * Created by PUNK on 2017/1/21.
 */
@Service
@Transactional
public class DBResourceMngImpl implements DBResourceMng {

    @Autowired
    private DBFileMng flieMng;

    @Override
    public int delete(String[] names) {
        int count = 0;
        File f;
        for (String name : names) {
            DBFile file =flieMng.findByFileName(name);
            f = new File(realPathResolver.get(file.getFilePath()));
            if (FileUtils.deleteQuietly(f)) {
                count++;
            }
        }
        return count;
    }


    @Autowired
    private RealPathResolver realPathResolver;
}
