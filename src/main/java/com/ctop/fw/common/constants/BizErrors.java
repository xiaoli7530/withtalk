package com.ctop.fw.common.constants;

public class BizErrors {
	/**用户名不存在!*/
	public static final String SYS_LOGIN_LOGIN_NAME_INVALID = "SYS_LOGIN_LOGIN_NAME_INVALID";
	/**密码错误!*/
	public static final String SYS_LOGIN_PASSWORD_INVALID = "SYS_LOGIN_PASSWORD_INVALID";
	
	/**存在重复的周转规则{0}!*/
	public static final String WM_OUT_RULE_CODE_DUPLICATED = "WM_OUT_RULE_CODE_DUPLICATED";
	
	public static class SysAccountErrors {
		public static String accountLocked4Login = "sys.sysAccount.accountLocked4Login";
		public static String loginNameInvalid = "sys.sysAccount.loginNameInvalid";
		public static String passwordInvalid = "sys.sysAccount.passwordInvalid";
	}
	
	public static class WmWarehouseAreaGroupDetailErrors {
		//存在重复的排序号!
		public static String duplicateSeqNo = "wmbase.wmWarehouseAreaGroupDetail.duplicateSeqNoExist";
	}
}
