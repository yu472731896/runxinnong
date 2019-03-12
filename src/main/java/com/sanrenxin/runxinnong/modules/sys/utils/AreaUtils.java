package com.sanrenxin.runxinnong.modules.sys.utils;

import com.sanrenxin.runxinnong.common.utils.SpringContextHolder;
import com.sanrenxin.runxinnong.modules.sys.dao.AreaDao;
import com.sanrenxin.runxinnong.modules.sys.entity.Area;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 地址工具类
 * @author LiuDeHua
 * @version 2015-11-17
 */
public class AreaUtils implements Serializable {
	
	private static final long serialVersionUID = -8036916498685365191L;
	
	private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);

	public static String getAreaName(String areaId, String defaultValue){
		if (StringUtils.isNotBlank(areaId)){
			Area area = (Area) UserUtils.getCache(areaId);
			if(area == null){
				area = areaDao.get(areaId);
				if(area == null){
					return defaultValue;
				}
				UserUtils.putCache(areaId, area);
			}
			return area.getName();
		}
		return defaultValue;
	}
	/**
	 * 通过名称和类型获取区域ID
	 * @param name
	 * @param type
	 * @return
	 */
	public static String findAreaIdByLikeNameAndType(String name,String type){
		List<Area> areaList = areaDao.findNameLike(name, type);
		if(null != areaList && areaList.size() > 0){
			return areaList.get(0).getId();
		}
		return null;
	}
	
}
