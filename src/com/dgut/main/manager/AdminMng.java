package com.dgut.main.manager;

import com.dgut.common.page.Pagination;
import com.dgut.common.security.BadCredentialsException;
import com.dgut.common.security.UsernameNotFoundException;
import com.dgut.main.entity.Admin;

public interface AdminMng {
	
	public Admin login(String username, String password) throws UsernameNotFoundException, BadCredentialsException;

	public Pagination getPage(String username, Integer roleId,
							  Boolean disabled, Boolean isSuper,
							  int pageNo, int pageSize);

	public Admin findById(Integer uid);

	public Admin findByUsername(String username);


//	Integer errorRemaining(String username);

	Integer errorTimes(String username);

	public void updateLoginSuccessInfo(Integer userId, String ip);

	public void updateLoginFailureInfo(String username,String ip,Integer count);

	void updatePwdRealName(Integer id, String newPwd, String realName);

	public boolean isPasswordValid(Integer id, String password) ;

	Admin save(String username, String password, String ip, Integer rank, Boolean gender, String realname, Integer roleId);

	Admin save(Admin bean);

	Admin updateAdmin(Admin bean, Integer roleId);
}