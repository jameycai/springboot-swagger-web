package com.ultrapower.sbdemo.webapp.home.service;

import java.util.List;

import com.ultrapower.sbdemo.webapp.home.model.UserBean;

/**
 * 
 * @ClassName:     用户的业务逻辑接口类 
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2017年12月15日 上午10:53:29 
 *
 */
public interface IUserService {

	/**
	 * 插入用户
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public int insertUser(UserBean user);
	
	
	/**
	 * 修改用戶
	 * @param user
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public int updateUser(UserBean user, long userId);
	
	
	/**
     * 刪除用戶
     * @param userId
     * @return
     * @throws Exception
     */
	public int deleteUser(long userId);
	
	
	/**
	 * 根据ID，查询用户
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public UserBean getUserInfoById(long userId);
	
	
	/**
	 * 根据用户账号，查询用户
	 * @param userAccount
	 * @return
	 * @throws Exception
	 */
	public UserBean getUserInfoByAccount(String userAccount);
	
	
	/**
     * 查询所有的用户信息
     * @return
     * @throws Exception
     */
	public List<UserBean> getAllUser();
	
    /**
     * 
     * @Title:getUsersByPager
     * @Description: 分页查询数据 
     * @param userAccount
     * @param userName
     * @param fromIndex
     * @param toIndex
     * @return
     * @author caijinpeng
     */
	public List<UserBean> getUsersByPager(String userAccount, String userName, int fromIndex, int toIndex);
	
	/**
	 * @Title: getUserCount
	 * @Description: 分页统计数据
	 * @param userAccount
	 * @param userName
	 * @return
	 * @author caijinpeng
	 */
	public int getUserCount(String userAccount, String userName);
	
}
