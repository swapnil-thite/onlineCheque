package com.rest.webservices;


public class EmailData {

	private String mobile;
	private String amount;
	private String chequeNumber;
	private String bankUser;
	private String bankName;
	private String email;
	private String accNo;
	
	public EmailData() {
	}
	public EmailData(String mobile, String amount, String chequeNumber, String bankUser, String bankName, String email,
			String accNo) {
		super();
		this.mobile = mobile;
		this.amount = amount;
		this.chequeNumber = chequeNumber;
		this.bankUser = bankUser;
		this.bankName = bankName;
		this.email = email;
		this.accNo = accNo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAmount() {
		return amount;
	}
	public String getChequeNumber() {
		return chequeNumber;
	}
	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}
	public String getBankUser() {
		return bankUser;
	}
	public void setBankUser(String bankUser) {
		this.bankUser = bankUser;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	
}
