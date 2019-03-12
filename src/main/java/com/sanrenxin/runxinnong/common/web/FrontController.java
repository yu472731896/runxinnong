package com.sanrenxin.runxinnong.common.web;

import com.sanrenxin.runxinnong.common.persistence.DataEntity;
import com.sanrenxin.runxinnong.common.service.CrudService;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class FrontController extends BaseController {

	/**
	 * 前端基础路径
	 */
	@Value("${frontPath}")
	protected String frontPath;
	
	/**
	 * 前端基础路径
	 */
	@Value("${basePathCashier}")
	protected String basePathCashier;

	/**
	 * 保存实体方法
	 * 
	 * @param service
	 * @param entity
	 */
	protected void saveEntity(CrudService service, DataEntity entity) {
		HashMap<String, String> currentUserTag = new HashMap<String, String>();
		currentUserTag.put("currentUserTag", "frontUser");
		threadLocal.set(currentUserTag);
		service.save(entity);
	}

	/**
	 * 保存实体方法
	 * 
	 * @param service
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	protected void saveEntityByParamList(CrudService service, Object... params)
			throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		HashMap<String, String> currentUserTag = new HashMap<String, String>();
		currentUserTag.put("currentUserTag", "frontUser");
		threadLocal.set(currentUserTag);

		Class<?> objectClass = service.getClass();
		Class[] classList = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			classList[i] = params[i].getClass();
		}
		
		Method method = objectClass.getMethod("save", classList);
		method.invoke(service, params.clone());
	}
	
	/**
	 * 更新实体方法
	 * 
	 * @param service
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	protected void updateEntityByParamList(CrudService service, Object... params)
	throws NoSuchMethodException, SecurityException,
	IllegalAccessException, IllegalArgumentException,
	InvocationTargetException {
		HashMap<String, String> currentUserTag = new HashMap<String, String>();
		currentUserTag.put("currentUserTag", "frontUser");
		threadLocal.set(currentUserTag);
		
		Class<?> objectClass = service.getClass();
		Class[] classList = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			classList[i] = params[i].getClass();
		}
		
		Method method = objectClass.getMethod("update", classList);
		method.invoke(service, params.clone());
	}
	
	
/*	public AccessToken getAccessToken() {
		AccessToken accessToken = null;
		TokenThread tokenThread = new TokenThread();
		accessToken = tokenThread.accessToken;
		return accessToken;
	}*/
}
