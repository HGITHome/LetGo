package com.dgut.main.manager.impl;

import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.main.dao.ApkVersionDao;
import com.dgut.main.entity.ApkVersion;
import com.dgut.main.manager.ApkVersionMng;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by PUNK on 2017/2/12.
 */
@Service
@Transactional
public class ApkVersionMngImpl implements ApkVersionMng {

    @Autowired
    private ApkVersionDao dao;

    @Override
    public Pagination getList(String queryType, String isValid, int pageNo, int pageSize) {
        return dao.getList(queryType,isValid,pageNo,pageSize);
    }

    @Override
    public ApkVersion save(String version,String url, String type, String isValid ,String note) {
//		String new_version_id=null;
//		ApkVersion old = dao.findById(version);
//		if(old==null){
//			new_version_id = getVersionId(null, type);
//		}
//		else{
//			new_version_id = getVersionId(old.getVersion_id(), type);
//		}

        ApkVersion bean = new ApkVersion();
        bean.setIsValid(true);
        bean.setNote(note);
        bean.setUrl(url);
        bean.setType(type);
        bean.setPublish_time(new Date());
        bean.setVersion_id(version);
        bean.setIsValid(Boolean.valueOf(isValid));


        return dao.save(bean);
    }

    /**
     * 根据GNU策略 修订类型生成版本号
     * DNU（主版+子版+修正版+编译版）
     * 1）局部修改或bug修正，则修正版加一；
     * 2）添加功能，子版加一；
     * 3）重大修改或局部修改过多，主版加一
     * @param id
     * @param type
     * @return
     */
    private String getVersionId(String id, String type) {
        Integer random_No = 10 + (int) (Math.random() * 90);
        if (StringUtils.isBlank(id)) {
            return "1.0.0." + random_No;
        }

        String[] ids = id.split("\\.");
        String main = ids[0];
        String child = ids[1];
        String modify = ids[2];

        StringBuffer version_id = new StringBuffer();
        //重大修改或局部修改过多
        if (type.equals("1")) {
            version_id.append((Integer.parseInt(main) + 1) + ".0" + ".0." + random_No);
        }
        //添加功能
        else if (type.equals("2")) {
            version_id.append(main + "." + (Integer.parseInt(child) + 1) + ".0" + random_No);
        }
        //局部修改或bug订正
        else if (type.equals("3")) {
            version_id.append(main + "." + child + "." + (Integer.parseInt(modify) + 1) + "." + random_No);
        }

        return version_id.toString();
    }

    @Override
    public ApkVersion updateVersion(ApkVersion bean) {
        Updater<ApkVersion> updater = new Updater<ApkVersion>(bean);
        bean =dao.updateByUpdater(updater);
        return bean;
    }

    @Override
    public ApkVersion findById(String id) {

        return dao.findById(id);
    }

    @Override
    public ApkVersion deleteById(String id) {
        ApkVersion version =dao.deleteById(id);
        return version;
    }
}
