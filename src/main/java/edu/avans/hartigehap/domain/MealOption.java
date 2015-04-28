package edu.avans.hartigehap.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

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
public class MealOption extends MenuItem {
	private static final long serialVersionUID = 1L;
	
	private String recipe;
	
	@ManyToMany
	private Collection<MenuItem> menuItems = new ArrayList<MenuItem>();

	public MealOption(String id, String imageFileName, int price, MenuType type, String recipe) {
		super(id, imageFileName, price, type);
		this.recipe = recipe;
	}
}
