package com.ctop.base.dto;

import java.math.BigDecimal;

public class BaseIndexInfoDto {
	private BigDecimal pullQty;
	private BigDecimal blQty;
	private BigDecimal outQty;
	private BigDecimal inQty;
	private BigDecimal psnQty;
	private BigDecimal skuQty;
	
	public BigDecimal getPullQty() {
		return pullQty;
	}
	public void setPullQty(BigDecimal pullQty) {
		this.pullQty = pullQty;
	}
	public BigDecimal getBlQty() {
		return blQty;
	}
	public void setBlQty(BigDecimal blQty) {
		this.blQty = blQty;
	}
	public BigDecimal getOutQty() {
		return outQty;
	}
	public void setOutQty(BigDecimal outQty) {
		this.outQty = outQty;
	}
	public BigDecimal getInQty() {
		return inQty;
	}
	public void setInQty(BigDecimal inQty) {
		this.inQty = inQty;
	}
	public BigDecimal getPsnQty() {
		return psnQty;
	}
	public void setPsnQty(BigDecimal psnQty) {
		this.psnQty = psnQty;
	}
	public BigDecimal getSkuQty() {
		return skuQty;
	}
	public void setSkuQty(BigDecimal skuQty) {
		this.skuQty = skuQty;
	}
	
	
}
