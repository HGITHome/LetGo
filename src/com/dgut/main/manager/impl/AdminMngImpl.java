package com.dgut.main.manager.impl;

import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.common.security.BadCredentialsException;
import com.dgut.common.security.UsernameNotFoundException;
import com.dgut.common.security.encoder.PwdEncoder;
import com.dgut.core.manager.ConfigMng;
import com.dgut.main.dao.AdminDao;
import com.dgut.main.dao.RoleDao;
import com.dgut.main.entity.Admin;
import com.dgut.main.entity.Role;
import com.dgut.main.manager.AdminMng;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by PUNK on 2017/1/15.
 */
@Service
@Transactional
public class AdminMngImpl implements AdminMng {



    /**
     * admin 登陆
     *
     * @param username
     * @param password
     * @return
     * @throws UsernameNotFoundException
     * @throws BadCredentialsException
     */
    @Override
    public Admin login(String username, String password) throws UsernameNotFoundException, BadCredentialsException {
        Admin admin = dao.findByUsername(username);
        if (null == admin) {
            throw new UsernameNotFoundException("不存在用户: "
                    + username);
        }
        if (!(pwdEncoder.isPasswordValid(admin.getPassword(), password))) {
            throw new BadCredentialsException("用户名和密码不一致");
        }
        return admin;
    }

    /**
     * 根据查询条件检索管理员
     *
     * @param username
     * @param roleId
     * @param disabled
     * @param isSuper
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Pagination getPage(String username, Integer roleId, Boolean disabled, Boolean isSuper, int pageNo, int pageSize) {
        return dao.getPage(username, roleId, disabled, isSuper, pageNo, pageSize);
    }

    /**
     * 主键查找
     *
     * @param uid
     * @return
     */
    @Override
    public Admin findById(Integer uid) {
        Admin admin = dao.findById(uid);
        return admin;
    }

    /**
     * 用户名查找
     *
     * @param username
     * @return
     */
    @Override
    public Admin findByUsername(String username) {
        return dao.findByUsername(username);
    }

    /**
     * 查询用户密码输入错误的次数
     *
     * @param username
     * @return
     */
    @Override
    public Integer errorTimes(String username) {
        if (StringUtils.isBlank(username)) {
            return 0;
        }
        Admin admin = findByUsername(username);
        if (admin == null) {
            return 0;
        }
        return admin.getErrorCount();
    }

    /*@Override
    public Integer errorRemaining(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        Admin user = findByUsername(username);
        if (user == null) {
            return null;
        }
        long now = System.currentTimeMillis();
        Config.ConfigLogin configLogin = configMng.getConfigLogin();
        //得到系统配置的密码错误次数和输入间隔
        int maxErrorTimes = configLogin.getErrorTimes();
        int maxErrorInterval = configLogin.getErrorInterval() * 60 * 1000;
        Integer errorCount = user.getErrorCount();
        Date errorTime = user.getErrorTime();
        if (errorCount <= 0 || errorTime == null
                || errorTime.getTime() + maxErrorInterval < now) {
            return maxErrorTimes;
        }
        return maxErrorTimes - errorCount;
    }*/

    /**
     * 登陆成功更新用户的上线时间
     *
     * @param userId
     * @param ip
     */
    @Override
    public void updateLoginSuccessInfo(Integer userId, String ip) {
        Date now = new Timestamp(System.currentTimeMillis());
        Admin admin = findById(userId);
        if (admin != null) {
            admin.setLoginCount(admin.getLoginCount() + 1);
            admin.setLastLoginIP(ip);
            admin.setLastLoginTime(now);
            admin.setErrorCount(0);
            admin.setErrorTime(null);
        }
    }

    /**
     * 密码输入错误时更新用户的错误次数
     *
     * @param username
     * @param ip
     * @param maxErrorTimes
     */
    @Override
    public void updateLoginFailureInfo(String username, String ip, Integer maxErrorTimes) {
        Date now = new Timestamp(System.currentTimeMillis());
        Admin admin = findByUsername(username);
        if (admin != null) {
            //若次数达到5时，锁定该账号，等到错误间隔才可再次输入密码
            if (admin.getErrorCount().equals(5)) {
                admin.setDisabled(true);
            } else {
                admin.setErrorCount(admin.getErrorCount() + 1);
                admin.setErrorTime(now);
            }


        }
    }

    /**
     * 个人资料页修改个人密码和姓名
     *
     * @param id
     * @param newPwd
     * @param realName
     */
    @Override
    public void updatePwdRealName(Integer id, String newPwd, String realName) {
        Admin admin = findById(id);
        admin.setRealname(realName);
        if (!StringUtils.isBlank(newPwd)) {

            admin.setPassword(pwdEncoder.encodePassword(newPwd));
        }
    }

    /**
     * 检查输入密码的密码是否和原密码一致
     *
     * @param id
     * @param password
     * @return
     */
    public boolean isPasswordValid(Integer id, String password) {
        Admin admin = findById(id);
        return pwdEncoder.isPasswordValid(admin.getPassword(), password);
    }

    @Override
    public Admin save(String username, String password, String ip, Integer rank, Boolean gender, String realname, Integer roleId) {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(pwdEncoder.encodePassword(password));
        admin.setDisabled(false);
        admin.setRegisterIP(ip);
        admin.setRank(rank);
        admin.setGender(gender);
        admin.setRealname(realname);
        admin.setRegisterTime(new Date());
        admin.setLoginCount(0);

        Role role =roleDao.findById(roleId);
        admin.setRole(role);
        save(admin);
        return admin;
    }

    @Override
    public Admin save(Admin bean) {
        return dao.save(bean);
    }

    @Override
    public Admin updateAdmin(Admin bean, Integer roleId) {
        Admin admin = findById(bean.getId());
        if(StringUtils.isNotBlank(bean.getPassword())){
            bean.setPassword(pwdEncoder.encodePassword(bean.getPassword()));
        }
        else{
            bean.setPassword(admin.getPassword());
        }
        Role role = roleDao.findById(roleId);
        bean.setRole(role);

        Updater<Admin> updater =new Updater<Admin>(bean);
        bean = dao.updateByUpdater(updater);
        return bean;
    }

    @Autowired
    private AdminDao dao;

    @Autowired
    private PwdEncoder pwdEncoder;

    @Autowired
    private ConfigMng configMng;

    @Autowired
    private RoleDao roleDao;

}
