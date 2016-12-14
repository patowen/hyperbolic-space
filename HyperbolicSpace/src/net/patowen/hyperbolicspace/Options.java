package net.patowen.hyperbolicspace;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Options {
	private boolean canShowErrors = true;
	
	public int treeLevels;
	public int horosphereSize;
	
	// Set defaults
	public Options() {
		treeLevels = 7;
		horosphereSize = 8;
	}
	
	public void readOptionsFile() {
		try (Scanner scan = new Scanner(new File("options.txt"))) {
			int lineNum = 0;
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				lineNum++;
				int commentLocation = line.indexOf('#');
				if (commentLocation != -1) line = line.substring(0, line.indexOf('#'));
				line = line.trim();
				if (line.isEmpty()) continue;
				int equals = line.indexOf('=');
				String option = line.substring(0, equals).trim();
				String value = line.substring(equals+1).trim();
				interpretOption(option, value, lineNum);
			}
		} catch (FileNotFoundException e) {
			showError("Could not find \"options.txt\". Using defaults.");
		}
	}
	
	private void interpretOption(String option, String value, int lineNum) {
		if (option.equals("tree_levels")) {
			treeLevels = getIntValue(option, value, treeLevels, lineNum);
		} else if (option.equals("horosphere_size")) {
			horosphereSize = getIntValue(option, value, horosphereSize, lineNum);
		} else {
			showLineError("Unknown option \"" + option + "\"", lineNum);
		}
	}
	
	private int getIntValue(String option, String valueStr, int defaultValue, int lineNum) {
		try {
			return Integer.parseInt(valueStr);
		} catch (NumberFormatException e) {
			showLineError("Could not set " + option + " to \"" + valueStr +
					"\" because it needs to be set to an integer value. Using default.", lineNum);
			return defaultValue;
		}
	}
	
	private void showLineError(String str, int lineNum) {
		showError("Error in options.txt at line " + lineNum + ": " + str);
	}
	
	private void showError(String str) {
		if (canShowErrors) {
			JOptionPane.showMessageDialog(null, str, "Error", JOptionPane.ERROR_MESSAGE);
			System.err.println(str);
			canShowErrors = false;
		}
	}
}
