package edu.avans.hartigehap.domain;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@Getter @Setter
@ToString(callSuper=true, includeFieldNames=true, of= {})
@NoArgsConstructor
public class Drink extends MenuItem {
	private static final long serialVersionUID = 1L;
	
	private Size size;

	public enum Size {
		SMALL, MEDIUM, LARGE
	}

	public Drink(String id, String imageFileName, int price, MenuType type, Size size) {
		super(id, imageFileName, price, type);
		this.size = size;

	}

	// business logic
}
