package br.com.mls.mltracking.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import br.com.mls.mltracking.dto.CategoryAmountDTO;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import com.google.gwt.thirdparty.guava.common.collect.Multiset;

import br.com.mls.mltracking.vo.Category;

public class BuyerCollectorAggregatorTest {

	private BuyerCollectorAggregator buyerCollectorAggregator;

	@Before
	public void setUp() {
		buyerCollectorAggregator = new BuyerCollectorAggregator();
	}
	
	@Test
	public void aggregateCategories() {
		// GIVEN
		Long buyerId = 1L;		
		List<Category> categories = CategoriesBuilder.create()
				.addCategory("MLB1571","MLB1572","MLB1573","MLB1571","MLB1574","MLB1571","MLB1573")
				.build();
		
		// GIVEN - buyer category addition
		buyerCollectorAggregator.add(buyerId, categories);
		
		// WHEN
		Collection<Category> aggregatedCategories = buyerCollectorAggregator.getCategories(buyerId);
		
		// THEN
		assertEquals(4, aggregatedCategories.size());
		assertNotNull(buyerCollectorAggregator.getCategoryPurchaseCount(buyerId));
	}
	
	@Test
	public void aggregateCategoryPurchaseCount() {
		// GIVEN
		Long buyerId = 1L;		
		List<Category> categories = CategoriesBuilder.create()
				.addCategory("MLB1571","MLB1572","MLB1573","MLB1571","MLB1574","MLB1571","MLB1573")
				.addName("MLB1571","MLB1572","MLB1573","MLB1571","MLB1574","MLB1571","MLB1573")
				.build();

		// GIVEN - buyer category addition
		buyerCollectorAggregator.add(buyerId, categories);
		
		// WHEN
		Multiset<Category> categoryPurchaseCountMap = buyerCollectorAggregator.getCategoryPurchaseCount(buyerId);
		
		// THEN
		assertEquals(3, categoryPurchaseCountMap.count(new Category("MLB1571")));
		assertEquals(1, categoryPurchaseCountMap.count(new Category("MLB1572")));
		assertEquals(2, categoryPurchaseCountMap.count(new Category("MLB1573")));
		assertEquals(1, categoryPurchaseCountMap.count(new Category("MLB1574")));
		assertNotNull(buyerCollectorAggregator.getCategories(buyerId));
	}
	
	@Test
	public void aggregateTwiceForSameBuyer() {
		// GIVEN
		Long buyerId = 1L;		
		List<Category> categories = CategoriesBuilder.create()
				.addCategory("MLB1571","MLB1572","MLB1571","MLB1571")
				.build();
		List<Category> otherCategories = CategoriesBuilder.create()
				.addCategory("MLB1576", "MLB1577", "MLB1576")
				.build();
		
		// GIVEN - buyer category addition
		buyerCollectorAggregator.add(buyerId, categories);
		buyerCollectorAggregator.add(buyerId, otherCategories);
		
		// WHEN
		Multiset<Category> categoryPurchaseCountMap = buyerCollectorAggregator.getCategoryPurchaseCount(buyerId);
		
		// THEN
		assertEquals(2, categoryPurchaseCountMap.count(new Category("MLB1576")));
		assertEquals(1, categoryPurchaseCountMap.count(new Category("MLB1577")));
		assertEquals(3, categoryPurchaseCountMap.count(new Category("MLB1571")));
		assertEquals(1, categoryPurchaseCountMap.count(new Category("MLB1572")));
		assertEquals(4, buyerCollectorAggregator.getCategories(buyerId).size());
	}
	
	@Test // TODO: parameterized test for different thresholds
	public void isCollectorByThreshold() throws Exception {
		// GIVEN
		Long buyerId = 1L;		
		Integer threshold = 3;
		List<Category> categoriesOne = CategoriesBuilder.create()
				.addCategory("MLB1571","MLB1572","MLB1573","MLB1571","MLB1574","MLB1571","MLB1573")
				.addName("MLB1571","MLB1572","MLB1573","MLB1571","MLB1574","MLB1571","MLB1573")
				.build();
		// GIVEN
		int expectedSize = 1;
		String expectedCategory = "MLB1571";

		// GIVEN - buyer category addition
		buyerCollectorAggregator.add(buyerId, categoriesOne);
		
		// WHEN
		Collection<Category> collectorCategories = buyerCollectorAggregator.getCollectionKinds(buyerId, threshold);
		
		assertEquals(expectedSize, collectorCategories.size());
		assertEquals(expectedCategory, collectorCategories.iterator().next().getId());
	}
	
