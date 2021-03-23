package br.com.mls.mltracking.entity;

import br.com.mls.mltracking.vo.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyerCollector extends Buyer {

	/**
	 * The category that represent this buyer as a collector
	 */
	private Category mainCategory;
	
}
