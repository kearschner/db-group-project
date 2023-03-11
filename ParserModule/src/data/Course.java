package data;

public record Course(
	String title,
	String courseNumber,
	Subject subject,
	int credits,
	String department,
	boolean permitRequired,
	FundingAttribute fundingAttribute,
	CourseAttribute[] additionalAttributes
) { }
