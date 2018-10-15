package com.irille.core.web.exception;

public enum ReturnCode {
	success(1),//成功
	failure(-1),//成功
	
	tools_unknow(100),//功能异常
	
	service_unknow(200),//业务逻辑异常
	service_timeout(201),//登录超时
	service_verification_code(202),//验证码校验失败
	service_wrong_data(203),//错误数据
	service_gone(204),//资源不可用
	service_unauthorized(205), //没有权限
	
	third_unknow(300),//第三方库异常
	
	db_unknow(400),//数据库异常
	
	valid_unknow(500),//表单校验异常
	valid_notnull(501),//不能为空
	valid_notempty(502),//不能为空字符串
	valid_notblank(503),//不能没有内容
	valid_regex(504),//正则校验不通过
	valid_illegal(505),//非法参数
	;
	int code;
	private ReturnCode(int code) {
		this.code = code;
	}
	public int getCode() {
		return this.code;
	}
}
