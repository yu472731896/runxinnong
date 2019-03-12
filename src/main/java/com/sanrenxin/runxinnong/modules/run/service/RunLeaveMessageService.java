/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.service;

import java.util.List;

import com.sanrenxin.runxinnong.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.service.CrudService;
import com.sanrenxin.runxinnong.modules.run.entity.RunLeaveMessage;
import com.sanrenxin.runxinnong.modules.run.dao.RunLeaveMessageDao;

/**
 * 留言Service
 * @author mingh
 * @version 2019-03-08
 */
@Service
@Transactional(readOnly = true)
public class RunLeaveMessageService extends CrudService<RunLeaveMessageDao, RunLeaveMessage> {

	public RunLeaveMessage get(String id) {
		return super.get(id);
	}
	
	public List<RunLeaveMessage> findList(RunLeaveMessage runLeaveMessage) {
		return super.findList(runLeaveMessage);
	}
	
	public Page<RunLeaveMessage> findPage(Page<RunLeaveMessage> page, RunLeaveMessage runLeaveMessage) {
		return super.findPage(page, runLeaveMessage);
	}
	
	@Transactional(readOnly = false)
	public void save(RunLeaveMessage runLeaveMessage) {
		if(StringUtils.isBlank(runLeaveMessage.getState())){
			runLeaveMessage.setState("0");
		}
		super.save(runLeaveMessage);
	}
	
	@Transactional(readOnly = false)
	public void delete(RunLeaveMessage runLeaveMessage) {
		super.delete(runLeaveMessage);
	}
	
}