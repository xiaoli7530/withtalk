
package com.ctop.fw.common.constants;

import java.math.BigDecimal;

public class Constants {

	/** 存放当前登陆信息 */
	public static final String SESSION_KEY_USER = "USER";
	public static final String DEFAULT_PASSWORD = "123456";
	public static final String SUPER_ADMIN = "admin";
	public static final int DEFAULT_UNIT_SCALE = 4;
	
	/** 是否 */
	public static final class YesNo {
		public static final String YES = "Y";
		public static final String NO = "N";
	}

	/** excel导入步骤 */
	public static class ExcelImportStage {
		/** 数据进入中间表, 但是有校验错 */
		public static final String ENTER_TEMP_TABLE = "TEMP_TABLE";
		/** 数据进入中间表，基本校验通过 */
		public static final String BASIC_VALID = "BASIC_VALID";
		/** 数据在中间表，已完成校验 */
		public static final String ADVANCE_VALID = "AD_VALID";
		/** 数据完成导入 */
		public static final String DONE = "DONE";
	}

	public static class SysAccountStatus {
		public static final String COMMON = "common";
		public static final String LOCKED = "locked";
	}

	
	public static class CacheKeys {
		public static String DICT_ALL="dict_all";//所有字典
		public static String DICT_JS="dict_js";//字典的js字符串
		public static String COUNTRYS_JS="countrys_js";//国家省份城市的JS
	}
	
	/**
	 * @author YangHongyuan websocket消息类型
	 */
	public class WsMsgType {
		/**
		 * 上线
		 */
		public static final String ONLINE = "user_join";
		/**
		 * 下线
		 */
		public static final String OFFLINE = "user_leave";
		/**
		 * 通知
		 */
		public static final String NOTICE = "notice";
		/**
		 * 聊天消息
		 */
		public static final String CHAT = "chat";
		/**
		 * 业务消息
		 */
		public static final String BIZ = "biz";
		/**
		 * 待办事项
		 */
		public static final String TASK = "task";
		/**
		 * 好友清单
		 */
		public static final String FRIENDS = "friends";
	}
	
	public static class LoginType {
		public static final String EPMSWMS_WEB = "epmswms_web";
		public static final String EPMSWMS_WEBSSO = "epmswms_websso";
		public static final String EPMSWMS_RF = "epmswms_rf";
		public static final String EPMS_WEB = "epms_web";
		public static final String EPMS_APP_IOS = "epms_app_ios";
	}
	
	public static class AccountRefType {
		/** 员工 */
		public static final String EMPLOYEE = "employee";
	}

	/**
	 * 人员下拉选择默认数
	 */
	public static final int O2OHREMPLOYEENUMBER = 8;
	
	/**
	 * combogrid下拉数据默认条数
	 */
	public static final int COMBOGRID_ROW_LENGTH = 100;

	/**
	 * 调度状态
	 * 
	 * @author Administrator
	 *
	 */
	public class ScheduleConfigStatus {
		/** 活跃的 */
		public static final String ACTIVE = "active";
		/** 暂停 */
		public static final String PAUSE = "pause";
		/** 待加载 */
		public static final String WAIT_LOAD = "wait_load";
		/** 待卸载 */
		public static final String WAIT_UNlOAD = "wait_unload";
	}

	/**
	 * 调度状态
	 * 
	 * @author Administrator
	 *
	 */
	public class ScheduleConfigBizType {
		/** java调用 */
		public static final String TASK_CLASS = "task_class";
		/** 调用sql */
		public static final String TASK_SQL = "task_sql";
		/** 调用存储过程 */
		public static final String TASK_PROCEDURE = "task_procedure";
		/** rest请求 */
		public static final String TASK_REST = "task_rest";
	}

	/**
	 * 最后一次执行状态
	 * @author Administrator
	 */
	public class ScheduleConfigLastRunStatus {
		/** 成功 */
		public static final String SUCCESS = "success";
		/** 失败 */
		public static final String FAIL = "fail";
	}

	public static class LoginErrType {
		// 用户名不存在，密码错误, 锁定
		public static final String USER_NO_EXIST = "userNotExist";
		public static final String PASSWORD_INVALID = "passwordInvalid";
		public static final String LOCKED = "locked";
		public static final String OTHER_ERROR = "other";
	}
	
	
	public static class SysEmailStatus {
		public static final String SEND_TYPE_CRASH = "1";
		public static final String SEND_TYPE_COMMON = "0";

		/**
		 * 待发
		 */
		public static final String STATUS_AWAIT = "0";
		/**
		 * 部分发送
		 */
		public static final String STATUS_PAR_SENDED = "1";
		/**
		 * 发送失败
		 */
		public static final String STATUS_FALL_SENDED = "-1";
		/**
		 * 已发
		 */
		public static final String STATUS_IS_SENDED = "2";

		public static BigDecimal LARGEST_FILE_SIZE = BigDecimal.valueOf(1024);
	}
	
	/**
	 * 发送的状态
	 * @author Administrator
	 *
	 */
	public static class sendStatus {
		public static final String NO_SEND = "no_send";
		public static final String TO_SEND = "to_send";
		public static final String SEND_SUS = "send_sus";
		public static final String SEND_FAIL = "send_fail";
	}
	
	/**
	 * 流程状态
	 */
	public static final class FlowStatus {
		public static final String NO_SUBMIT = "NO_SUBMIT";//未提交
		public static final String IN_AUDIT = "IN_AUDIT";//审核中
		public static final String AUDIT_COMPLATE = "AUDIT_COMPLATE";//审核通过
		public static final String CLOSE = "CLOSE";//关闭
	}
	
	
	
	public static class UserStatus {
		public static final String COMMON = "common";
		public static final String LOCKED = "locked";
	}

}