package com.ctop.fw.common.utils;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ctop.fw.common.constants.Constants;
import com.ctop.fw.common.model.UserDto;

/**用户上下文的辅佐方法，取登陆用户，货主，当前仓库等信息*/
public class UserContextUtil {

	public static UserDto getUser() {
		ServletRequestAttributes ra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpSession session = null;
		if(ra != null) {
			session = ra.getRequest().getSession();
		}
		if(session == null) {
			return null;
		}
        return (UserDto) session.getAttribute(Constants.SESSION_KEY_USER);
	}
	
	public static void setUser(UserDto userDto) {
		ServletRequestAttributes ra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpSession session = null;
		if(ra != null) {
			session = ra.getRequest().getSession();
		}
		if(session != null) {
			session.setAttribute(Constants.SESSION_KEY_USER, userDto);
		}
	}
	
	
	/**在Filter中用的*/
	public static String getLoginName(HttpSession session) {
		UserDto user = (UserDto) session.getAttribute(Constants.SESSION_KEY_USER);
		return user != null && user.getSysAccount() != null ? user.getSysAccount().getLoginName() : "";
	}
	
	public static String getAccountUuid() {
		UserDto user = getUser();
		return user != null && user.getSysAccount() != null ? user.getSysAccount().getAccountUuid() : "";
	}
	
	public static String getLoginName() {
		UserDto user = getUser();
		return user != null && user.getSysAccount() != null ? user.getSysAccount().getLoginName() : "";
	}
	
	public static String getCompanyUuid() {
		UserDto user = getUser();
		return user != null && user.getBaseCompany() != null ? user.getBaseCompany().getCompanyUuid() : "1";
	}

	
	public static String getEmpUuid() {
		UserDto user = getUser();
		return user != null && user.getEmpUuid() != null ? user.getEmpUuid() : "";
	}

	public static String getAvatar() {
		UserDto user = getUser();
		return user != null && user.getAvatar() != null ? user.getAvatar() : "";
	}
	
	public static String getEmpName() {
		UserDto user = getUser();
		return user != null && user.getEmpName() != null ? user.getEmpName() : "";
	}
	public static String getEmpPhone() {
		UserDto user = getUser();
		return user != null && user.getEmpPhone() != null ? user.getEmpPhone() : "";
	}
	public static String getDepartmentUuid() {
		UserDto user = getUser();
		return user != null && user.getDepartmentUuid() != null ? user.getDepartmentUuid() : "";
	}
	
	public static String getDepartmentNo() {
		UserDto user = getUser();
		return user != null && user.getDepartmentNo() != null ? user.getDepartmentNo() : "";
	}
	
	public static String getDepartmentType() {
		UserDto user = getUser();
		return user != null && user.getDepartmentType() != null ? user.getDepartmentType() : "";
	}
	
	public static boolean isSuperAdmin(String loginName) {
		return Constants.SUPER_ADMIN.equals(loginName);
	}
	
	public static boolean currentUserIsSuperAdmin(){
		return Constants.SUPER_ADMIN.equals(getLoginName());
	}
	
	/**
	 * 物料工程师交样明细序号
	 * @return
	 */
	public static String getDeliverySeq() {
		UserDto user = getUser();
		return user != null && user.getDeliverySeq() != null ? user.getDeliverySeq() : "";
	}
}
