
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
	

	/**
	 * 二维码
	 */
	public static class QrCode{
		
		/**标签二维码开始字符串 */
		public static final String START = "[)>";
		/**入库标签格式表头*/
		public static final String IN_LABEL_TITLE = "06";
		/**出库标签格式表头*/
		public static final String OUT_LABEL_TITLE = "08";
		/**入厂标签格式表头*/
		public static final String IN_PLACE_TITLE = "09";
		/**标签二维码结束符ASII:4*/
		public static final String END_OF_TRANSMISSION = "";
		/**标签二维码分组符ASII:29*/
		public static final String GROUP_SEPARATOR = "";
		/**标签二维码记录分隔符ASII:30*/
		public static final String RECORD_SEPARATOR = "";
		/**标签二维码零件号前缀*/
		public static final String PART_CODE_PREFIX ="P";
		/**标签二维码项目号前缀*/
		public static final String PROJECT_CODE_PREFIX ="XM";
		/**标签二维码订单号前缀*/
		public static final String PO_CODE_PREFIX ="K";
		/**标签二维码入厂的号前缀*/
		public static final String IN_CODE_PREFIX ="I";
		/**标签二维码ASN号前缀*/
		public static final String ASN_CODE_PREFIX ="SNR";
		/**标签二维码批次总量前缀*/
		public static final String BATCH_QTY_PREFIX ="B";
		/**标签二维码箱数量前缀*/
		public static final String BOX_QTY_PREFIX ="Q";
		/**标签二维码扩展信息前缀*/
		public static final String EXPANSION_INFO_PREFIX ="C";
		/**标签二维码箱号前缀*/
		public static final String BOX_PREFIX ="1J";
		/**标签二维码交样日期前缀*/
		public static final String DELIVERY_DATE_PREFIX ="SP";
		/**标签二维码入厂日期前缀*/
		public static final String IN_DATE_PREFIX ="ID";
		/**标签二维码物料用途前缀*/
		public static final String USAGE_PREFIX ="U";
		/**标签二维码SMT前缀*/
		public static final String SMT_PREFIX ="S";
		/**标签二维码需求人信息前缀*/
		public static final String APPLICANT_PREFIX ="A";
		/**标签二维码DRE信息前缀*/
		public static final String DRE_PREFIX ="D";
		/**标签二维码返修单前缀*/
		public static final String REPAIR_CODE_PREFIX ="R";
		/**标签二维码任务单前缀*/
		public static final String TASK_CODE_PREFIX ="T";
		/**标签二维码结束符解析*/
		public static final String ANALYZE_END_OF_TRANSMISSION = "`EOT";
		/**标签二维码分组符解析*/
		public static final String ANALYZE_GROUP_SEPARATOR = "`GS";
		/**标签二维码记录分隔符解析*/
		public static final String ANALYZE_RECORD_SEPARATOR = "`RS";
	}
	
	
	public static class UserStatus {
		public static final String COMMON = "common";
		public static final String LOCKED = "locked";
	}

}