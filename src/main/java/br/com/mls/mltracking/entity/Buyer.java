/**
 * 
 */
package br.com.mls.mltracking.entity;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Buyer {

	@SerializedName("id")
	private Long id;
	
}
