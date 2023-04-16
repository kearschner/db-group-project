package com.example.demo.data;

import org.hibernate.annotations.Immutable;

final public class FundingAttribute extends CourseAttribute{

	final String fundingLevel;

	final String fundingCampus;

	public FundingAttribute(String name, String level, String fundingCampus) {
		super(null, name);
		this.fundingLevel = level;
		this.fundingCampus = fundingCampus;
	}

	public FundingAttribute(String name) {
		super(null, name);

		String[] namePortions = name.trim().split(" ");
		fundingLevel = namePortions[1];
		fundingCampus = namePortions[2];
	}

	public FundingAttribute() {
		this("");
	}

	public String fundingLevel() { return fundingLevel; }
	public String fundingCampus() { return fundingCampus; }
	
}
