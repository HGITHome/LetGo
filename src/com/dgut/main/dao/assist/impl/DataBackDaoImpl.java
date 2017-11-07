package com.dgut.main.dao.assist.impl;


import com.dgut.main.dao.assist.DataBackDao;
import com.dgut.main.entity.assist.DBField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PUNK on 2017/1/20.
 */
@Repository
public class DataBackDaoImpl extends JdbcDaoSupport implements DataBackDao {

    @Autowired
    private DataSource dataSource;

    @Override
    protected JdbcTemplate createJdbcTemplate(DataSource dataSource) {
        return super.createJdbcTemplate(dataSource);
    }

    @Override
    public String getDefaultCatalog() throws SQLException {
        return getJdbcTemplate().getDataSource().getConnection().getCatalog();
    }

    @Override
    public List<String> listTables(String catalog) {
        String sql = " SELECT TABLE_NAME FROM information_schema.`TABLES` WHERE TABLE_SCHEMA='"
                + catalog + "' ";
        List<String> tables = new ArrayList<String>();
        SqlRowSet set = getJdbcTemplate().queryForRowSet(sql);
        while (set.next()) {
            tables.add(set.getString(1));
        }
        return tables;
    }

    @Override
    public List<DBField> listFields(String tablename) {
        String sql = " desc  " + tablename;
        List<DBField> fields = new ArrayList<DBField>();
        SqlRowSet set = getJdbcTemplate().queryForRowSet(sql);
        while (set.next()) {
            DBField field = new DBField();
            field.setName(set.getString(1));
            field.setFieldType(set.getString(2));
            field.setNullable(set.getString(3));
            field.setFieldProperty(set.getString(4));
            field.setFieldDefault(set.getString(5));
            field.setExtra(set.getString(6));
            fields.add(field);
        }
        return fields;
    }

    @Override
    public String createTableDDL(String tablename) {
        String sql = " show create table " + tablename;
        String ddl = getJdbcTemplate().queryForObject(sql,
                new RowMapper<String>() {
                    public String mapRow(ResultSet set, int arg1)
                            throws SQLException {
                        return set.getString(2);
                    }
                });
        return ddl;
    }

    @Override
    public List<Object[]> createTableData(String tablename) {
        int filedNum = getTableFieldNums(tablename);
        List<Object[]> results = new ArrayList<Object[]>();
        String sql = " select * from   " + tablename;
        SqlRowSet set = getJdbcTemplate().queryForRowSet(sql);
        while (set.next()) {
            Object[] oneResult = new Object[filedNum];
            for (int i = 1; i <= filedNum; i++) {
                oneResult[i - 1] = set.getObject(i);
            }
            results.add(oneResult);
        }
        return results;
    }

    private int getTableFieldNums(String tablename) {
        String sql = " desc  " + tablename;
        SqlRowSet set = getJdbcTemplate().queryForRowSet(sql);
        int rownum = 0;
        while (set.next()) {
            rownum++;
        }
        return rownum;
    }
}
