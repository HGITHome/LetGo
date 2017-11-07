package com.dgut.main.manager.assist;

import com.dgut.main.entity.assist.DBField;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by PUNK on 2017/1/20.
 */
public interface DataBackMng {
    String getDefaultCatalog() throws SQLException;


    List<String> listTables(String defaultCatalog);

    List<DBField> listFields(String tablename);

    String createTableDDL(String tablename);

    List<Object[]> createTableData(String tablename);
}
