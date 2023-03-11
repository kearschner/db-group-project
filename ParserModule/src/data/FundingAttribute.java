package data;

final public class FundingAttribute extends CourseAttribute{
	
	final String fundingLevel;
	final String fundingCampus;

	public FundingAttribute(String name, String level, String fundingCampus) {
		super(name);
		this.fundingLevel = level;
		this.fundingCampus = fundingCampus;
	}

	public FundingAttribute(String name) {
		super(name);

		String[] namePortions = name.trim().split(" ");
		fundingLevel = namePortions[1];
		fundingCampus = namePortions[2];
	}

	public String fundingLevel() { return fundingLevel; }
	public String fundingCampus() { return fundingCampus; }
	
}
