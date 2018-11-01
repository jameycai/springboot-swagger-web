package com.ultrapower.sbdemo.webapp.home.controller.swagger;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ultrapower.sbdemo.common.apiver.ApiVersion;
import com.ultrapower.sbdemo.common.utils.JsonResultBean;
import com.ultrapower.sbdemo.webapp.home.model.UserBean;
import com.ultrapower.sbdemo.webapp.home.service.IUserService;

import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

/**
 * 
 * @ClassName:     用户restful接口方法层
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2017年12月15日 上午11:14:11 
 *
 */
@Api(tags="用户相关接口")
@RequestMapping(value="/api/{version}/users")
@RestController
public class UserSwagger {

	private static final Logger logger = LoggerFactory.getLogger(UserSwagger.class);  
		 
	
	/*** ############# Restful中methodl类型  ############# *****/
	/**
	 *  1. 新增：method=RequestMethod.POST
	 *  2. 修改：method=RequestMethod.PUT
	 *  3. 删除：method=RequestMethod.DELETE
	 *  4. 查询：method = RequestMethod.GET
	 */
	/*** ############################################## *****/
	
	
	@Resource
	private IUserService userServiceImpl;

    
	/**
	 * restful接口生成token
	 * @param user
	 * @return
	 */
	 @ApiVersion(1.0)
	 @ApiOperation(value="restful接口生成token", notes="该方法是用户信息，生成token")
	 @ApiImplicitParams({
	        @ApiImplicitParam(name = "username", value = "用户账号", required = true, dataType = "String", paramType="query"),
	        @ApiImplicitParam(name = "password", value = "用户密码", required = true, dataType = "String", paramType="query")
	 })
	 @ApiResponses({ 
		   @ApiResponse(code = 500, message = "接口异常"), 
		   @ApiResponse(code = 403, message = "没有权限")
	  })
	 @RequestMapping(value="/sign", method=RequestMethod.POST)
     public void sign(@RequestParam(value="username",required=true) String username,
			@RequestParam(value="password",required=true) String password) {
        UserBean u = userServiceImpl.getUserInfoByAccount(username);
        if(null==u) {
        	logger.info("user is null, request username: "+ username);
        }
        
    }
    
    
	/**
	 * 新增用户
	 * @param user
	 * @return
	 */
	 @ApiVersion(1.0)
	 @ApiOperation(value="创建用户", notes="根据User对象创建用户")
	 @ApiImplicitParams({
	        @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "UserBean")
	 })
	 @ApiResponses({ 
		   @ApiResponse(code = 500, message = "接口异常"), 
		   @ApiResponse(code = 403, message = "没有权限") 
	 })
    @RequestMapping(value="/insertUser", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
    public ResponseEntity<JsonResultBean> insertUser(@RequestBody UserBean user) {
		JsonResultBean r = new JsonResultBean();
		try {
			int result = userServiceImpl.insertUser(user);
			if(result>0){
				r.setStatus(JsonResultBean.SUCCESS);
			}else{
				r.setStatus(JsonResultBean.FAIL);
			}
		} catch (Exception e) {
			r.setStatus(JsonResultBean.FAIL);
			logger.error("postUser error! ", e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(r);
    }

	
	
	/**
	 * 修改用戶
	 * @param user
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@ApiVersion(1.0)
	@ApiOperation(value="更新用户详细信息", notes="根据url的id来指定更新对象，并根据传过来的user信息来更新用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "UserBean")
    })
	@ApiResponses({ 
		   @ApiResponse(code = 500, message = "接口异常"), 
		   @ApiResponse(code = 403, message = "没有权限")
	})
    @RequestMapping(value="updateUser/{userId}", method=RequestMethod.PUT, produces="application/json;charset=UTF-8")
    public ResponseEntity<JsonResultBean> updateUser(@PathVariable(value = "userId") Long userId, @RequestBody UserBean user) {
		JsonResultBean r = new JsonResultBean();
		try {
			int result = userServiceImpl.updateUser(user, userId);
			if(result>0){
				r.setStatus(JsonResultBean.SUCCESS);
			}else{
				r.setStatus(JsonResultBean.FAIL);
			}
		} catch (Exception e) {
			r.setStatus(JsonResultBean.FAIL);
			logger.error("putUser error! ", e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(r);
    }

	
	/**
     * 刪除用戶
     * @param userId
     * @return
     * @throws Exception
     */
	@ApiVersion(1.0)
    @ApiOperation(value="删除用户", notes="根据url的id来指定删除对象")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long", paramType = "path")
    })
	@ApiResponses({ 
		   @ApiResponse(code = 500, message = "接口异常"), 
		   @ApiResponse(code = 403, message = "没有权限")
	})	
    @RequestMapping(value="/deleteUser/{userId}", method=RequestMethod.DELETE, produces="application/json;charset=UTF-8")
    public ResponseEntity<JsonResultBean> deleteUser(@PathVariable(value = "userId") Long userId) {
    	JsonResultBean r = new JsonResultBean();
		try {
			int result = userServiceImpl.deleteUser(userId);
			if(result>0){
				r.setStatus(JsonResultBean.SUCCESS);
			}else{
				r.setStatus(JsonResultBean.FAIL);
			}
		} catch (Exception e) {
			r.setStatus(JsonResultBean.FAIL);
			logger.error("deleteUser error! ", e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(r);
    }


	/**
	 * 根据ID查询用户
	 * @param userId
	 * @return
	 */
	@ApiVersion(1.0)
	@ApiOperation(value="获取用户详细信息", notes="根据url的id来获取用户详细信息") 
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long",  paramType = "path")
    })
	@ApiResponses({ 
		   @ApiResponse(code = 500, message = "接口异常"), 
		   @ApiResponse(code = 403, message = "没有权限")
	})	
	@RequestMapping(value = "getUser/{userId}", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
	public ResponseEntity<JsonResultBean> getUserById (@PathVariable(value = "userId") Long userId){
		JsonResultBean r = new JsonResultBean();
		try {
			UserBean user = userServiceImpl.getUserInfoById(userId);
	        if(null==user){
	        	user = new UserBean();
	        }
	        
			r.setResult(user);
			r.setStatus(JsonResultBean.SUCCESS);
		} catch (Exception e) {
			r.setResult(e.getClass().getName() + ":" + e.getMessage());
			r.setStatus(JsonResultBean.FAIL);
			logger.error("getUserById error! request userId: "+userId, e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(r);
	}
	
	
	/**
	 * 根据用户账号，查询用户
	 * @param userId
	 * @return
	 */
	@ApiVersion(1.0)
	@ApiOperation(value="获取用户详细信息", notes="根据用户账号来获取用户详细信息")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "userAccount", value = "用户账号", required = true, dataType = "String",  paramType = "query")
    })
	@ApiResponses({ 
		   @ApiResponse(code = 500, message = "接口异常"), 
		   @ApiResponse(code = 403, message = "没有权限")
	})	
	@RequestMapping(value = "getUserByAccount", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
	public ResponseEntity<JsonResultBean> getUserByAccount (@RequestParam("userAccount") String userAccount){
		JsonResultBean r = new JsonResultBean();
		try {
			UserBean user = userServiceImpl.getUserInfoByAccount(userAccount);
	        if(null==user){
	        	user = new UserBean();
	        }
	        
			r.setResult(user);
			r.setStatus(JsonResultBean.SUCCESS);
		} catch (Exception e) {
			r.setResult(e.getClass().getName() + ":" + e.getMessage());
			r.setStatus(JsonResultBean.FAIL);
			logger.error("getUserByAccount error! request userAccount: "+userAccount, e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(r);
	}
	
	/**
	 * 根据用户账号和用户名称,查询用户
	 * @param userId
	 * @return
	 */
	@ApiVersion(1.0)
	@ApiOperation(value="获取用户详细信息", notes="根据用户账号和用户名称，查询用户")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userAccount", value = "用户账号", required = true, dataType = "String", paramType="query"),
        @ApiImplicitParam(name = "userName", value = "用户姓名", required = true, dataType = "String", paramType="query")
    })
	@ApiResponses({ 
		   @ApiResponse(code = 500, message = "接口异常"), 
		   @ApiResponse(code = 403, message = "没有权限")
	})	
	@RequestMapping(value = "getUsersByAccountName", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
	public ResponseEntity<JsonResultBean> getUsersByAccountName (@RequestParam("userAccount") String userAccount,
			@RequestParam("userName") String userName){
		JsonResultBean r = new JsonResultBean();
		try {
			List<UserBean> users = userServiceImpl.getUsersByPager(userAccount, userName, 0, 0);
	        if(null==users){
	        	users = new ArrayList<UserBean>();
	        }
	        
			r.setResult(users);
			r.setStatus(JsonResultBean.SUCCESS);
			r.setStatus(JsonResultBean.SUCCESS);
		} catch (Exception e) {
			///r.setResult(e.getClass().getName() + ":" + e.getMessage());
			r.setStatus(JsonResultBean.FAIL);
			logger.error("getUsersByAccountName error! request userAccount: "+userAccount+", userName:"+userName, e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(r);
	}
	
	
	
	
	@ApiIgnore//使用该注解忽略这个API
	@ApiResponses({ 
		   @ApiResponse(code = 500, message = "接口异常"), 
		   @ApiResponse(code = 403, message = "没有权限")
	})
	@RequestMapping(value = "/hi", method = RequestMethod.GET)
	public String  jsonTest() {
		return " hi you!";
	}
	
	
	
	
}
