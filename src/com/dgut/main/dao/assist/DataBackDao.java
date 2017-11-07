package com.dgut.main.dao.assist;

import com.dgut.main.entity.assist.DBField;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by PUNK on 2017/1/20.
 */
public interface DataBackDao  {
    String getDefaultCatalog() throws SQLException;

    List<String> listTables(String catalog);

    List<DBField> listFields(String tablename);

    String createTableDDL(String tablename);

    List<Object[]> createTableData(String tablename);
}
