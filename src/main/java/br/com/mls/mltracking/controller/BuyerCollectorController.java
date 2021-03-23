package br.com.mls.mltracking.controller;

import br.com.mls.mltracking.dto.CategoryAmountDTO;
import br.com.mls.mltracking.json.CategoryAmountCollection;
import br.com.mls.mltracking.service.BuyerCollectorService;
import br.com.mls.mltracking.util.LoginData;
import br.com.mls.mltracking.vo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class BuyerCollectorController {

	@Autowired
	private BuyerCollectorService buyerCollectorService;
	
	@Autowired
	private LoginData loginData;

	@RequestMapping("/buyers/{buyerId}/items/categories")
	public Collection<Category> getBuyerCollectorCategories(@RequestHeader("meliClientId") Long meliClientId,
															@RequestHeader("meliClientSecret") String meliClientSecret,
															@PathVariable("buyerId") Long id,
															@RequestParam(name="status") String status) {
		loginData.setClientId(meliClientId);
		loginData.setClientSecret(meliClientSecret);
		return buyerCollectorService.getCategories(id, status);
	}

	@RequestMapping("/buyers/{buyerId}/items/categories/amount")
	public CategoryAmountCollection getBuyerCollectorCategoriesAmount(@RequestHeader("meliClientId") Long meliClientId,
                                                                      @RequestHeader("meliClientSecret") String meliClientSecret,
                                                                      @PathVariable("buyerId") Long id) {
		loginData.setClientId(meliClientId);
		loginData.setClientSecret(meliClientSecret);
        Collection<CategoryAmountDTO> categoriesAmount = null;

		categoriesAmount = buyerCollectorService.getCategoriesAmount(id);

        CategoryAmountCollection categoryAmountCollection = new CategoryAmountCollection();
        categoryAmountCollection.addCategories(categoriesAmount);
        return categoryAmountCollection;
	}
}
