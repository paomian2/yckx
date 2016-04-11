package com.brother.yckx.model;

public class OrderRegisterEntity {
	
	private String companyName;
	private String companyAdress;
	private String companyPone;
	private String companyHangye;
	private String companyServiceMain;
	private String principal;
	private String principalPhone;
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyAdress() {
		return companyAdress;
	}
	public void setCompanyAdress(String companyAdress) {
		this.companyAdress = companyAdress;
	}
	public String getCompanyPone() {
		return companyPone;
	}
	public void setCompanyPone(String companyPone) {
		this.companyPone = companyPone;
	}
	public String getCompanyHangye() {
		return companyHangye;
	}
	public void setCompanyHangye(String companyHangye) {
		this.companyHangye = companyHangye;
	}
	public String getCompanyServiceMain() {
		return companyServiceMain;
	}
	public void setCompanyServiceMain(String companyServiceMain) {
		this.companyServiceMain = companyServiceMain;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getPrincipalPhone() {
		return principalPhone;
	}
	public void setPrincipalPhone(String principalPhone) {
		this.principalPhone = principalPhone;
	}
	@Override
	public String toString() {
		return "OrderRegisterEntity [companyName=" + companyName
				+ ", companyAdress=" + companyAdress + ", companyPone="
				+ companyPone + ", companyHangye=" + companyHangye
				+ ", companyServiceMain=" + companyServiceMain + ", principal="
				+ principal + ", principalPhone=" + principalPhone + "]";
	}
	public OrderRegisterEntity(String companyName, String companyAdress,
			String companyPone, String companyHangye,
			String companyServiceMain, String principal, String principalPhone) {
		super();
		this.companyName = companyName;
		this.companyAdress = companyAdress;
		this.companyPone = companyPone;
		this.companyHangye = companyHangye;
		this.companyServiceMain = companyServiceMain;
		this.principal = principal;
		this.principalPhone = principalPhone;
	}
	
	

}
