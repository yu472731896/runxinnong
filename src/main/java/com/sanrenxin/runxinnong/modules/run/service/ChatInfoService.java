/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.service.CrudService;
import com.sanrenxin.runxinnong.modules.run.entity.ChatInfo;
import com.sanrenxin.runxinnong.modules.run.dao.ChatInfoDao;

/**
 * 聊天信息Service
 * @author mh
 * @version 2019-01-29
 */
@Service
@Transactional(readOnly = true)
public class ChatInfoService extends CrudService<ChatInfoDao, ChatInfo> {

	public ChatInfo get(String id) {
		return super.get(id);
	}
	
	public List<ChatInfo> findList(ChatInfo chatInfo) {
		return super.findList(chatInfo);
	}
	
	public Page<ChatInfo> findPage(Page<ChatInfo> page, ChatInfo chatInfo) {
		return super.findPage(page, chatInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(ChatInfo chatInfo) {
		super.save(chatInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(ChatInfo chatInfo) {
		super.delete(chatInfo);
	}
	
}