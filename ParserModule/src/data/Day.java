package data;

import java.util.EnumSet;

public enum Day {
	MON, TUE, WED, THU, FRI, SAT, SUN;

	public static EnumSet<Day> setFromString(String str) {

		EnumSet<Day> set = EnumSet.noneOf(Day.class);
		for (int i = 0; i < str.length(); i++) {
			char dayChar = str.charAt(i);
			switch (dayChar) {
				case 'M':
					set.add(MON);
					break;
				case 'T':
					set.add(TUE);
					break;
				case 'W':
					set.add(WED);
					break;
				case 'R':
					set.add(THU);
					break;
				case 'F':
					set.add(FRI);
					break;
				case 'S':
					set.add(SAT);
					break;
				case 'U':
					set.add(SUN);
					break;
			}
		}

		return set;
	}

}
