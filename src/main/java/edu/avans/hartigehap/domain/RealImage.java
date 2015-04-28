package edu.avans.hartigehap.domain;

public class RealImage implements Image {
	private String fileName;
	
	public RealImage(String fileName) {
		this.fileName = fileName;
		loadImage(fileName);
	}
	
	@Override
	public String display() {
		return "/resources/images/" + fileName;
	}
	
	private String loadImage(String fileName) {
		return "Laad afbeelding";
	}
}
