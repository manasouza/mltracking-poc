package br.com.mls.mltracking.builder;

import br.com.mls.mltracking.dto.CategoryAmountDTO;
import br.com.mls.mltracking.vo.Category;

/**
 * Created by manasses on 9/3/16.
 */
public class CategoryDataBuilder {

    private CategoryAmountDTO categoryAmountDTO;

    private CategoryDataBuilder(){
        this.categoryAmountDTO = new CategoryAmountDTO();
    }

    public CategoryDataBuilder amount(Integer amount) {
        this.categoryAmountDTO.setAmount(amount);
        return this;
    }

    public CategoryDataBuilder onCategory(String categoryId, String categoryName) {
        this.categoryAmountDTO.setCategory(new Category(categoryId, categoryName));
        return this;
    }

    public CategoryAmountDTO build() {
        return this.categoryAmountDTO;
    }

    public static CategoryDataBuilder create() {
        return new CategoryDataBuilder();
    }
}
