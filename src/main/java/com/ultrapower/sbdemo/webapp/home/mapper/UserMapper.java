package com.ultrapower.sbdemo.webapp.home.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ultrapower.sbdemo.webapp.home.model.UserBean;

/**
 * 
 * @ClassName:     用户的Mapper的接口类 
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2017年12月15日 上午10:52:19 
 *
 */
@Mapper
public interface UserMapper {

	/**
	 * 新增
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Insert("insert into security_rest_user values (#{user.userId},#{user.userAccount},#{user.userName},"
			+ "#{user.password},#{user.dept_id},#{user.business},#{user.user_status})")
	public int insertUser(@Param("user")UserBean user) throws Exception; 
	
	
	/**
	 * 修改用戶
	 * @param user
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Update(" update security_rest_user set account=#{u.userAccount},user_name=#{u.userName},pass=#{u.password},"
			+ " dept_id=#{u.dept_id},business=#{u.business},user_status=#{u.user_status}"
			+ " where user_id=#{userId}")
    public int updateUser (@Param("u")UserBean u,@Param("userId")long userId) throws Exception;
	
	
    /**
     * 刪除用戶
     * @param userId
     * @return
     * @throws Exception
     */
	@Delete(" delete from security_rest_user where user_id=#{userId} ")
   public int deleteUser(long userId) throws Exception;
	
	/**
	 * 根据ID，查询用户
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Select(" select * from security_rest_user where user_id = #{userId} ")
	@Results({	
		@Result(id=true,property="userId",column="user_id",javaType=Long.class),
		@Result(property="userAccount",column="account",javaType=String.class),
		@Result(property="userName",column="user_name",javaType=String.class),
		@Result(property="password",column="pass",javaType=String.class),
		@Result(property="dept_id",column="dept_id",javaType=String.class),
		@Result(property="business",column="business",javaType=String.class),
		@Result(property="user_status",column="user_status",javaType=Integer.class)
	})
	public UserBean getUserInfoById(long userId);
	
	
	/**
	 * 根据用户账号，查询用户
	 * @param userAccount
	 * @return
	 * @throws Exception
	 */
	@Select(" select * from security_rest_user where account = #{userAccount} ")
	@Results({	
		@Result(id=true,property="userId",column="user_id",javaType=Long.class),
		@Result(property="userAccount",column="account",javaType=String.class),
		@Result(property="userName",column="user_name",javaType=String.class),
		@Result(property="password",column="pass",javaType=String.class),
		@Result(property="dept_id",column="dept_id",javaType=String.class),
		@Result(property="business",column="business",javaType=String.class),
		@Result(property="user_status",column="user_status",javaType=Integer.class)
	})
	public UserBean getUserInfoByAccount(String userAccount);
	
	/**
     * 查询所有的用户信息
     * @return
     * @throws Exception
     */
	@Select(" select * from security_rest_user")
	@ResultMap("userMap")
    public List<UserBean> getAllUser() throws Exception;
	
	
   /**
    * 分页查询数据
    * @param parma
    * @return
    * @throws Exception
    */
	public List<UserBean> getUsersByPager(Map<String, Object> parmas) throws Exception;
	   
   /**
    * 
    * 分页统计数据
    * @param parma
    * @return
    * @throws Exception
    */
    public int getUserCount(Map<String, Object> parmas) throws Exception;
	    
}
