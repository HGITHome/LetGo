package com.dgut.main.dao;

import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.main.entity.ApkVersion;

/**
 * Created by PUNK on 2017/2/12.
 */
public interface ApkVersionDao {
    public Pagination getList(String queryType, String isValid, int pageNo, int pageSize) ;

    /**
     * 上传apk
     * @param bean
     * @return
     */
    public ApkVersion save(ApkVersion bean);

    /**
     * 得到最新版本的app
     * @return
     */
    public ApkVersion getLatestVersionNo();


    /**
     * 得到最近有效的app版本
     * @return
     */
    public ApkVersion getLatestValidVersion();



    public ApkVersion updateByUpdater(Updater<ApkVersion> updater);


    /**
     * 查找版本信息
     * @param id
     * @return
     */
    public ApkVersion findById(String id);


    /**
     *  删除版本
     * @param id
     * @return
     */
    public ApkVersion deleteById(String id);
}
