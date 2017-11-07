package com.dgut.main.manager.assist.impl;

import com.dgut.main.dao.assist.DataBackDao;
import com.dgut.main.entity.assist.DBField;
import com.dgut.main.manager.assist.DataBackMng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * 数据备份service接口
 * Created by PUNK on 2017/1/20.
 */
@Service
@Transactional
public class DataBackMngImpl implements DataBackMng {

    @Autowired
    private DataBackDao dataBackDao;

    @Override
    public String getDefaultCatalog() throws SQLException {
        return dataBackDao.getDefaultCatalog();
    }

    @Override
    public List<String> listTables(String catalog) {
        return dataBackDao.listTables(catalog);
    }

    @Override
    public List<DBField> listFields(String tablename) {
        return  dataBackDao.listFields(tablename);
    }

    @Override
    public String createTableDDL(String tablename) {
        return dataBackDao.createTableDDL(tablename);
    }

    @Override
    public List<Object[]> createTableData(String tablename) {
        return dataBackDao.createTableData(tablename);
    }
}
