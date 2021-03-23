package br.com.mls.mltracking.controller;

import br.com.mls.mltracking.builder.CategoryDataBuilder;
import br.com.mls.mltracking.dto.CategoryAmountDTO;
import br.com.mls.mltracking.service.BuyerCollectorService;
import br.com.mls.mltracking.util.LoginData;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collection;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyCollection;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BuyerCollectorController.class)
public class BuyerCollectorTest {

	private static String meliClientId;
	private static String meliClientSecret;

	@BeforeClass
	public static void securityArgs() {
		meliClientSecret = System.getProperty("meliClientSecret");
		meliClientId = System.getProperty("meliClientId");
	}

	@MockBean
	private BuyerCollectorService buyerCollectorServiceMock;

	@MockBean
	private LoginData loginData;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void getBuyerCollectorCategories() throws Exception {
		// GIVEN
		Long buyerId = 83482535L;

		// GIVEN credentials
		// TODO: Login interceptor is been called and integrates with MELI OAuth
		given(loginData.getClientId()).willReturn(Long.valueOf(meliClientId));
		given(loginData.getClientSecret()).willReturn(meliClientSecret);

		// GIVEN buyer service mock
		given(buyerCollectorServiceMock.getCategories(buyerId, null))
			.willReturn(anyCollection());


		mockMvc.perform(get("/buyers/{id}/items/categories", buyerId)
				.header("meliClientId", meliClientId)
				.header("meliClientSecret", meliClientSecret)
				.param("status", "blabla"))
				.andExpect(status().isOk());
	}

	@Test
	@Ignore
	public void testStatusUnanswered() {
		String path = "/buyers/1/items/categories?status=unanswered";
	}

	@Test
	public void getBuyerCollectorCategoriesAmount() throws Exception {
		// GIVEN
		Long buyerId = 83482535L;
		String categoryId = "MLB4915";
        String categoryName = "Eletrônicos";
        Integer amount = 100;

		// GIVEN credentials
		// TODO: Login interceptor is been called and integrates with MELI OAuth
		given(loginData.getClientId()).willReturn(Long.valueOf(meliClientId));
		given(loginData.getClientSecret()).willReturn(meliClientSecret);

		// GIVEN buyer service mock
		given(buyerCollectorServiceMock.getCategoriesAmount(buyerId))
				.willReturn(createCategoriesAmountResponse(categoryId, categoryName));

        mockMvc.perform(get("/buyers/{id}/items/categories/amount", buyerId)
                .header("meliClientId", meliClientId)
                .header("meliClientSecret", meliClientSecret))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.categoryAmount[0].amount").value(equalTo(amount)))
                .andExpect(jsonPath("$.categoryAmount[0].category.id").value(equalTo(categoryId)))
                .andExpect(jsonPath("$.categoryAmount[0].category.name").value(equalTo(categoryName)));
    }

	private Collection<CategoryAmountDTO> createCategoriesAmountResponse(String categoryId, String categoryName) {
        CategoryAmountDTO categoryAmountDTO = CategoryDataBuilder.create().amount(100).onCategory(categoryId, categoryName).build();
        return Lists.newArrayList(categoryAmountDTO);
    }
}