	@Test
	public void categoriesCollectorOrderedByDescendingCount() throws Exception {
		// GIVEN
		int expectedSize = 2;
		Long buyerId = 1L;		
		Integer threshold = 2;
		List<Category> categoriesOne = CategoriesBuilder.create()
				.addCategory("MLB1571","MLB1572","MLB1573","MLB1571","MLB1573","MLB1571","MLB1573","MLB1573")
				.addName("MLB1571","MLB1572","MLB1573","MLB1571","MLB1573","MLB1571","MLB1573","MLB1573")
				.build();

		// GIVEN - the first one
		String expectedMainCategory = "MLB1573";
		int expectedMainCategoryIndex = 0;
		// GIVEN - the second one
		String expectedSecondCategory = "MLB1571";
		int expectedSecondCategoryIndex = 1;

		// GIVEN - buyer category addition
		buyerCollectorAggregator.add(buyerId, categoriesOne);
		
		// WHEN
		Collection<Category> collectorCategories = buyerCollectorAggregator.getCollectionKinds(buyerId, threshold);

		// THEN
		assertNotNull(collectorCategories);
		assertEquals(expectedSize, collectorCategories.size());
		
		List<Category> categoriesList = collectorCategories.stream().collect(Collectors.toList());
		assertEquals(expectedMainCategory, categoriesList.get(expectedMainCategoryIndex).getName());
		assertEquals(expectedSecondCategory, categoriesList.get(expectedSecondCategoryIndex).getName());
	}

	@Test
	public void categoriesAmount() {
        // GIVEN
        Integer mlb1000Amount = 1;
        Integer mlb1001Amount = 3;
        Integer mlb1002Amount = 2;
        Integer mlb1003Amount = 2;

        List<CategoryAmountDTO> categoryAmountDTOs = new ArrayList<>();
        List<Category> categories = createCagetories();

		// WHEN
        buyerCollectorAggregator.addCategoryAmount(categoryAmountDTOs, categories);

        // THEN
        assertEquals(mlb1000Amount, categoryAmountDTOs.stream().filter(cat -> "MLB1000".equals(cat.getCategory().getId()))
                            .findFirst().get().getAmount());
        assertEquals(mlb1001Amount, categoryAmountDTOs.stream().filter(cat -> "MLB1001".equals(cat.getCategory().getId()))
                .findFirst().get().getAmount());
        assertEquals(mlb1002Amount, categoryAmountDTOs.stream().filter(cat -> "MLB1002".equals(cat.getCategory().getId()))
                .findFirst().get().getAmount());
        assertEquals(mlb1003Amount, categoryAmountDTOs.stream().filter(cat -> "MLB1003".equals(cat.getCategory().getId()))
                .findFirst().get().getAmount());
	}

    private List<Category> createCagetories() {
        return CategoriesBuilder.create()
                .addCategory("MLB1000","MLB1001","MLB1002","MLB1001","MLB1003","MLB1001","MLB1003","MLB1002")
                .addName("Eletrônicos, Áudio e Vídeo","Informática","Outros","Informática","Celular","Informática","Celular","Outros")
                .build();
    }

    private static class CategoriesBuilder {
		
		private static List<Category> categories;

		private CategoriesBuilder(){}

		private static CategoriesBuilder create() {
			categories = new ArrayList<>();
			return new CategoriesBuilder();
		}
		
		CategoriesBuilder addCategory(String... categoryIds) {
			for (String categoryId : categoryIds) {
				Category category = new Category();
				category.setId(categoryId);
				categories.add(category);
			}
			return this;
		}
		
		public CategoriesBuilder addName(String... categoryNames) {
			int index = 0;
			for (String categoryName : categoryNames) {
				Category category = categories.get(index);
				category.setName(categoryName);
				index++;
			}
			return this;
		}
		
		List<Category> build() {
			return categories;
		}
	}
}
