package br.com.mls.mltracking.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {

	private String id;
	
	private String description;
	
	private String categoryId;
	
	@Override
	public String toString() {
		return this.description;
	}
	
}
