package br.com.mls.mltracking.json;

import br.com.mls.mltracking.dto.CategoryAmountDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by manasses on 9/14/16.
 */
public class CategoryAmountCollection {

    @JsonProperty
    private Collection<CategoryAmountDTO> categoryAmount;

    public void addCategories(Collection<CategoryAmountDTO> categoriesAmount) {
        if (this.categoryAmount == null) {
            this.categoryAmount = new ArrayList<>();
        }
        this.categoryAmount.addAll(categoriesAmount);
    }
}
