package com.commonsense.hkgalden.security;

public class InputValidation {

	private static String errorInputCode;
	public static boolean IsNullOrWhiteSpaces(
			final String string) {
		boolean result = !(string != null && !string.equals("") && string.length() > 0);
		return result;
	}


	public static boolean validateEmail(String email) {

		boolean isCorrect = false;

		if (IsNullOrWhiteSpaces(email)) {
			errorInputCode = "N0000";
			isCorrect = true;
		} else {
			if (!email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
				errorInputCode = "I0000";
				isCorrect = true;
			}
		}
		return isCorrect;
	}



	public String inputErrorMessage() {
		String message = null;
		if(("N0000").equals(errorInputCode)){
			message = "Email must not be empty";
		}
		else if(("I0000").equals(errorInputCode)){
			message = "Email format is incorrect";
		}
		return message;
	}

	


}
