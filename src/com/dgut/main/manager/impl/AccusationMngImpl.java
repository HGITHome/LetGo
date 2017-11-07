package com.dgut.main.manager.impl;

import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.main.dao.AccusationCategoryDao;
import com.dgut.main.dao.AccusationDao;
import com.dgut.main.dao.AccusationTypeDao;
import com.dgut.main.entity.Accusation;
import com.dgut.main.entity.AccusationCategory;
import com.dgut.main.entity.AccusationType;
import com.dgut.main.manager.AccusationMng;
import com.dgut.main.member.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by PUNK on 2017/3/21.
 */
@Service
@Transactional
public class AccusationMngImpl implements AccusationMng {

    @Autowired
    private AccusationCategoryDao categoryDao;

    @Autowired
    private AccusationTypeDao typeDao;

    @Autowired
    private AccusationDao accusationDao;

    @Override
    public Pagination getCategory(String queryName, int pageNo, int pageSize) {
        return categoryDao.getCategory(queryName,pageNo,pageSize);
    }

    @Override
    public AccusationCategory addCategory(AccusationCategory bean) {
        bean.setTypes(null);
        return categoryDao.save(bean);
    }

    @Override
    public AccusationCategory findCategoryById(int id) {
        return categoryDao.fingById(id);
    }

    @Override
    public AccusationCategory updateCategory(AccusationCategory bean) {
        Updater<AccusationCategory> updater =  new Updater<AccusationCategory>(bean);
        categoryDao.updateByUpdater(updater);
        return bean;
    }

    @Override
    public AccusationCategory findCategoryByName(String categoryName) {
        return categoryDao.findByName(categoryName);
    }

    @Override
    public AccusationCategory deleteCategory(Integer id) {
        return categoryDao.deleteCategory(id);
    }

    @Override
    public Pagination getTypeList(String categoryId,String typeName, int pageNo, int pageSize) {
        return typeDao.getList(categoryId,typeName,pageNo,pageSize);
    }

    @Override
    public AccusationType findTypeByName(String categoryId, String name) {
        List<AccusationType> list =  typeDao.findByTypeByName(categoryId,name);

        return list.size()==0? null: list.get(0);
    }

    @Override
    public AccusationType saveType(String categoryId, AccusationType type) {
        AccusationCategory category = findCategoryById(Integer.parseInt(categoryId));
        type.setCategory(category);
        category.getTypes().add(type);
        return typeDao.save(type);
    }

    @Override
    public AccusationType findTypeById(Integer id) {
        AccusationType type = typeDao.findById(id);
        return type;
    }

    @Override
    public AccusationType updateType(AccusationType type) {
        Updater<AccusationType> updater = new Updater<AccusationType>(type);
        typeDao.updateByUpdater(updater);
        return type;
    }

    @Override
    public AccusationType deleteType(String s) {

        return typeDao.deleteType(Integer.parseInt(s));
    }

    @Override
    public Accusation addAccusation(Member member, Member respondent, String content,AccusationType type) {
        Accusation accusation = new Accusation();
        accusation.setContent(content);
        accusation.setReporter(member);
        accusation.setHandleFlag(false);
        accusation.setPublishTime(new Date());
        accusation.setReply(null);
        accusation.setReplyTime(null);
        accusation.setRespondent(respondent);
        accusation.setType(type);
        accusationDao.save(accusation);
        return accusation;
    }

    @Override
    public Pagination getAccusationList(String reporter, String respondent, Integer type,String status, int pageNo, int pageSize) {
        return accusationDao.getList(reporter,respondent,type,status,pageNo,pageSize);
    }

    @Override
    public Accusation findAccusationById(int id) {
        return accusationDao.findById(id);
    }

    @Override
    public Accusation update(String id, String reply) {
        Accusation bean = findAccusationById(Integer.parseInt(id));
        bean.setReply(reply);
        bean.setReplyTime(new Date());
        bean.setHandleFlag(true);

        Updater<Accusation> updater = new Updater<Accusation>(bean);
        accusationDao.updateByUpdater(updater);
        return bean;
    }
}
