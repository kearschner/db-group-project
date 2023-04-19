package com.example.demo.data;

import java.util.EnumSet;

public enum Day {
	MON('M'), TUE('T'), WED('W'), THU('R'), FRI('F'), SAT('S'), SUN('U');

	private final char charRep;

	Day(char charRep) {
		this.charRep = charRep;
	}

	public char getCharRep() {
		return charRep;
	}

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
				default:
					return EnumSet.noneOf(Day.class);
			}
		}

		return set;
	}

	public static String stringFromSet(EnumSet<Day> set) {
		var builder = new StringBuilder();
		for (Day day : set) {
			builder.append(builder.append(day.getCharRep()));
		}
		return builder.toString();
	}

}
