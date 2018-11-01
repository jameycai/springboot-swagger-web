package com.ultrapower.sbdemo.webapp.home.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ultrapower.sbdemo.common.ConvertUtil;
import com.ultrapower.sbdemo.webapp.home.mapper.UserMapper;
import com.ultrapower.sbdemo.webapp.home.model.UserBean;
import com.ultrapower.sbdemo.webapp.home.service.IUserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * 
 * @ClassName:     用户的业务逻辑接口实现类 
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2017年12月15日 上午10:54:15 
 *
 */
@Service
public class UserServiceImpl implements IUserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Resource
	private UserMapper userMapper;
	
	/**
	 * 插入用户
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public int insertUser(UserBean user) {
		int result = 0;
		try{
			if(null==user) {
				return result;
			}
			
			user.setUserAccount(ConvertUtil.Obj2Str(user.getUserAccount(),""));
			user.setUserName(ConvertUtil.Obj2Str(user.getUserName(),""));
			user.setPassword(ConvertUtil.Obj2Str(user.getPassword(),""));
			user.setDept_id(ConvertUtil.Obj2Str(user.getDept_id(),""));
			user.setDept_Name(ConvertUtil.Obj2Str(user.getDept_Name(),""));
			user.setBusiness(ConvertUtil.Obj2Str(user.getBusiness(),""));
			
			return userMapper.insertUser(user);
		}catch(Exception ex){
			logger.debug("insertUser error! ", ex);
			ex.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 修改用戶
	 * @param user
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public int updateUser(UserBean user, long userId){
		int result = 0;
		try{
			if(null==user || userId==0) {
				return result;
			}
			
			user.setUserAccount(ConvertUtil.Obj2Str(user.getUserAccount(),""));
			user.setUserName(ConvertUtil.Obj2Str(user.getUserName(),""));
			user.setPassword(ConvertUtil.Obj2Str(user.getPassword(),""));
			user.setDept_id(ConvertUtil.Obj2Str(user.getDept_id(),""));
			user.setDept_Name(ConvertUtil.Obj2Str(user.getDept_Name(),""));
			user.setBusiness(ConvertUtil.Obj2Str(user.getBusiness(),""));
			
			UserBean orguser = getUserInfoById(userId);
			if(null!=orguser && orguser.getUserId()>0){
				return userMapper.updateUser(user, userId);
			}else{
				return userMapper.insertUser(user);
			}
		}catch(Exception ex){
			logger.debug("updateUser error! ", ex);
			ex.printStackTrace();
		}
		return result;
	}


    /**
     * 刪除用戶
     * @param userId
     * @return
     * @throws Exception
     */
	@Override
	public int deleteUser(long userId){
		int result = 0;
		try{
			UserBean orguser = getUserInfoById(userId);
			if(null!=orguser && orguser.getUserId()>0){
				return userMapper.deleteUser(userId);
			}
		}catch(Exception ex){
			logger.debug("updateUser error! ", ex);
			ex.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据ID，查询用户
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public UserBean getUserInfoById(long userId){
		try{
			UserBean user = userMapper.getUserInfoById(userId);
			if(null!=user && user.getUserId()>0){
				user.setUserAccount(ConvertUtil.Obj2Str(user.getUserAccount(),""));
				user.setUserName(ConvertUtil.Obj2Str(user.getUserName(),""));
				user.setPassword(ConvertUtil.Obj2Str(user.getPassword(),""));
				user.setDept_id(ConvertUtil.Obj2Str(user.getDept_id(),""));
				user.setDept_Name(ConvertUtil.Obj2Str(user.getDept_Name(),""));
				user.setBusiness(ConvertUtil.Obj2Str(user.getBusiness(),""));
			}
			return user;
		}catch(Exception ex){
			logger.debug("getUserInfoById error! request userId:"+userId, ex);
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据用户账号，查询用户
	 * @param userAccount
	 * @return
	 * @throws Exception
	 */
	@Override
	public UserBean getUserInfoByAccount(String userAccount) {
		try{
			UserBean user = userMapper.getUserInfoByAccount(userAccount);
			if(null!=user && user.getUserId()>0){
				user.setUserAccount(ConvertUtil.Obj2Str(user.getUserAccount(),""));
				user.setUserName(ConvertUtil.Obj2Str(user.getUserName(),""));
				user.setPassword(ConvertUtil.Obj2Str(user.getPassword(),""));
				user.setDept_id(ConvertUtil.Obj2Str(user.getDept_id(),""));
				user.setDept_Name(ConvertUtil.Obj2Str(user.getDept_Name(),""));
				user.setBusiness(ConvertUtil.Obj2Str(user.getBusiness(),""));
			}
			return user;
		}catch(Exception ex){
			logger.debug("getUserInfoByAccount error! request userAccount:"+userAccount, ex);
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
     * 查询所有的用户信息
     * @return
     * @throws Exception
     */
	@Override
	public List<UserBean> getAllUser(){
		try{
			return userMapper.getAllUser();
		}catch(Exception ex){
			logger.debug("getAllUser error! ", ex);
			ex.printStackTrace();
		}
		return null;
	}
	
   /**
    * 分页查询数据
    * @param parma
    * @return
    * @throws Exception
    */
	@Override
	public List<UserBean> getUsersByPager(String userAccount, String userName, int fromIndex, int toIndex){
		try {
			Map<String, Object> parmas = new HashMap<String, Object>();
			parmas.put("dbType", ConvertUtil.Obj2Str(System.getProperty("jdbc.dbType"),"mysql"));
			parmas.put("fromIndex", fromIndex);
			parmas.put("toIndex", toIndex);
			
			if(null!=userAccount && userAccount.trim().length()>0) {
				parmas.put("userAccount", userAccount.toLowerCase());
			}else {
				parmas.put("userAccount", null);
			}
			if(null!=userName && userName.trim().length()>0) {
				parmas.put("userName", userName.toLowerCase());
			}else {
				parmas.put("userName", null);
			}

			
			return userMapper.getUsersByPager(parmas);
		}catch(Exception ex) {
			logger.debug("getUsersByPager error! ", ex);
			ex.printStackTrace();
		}
		return null;
	}
	
   /**
    * 
    * 分页统计数据
    * @param parma
    * @return
    * @throws Exception
    */
	@Override
	public int getUserCount(String userAccount, String userName) {
		try {
			Map<String, Object> parmas = new HashMap<String, Object>();
			parmas.put("dbType", ConvertUtil.Obj2Str(System.getProperty("jdbc.dbType"),"mysql"));
			
			if(null!=userAccount && userAccount.trim().length()>0) {
				parmas.put("userAccount", userAccount.toLowerCase());
			}else {
				parmas.put("userAccount", null);
			}
			if(null!=userName && userName.trim().length()>0) {
				parmas.put("userName", userName.toLowerCase());
			}else {
				parmas.put("userName", null);
			}

			
			return userMapper.getUserCount(parmas);
		}catch(Exception ex) {
			logger.debug("getUserCount error! ", ex);
			ex.printStackTrace();
		}
		return 0;
	}
	
}
