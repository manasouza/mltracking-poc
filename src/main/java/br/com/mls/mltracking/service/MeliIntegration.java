package br.com.mls.mltracking.service;

import br.com.mls.mltracking.entity.Item;
import br.com.mls.mltracking.entity.Order;
import br.com.mls.mltracking.entity.Question;
import br.com.mls.mltracking.entity.Seller;
import br.com.mls.mltracking.exception.ResourceNotFoundException;
import br.com.mls.mltracking.util.LoginData;
import br.com.mls.mltracking.util.ServiceLocator;
import br.com.mls.mltracking.vo.Category;
import io.swagger.client.ApiException;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.CategoryResponse;
import io.swagger.client.model.CategorySettings;
import io.swagger.client.model.Descriptions;
import io.swagger.client.model.ItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by manasses on 10/8/16.
 */
@Component
public class MeliIntegration {

    private static final Integer DEFAULT_OFFSET = 0;
    private static final Integer DEFAULT_LIMIT = 50;

    @Autowired
    private LoginData loginData;

    // TODO: remove ServiceLocator after current changes analysis if does not make sense to use it
    @Autowired
    private ServiceLocator serviceLocator;

    private DefaultApi api;

    public MeliIntegration() {
        api = new DefaultApi();
    }

    Item getItemById(String itemId) {
        ItemResponse buyerItemResponse;
        try {
            buyerItemResponse = api.itemsItemIdGet(itemId, loginData.getAccessToken());
        } catch (ApiException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
        final Item item = new Item();
        item.setId(buyerItemResponse.getId());
        item.setDescription(buyerItemResponse.getDescriptions().stream()
            .map(Descriptions::getId)
            .collect(Collectors.joining(", ")));
        item.setCategoryId(buyerItemResponse.getCategoryId());
        return item;
    }

    List<Category> getCategoriesById(String categoryId) {
        CategoryResponse buyerItemCategoryResponse;
        try {
            buyerItemCategoryResponse = api.categoriesCategoryIdGet(categoryId);
        } catch (ApiException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
        // TODO: how to convert to List<Category>
        final CategorySettings settings = buyerItemCategoryResponse.getSettings();
        return null;
    }

    /**
     * TODO: Validate if api.ordersSearchGet really attends
     * @param buyerId
     * @return
     */
    List<Map<String, Object>> getFeedbackResults(Long buyerId) {
        Object feedbacksResponse = null;
        try {
            feedbacksResponse = api.ordersSearchGet(loginData.getAccessToken(), buyerId.intValue(),
                null, DEFAULT_OFFSET, DEFAULT_LIMIT);
        } catch (ApiException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
        // TODO: analyse Object type response
        System.out.println(feedbacksResponse);
        return null;
    }

    /**
     * TODO: Validate if api.defaultGet really attends
     * @return
     */
    List<Question> getReceivedQuestions() {
        Object questionsResponse;
        try {
            questionsResponse = api.defaultGet("/my/received_questions/search");
        } catch (ApiException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
        // TODO: analyse Object type response
        System.out.println(questionsResponse);
        return null;
    }

    public Collection<Order> getSellerOrders(Seller seller) {
        try {
            final Object ordersResponse =
                api.ordersSearchGet(loginData.getAccessToken(), null, seller.getId().intValue(),
                    DEFAULT_OFFSET, DEFAULT_LIMIT);
            // TODO: analyse Object type response
            System.out.println(ordersResponse);
        } catch (ApiException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
        return null;
    }
}
