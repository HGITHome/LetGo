package com.dgut.main.dao.assist.impl;

import com.dgut.common.hibernate4.Finder;
import com.dgut.common.hibernate4.HibernateBaseDao;
import com.dgut.main.dao.assist.DBFileDao;
import com.dgut.main.entity.assist.DBFile;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by PUNK on 2017/1/21.
 */
@Repository
public class DBFileDaoImpl extends HibernateBaseDao<DBFile,String> implements DBFileDao {
    @Override
    protected Class<DBFile> getEntityClass() {
        return DBFile.class;
    }

    @Override
    public DBFile save(DBFile bean) {
        getSession().save(bean);
        return bean;
    }

    @Override
    public List<DBFile> getList(Boolean isValid) {
        Finder f = Finder.create("from DBFile bean where 1=1");
        if(isValid!=null){
            if(isValid){
                f.append(" and bean.fileIsvalid=true");
            }else{
                f.append(" and bean.fileIsvalid=false");
            }
        }
        f.append(" order by bean.id desc");
        return find(f);
    }

    @Override
    public void updateInvalidFiles(List<String> filenames) {
        String hql = "update DBFile bean set bean.fileIsvalid=false where bean.fileName in (:filenames)";
        getSession().createQuery(hql).setParameterList("filenames",filenames).executeUpdate();
    }

    @Override
    public DBFile findByFileName(String fileName) {
        return findUniqueByProperty("fileName",fileName);
    }
}
