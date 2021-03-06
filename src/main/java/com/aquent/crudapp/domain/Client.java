package com.aquent.crudapp.domain;

import java.util.ArrayList;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Client {
    private Integer clientId;

    @NotNull
    @Size(min = 1, max = 50, message = "Company is required with maximum length of 50")
    private String company;

    @NotNull
    @Size(min = 1, max = 50, message = "Website is required with maximum length of 50")
    private String website;

    @NotNull
    @Size(min = 1, max = 50, message = "Phone is required with length of 10")
    private String phone;

    @NotNull
    @Size(min = 1, max = 50, message = "Mailing address is required with maximum length of 50")
    private String mailing;
    
    private ArrayList<Integer> personIds;
    private ArrayList<String> personNames;


	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMailing() {
		return mailing;
	}

	public void setMailing(String mailing) {
		this.mailing = mailing;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	
	public ArrayList<Integer> getPersonIds() {
		return personIds;
	}

	public void setPersonIds(ArrayList<Integer> personIds) {
		this.personIds = personIds;
	}

	public ArrayList<String> getPersonNames() {
		return personNames;
	}

	public void setPersonNames(ArrayList<String> names) {
		this.personNames = names;
	}
}
