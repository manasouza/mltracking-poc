package br.com.mls.mltracking.entity;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Question {
	
	@SerializedName("status")
	private String status;
	
	@SerializedName("item_id")
	private String itemId;
	
	@SerializedName("from")
	private Buyer buyer;

}
