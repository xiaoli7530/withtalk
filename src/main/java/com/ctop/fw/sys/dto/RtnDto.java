package com.ctop.fw.sys.dto;

import java.io.Serializable;
import java.util.List;

public class RtnDto implements Serializable{
  private static final long serialVersionUID = -788597312540840051L;
  private String rharset;
  private Integer turnState;
  private String infoCode;
  private String sign;
  private SysAccountDto sysAccountDto;
  
public String getCharset() {
	return rharset;
}
public void setCharset(String inputCharset) {
	this.rharset = inputCharset;
}

public Integer getTurnState() {
	return turnState;
}
public void setTurnState(Integer turnState) {
	this.turnState = turnState;
}
public String getInfoCode() {
	return infoCode;
}
public void setInfoCode(String infoCode) {
	this.infoCode = infoCode;
}

public SysAccountDto getSysAccountDto() {
	return sysAccountDto;
}
public void setSysAccountDto(SysAccountDto sysAccountDto) {
	this.sysAccountDto = sysAccountDto;
}
public String getSign() {
	return sign;
}
public void setSign(String sign) {
	this.sign = sign;
}

  
  
}
