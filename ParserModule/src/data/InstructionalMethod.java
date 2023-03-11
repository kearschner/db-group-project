package data;

public enum InstructionalMethod {
	All_ONLINE ("All Online"),
	CLASSROOM ("Classroom"),
	FLEXIBLE ("Flexible Online/In-Person"),
	HYBRID ("Hybrid Blend"),
	PRIMARY_DL ("Primarily DL");

	private final String name;

	private InstructionalMethod(String name) {
		this.name = name;
	}

	public static InstructionalMethod fromString(String s) {
		switch (s) {
			case "All Online":
				return All_ONLINE;
			case "Classroom":
				return CLASSROOM;
			case "Flexible Online/In-Person":
				return FLEXIBLE;
			case "Hybrid Blend":
				return HYBRID;
			case "Primarily DL":
				return PRIMARY_DL;
			default:
				return null;
		}
	}

	@Override
	public String toString() {
		return this.name;
	}

}
