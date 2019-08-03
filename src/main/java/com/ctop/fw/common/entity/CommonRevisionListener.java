package com.ctop.fw.common.entity;

import org.hibernate.envers.RevisionListener;

import com.ctop.fw.common.model.UserDto;
import com.ctop.fw.common.utils.UserContextUtil;

public class CommonRevisionListener implements RevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
		if(revisionEntity instanceof CommonRevisionEntity) {
			CommonRevisionEntity revision = (CommonRevisionEntity) revisionEntity;
			UserDto user = UserContextUtil.getUser();
			if(user != null && user.getSysAccount() != null) {
				revision.setAccountName(user.getSysAccount().getLoginName());
			}
			revision.setAccountUuid(UserContextUtil.getAccountUuid());
			revision.setTenantUuid(UserContextUtil.getCompanyUuid());
			//revision.setWarehouseUuid(UserContextUtil.getWarehouseUuid());
		}
	}

}
