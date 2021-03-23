package br.com.mls.mltracking.dto;

import br.com.mls.mltracking.vo.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by manasses on 9/3/16.
 *
 * Represents the data that categories could support to help the analytics
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryAmountDTO {

    private Category category;
    private Integer amount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryAmountDTO that = (CategoryAmountDTO) o;
        return getCategory().equals(that.getCategory());
    }

    @Override
    public int hashCode() {
        return getCategory().hashCode();
    }
}
