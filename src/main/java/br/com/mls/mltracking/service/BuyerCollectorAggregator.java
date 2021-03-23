package br.com.mls.mltracking.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import br.com.mls.mltracking.dto.CategoryAmountDTO;
import com.google.gwt.thirdparty.guava.common.collect.Sets;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.gwt.thirdparty.guava.common.collect.HashMultiset;
import com.google.gwt.thirdparty.guava.common.collect.Multiset;
import com.google.gwt.thirdparty.guava.common.collect.Multisets;

import br.com.mls.mltracking.vo.Category;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Component
@Scope("prototype")
public class BuyerCollectorAggregator {

	private Map<Long, Set<Category>> buyerCategoriesMap;
	private Map<Long, Multiset<Category>> buyerCategoryPurchaseCountMap;

	BuyerCollectorAggregator() {
		buyerCategoryPurchaseCountMap = new HashMap<>();
		buyerCategoriesMap = new HashMap<>();
	}

	public void add(Long buyerId, List<Category> categories) {
		if (buyerCategoriesMap.get(buyerId) == null) {
			Set<Category> categoriesSet = new HashSet<>(categories);
			buyerCategoriesMap.put(buyerId, categoriesSet);
		} else {
			buyerCategoriesMap.get(buyerId).addAll(categories);
		}

		for (Category category : categories) {
			if (buyerCategoryPurchaseCountMap.containsKey(buyerId)) {
				Multiset<Category> categoryCountMap = buyerCategoryPurchaseCountMap.get(buyerId);
				categoryCountMap.add(category);
			} else {
				Multiset<Category> categoryCountMap = HashMultiset.create();
				categoryCountMap.add(category);
				buyerCategoryPurchaseCountMap.put(buyerId, categoryCountMap);
			}
		}
	}

	public Multiset<Category> getCategoryPurchaseCount(Long buyerId) {
		return buyerCategoryPurchaseCountMap.get(buyerId);
	}

	public Collection<Category> getCategories(Long buyerId) {
		return buyerCategoriesMap.get(buyerId);
	}

	/**
	 * Get the bought items category list count above a threshold that could classifies this as a collector.
	 * <br>
	 * The categories are ordered by descending count
	 * @param buyerId
	 * @param threshold
	 * @return
	 */
	public Collection<Category> getCollectionKinds(Long buyerId, Integer threshold) {
		Multiset<Category> categories = buyerCategoryPurchaseCountMap.get(buyerId);
		Set<Category> collectorCategories = Sets.newLinkedHashSet();
		for (Category category : Multisets.copyHighestCountFirst(categories)) {
			if (categories.count(category) >= threshold) {
				collectorCategories.add(category);
			}
		}
		return collectorCategories;
	}

	public void addCategoryAmount(List<CategoryAmountDTO> categoryAmountDTOs, List<Category> categories) {
		Map<Category, Long> itemCategories = categories.stream().collect(groupingBy(Function.identity(), counting()));
		for (Category category : itemCategories.keySet()) {
			if (categoryAmountDTOs.contains(category)) {
                CategoryAmountDTO categoryAmountDTO = categoryAmountDTOs.stream().filter(dto -> category.equals(dto.getCategory())).findFirst().get();
                Integer currentAmount = categoryAmountDTO.getAmount();
                categoryAmountDTO.setAmount(currentAmount + itemCategories.get(category).intValue());
            } else {
				categoryAmountDTOs.add(new CategoryAmountDTO(category, itemCategories.get(category).intValue()));
			}
		}
	}
}
