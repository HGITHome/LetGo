package com.dgut.main.manager;

import com.dgut.common.page.Pagination;
import com.dgut.main.entity.ApkVersion;

/**
 * Created by PUNK on 2017/2/12.
 */
public interface ApkVersionMng {

    Pagination getList(String queryType, String isValid, int pageNo, int pageSize);

    /**
     * 根据id查找版本信息
     * @param id
     * @return
     */
    public ApkVersion findById(String id);

    /**
     * 上传apk
     *
     * @param version
     * @param url
     * @param note
     * @param type
     * @return
     */
    public ApkVersion save(String version, String url, String type, String isValid , String note);

    /**
     * 更新版本信息
     * @param bean
     * @return
     */
    public ApkVersion updateVersion(ApkVersion bean);

    /**
     * 删除版本
     * @param id
     * @return
     */
    public ApkVersion deleteById(String id);
}
