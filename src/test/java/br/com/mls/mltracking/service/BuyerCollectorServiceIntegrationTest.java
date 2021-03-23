package br.com.mls.mltracking.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

import br.com.mls.mltracking.dto.CategoryAmountDTO;
import com.mercadolibre.sdk.MeliException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.mls.mltracking.entity.BuyerCollector;
import br.com.mls.mltracking.util.LoginData;

/**
 * Story 1 Integration Test: Identify willing buyer as a kind of collector
 * @author manasouza
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BuyerCollectorServiceIntegrationTest {

	public static Long clientId;
	public static String clientSecret;
	public static String refreshToken;
	public static String accessToken;

	@Autowired
	private BuyerCollectorService buyerCollectorService;

	@Autowired
	private LoginData loginData;
	
	@BeforeClass
	public static void setUpTestParams() {
		clientId = Long.valueOf(System.getProperty("meliClientId"));
		clientSecret = System.getProperty("meliClientSecret");
	}

	@Before
	public void setUp() {
		loginData.setClientId(clientId);
		loginData.setClientSecret(clientSecret);
	}
	
	@Test
	@Ignore("Must @RunWith(SpringJUnit4ClassRunner.class)")
	public void answeredQuestions() throws Exception {
		Collection<BuyerCollector> historyCollectorBuyers = buyerCollectorService.getHistoryCollectorBuyers();
		assertNotNull(historyCollectorBuyers);
		System.out.println(historyCollectorBuyers);
//		assertNotNull("Buyer category purchase count must not be null", buyerCollectorAggregator.getCategoryPurchaseCount(buyerId));
//		assertNotNull("Buyer categories must not be null", buyerCollectorAggregator.getCategories(buyerId));
	}

	@Test
	public void categoriesAmount() throws IOException, MeliException {
		// GIVEN
		Long buyerId = 83482535L;

		Collection<CategoryAmountDTO> categoriesAmount = buyerCollectorService.getCategoriesAmount(buyerId);

		assertFalse("Categories amount should not be empty", categoriesAmount.isEmpty());
	}

}
