package br.com.mls.mltracking.service;

import br.com.mls.mltracking.dto.CategoryAmountDTO;
import br.com.mls.mltracking.entity.BuyerCollector;
import br.com.mls.mltracking.entity.Item;
import br.com.mls.mltracking.entity.Question;
import br.com.mls.mltracking.service.annotation.MeliAuthentication;
import br.com.mls.mltracking.vo.Category;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
public class BuyerCollectorService {

    // TODO: this must be update at runtime
    private static final Integer THRESHOLD = 1;

    public static final String QUESTION_STATUS_ANSWERED = "ANSWERED";

    @Autowired
    private BuyerCollectorAggregator buyerCollectorAggregator;

    @Autowired
    private MeliIntegration meliIntegration;

    @SuppressWarnings({ "serial" })
    public Collection<BuyerCollector> getHistoryCollectorBuyers() {
        Collection<BuyerCollector> buyerCollectors = Lists.newArrayList();

        List<Question> questions = meliIntegration.getReceivedQuestions();

        Stream<Question> fieldStream = questions.stream();
        Iterator<Question> questionIterator = fieldStream.filter(json -> json.getStatus().equals(QUESTION_STATUS_ANSWERED)).iterator();
        while (questionIterator.hasNext()) {
            Question question = questionIterator.next();
            // User bought items
            Long buyerId = question.getBuyer().getId();
            List<Item> buyerItems = getBuyerItems(buyerId);
            aggregateCategories(buyerId, buyerItems);
            // TODO
            System.out.println("Buyer: " + buyerId + " = " + buyerCollectorAggregator.getCategoryPurchaseCount(buyerId));
            Collection<Category> collectionKinds = buyerCollectorAggregator.getCollectionKinds(buyerId, THRESHOLD);
            if (!collectionKinds.isEmpty()) {
                BuyerCollector buyerCollector = new BuyerCollector();
                buyerCollector.setMainCategory(collectionKinds.iterator().next());
                buyerCollectors.add(buyerCollector);
            }
        }
        return buyerCollectors;
    }

    @SuppressWarnings("serial")
    private void aggregateCategories(Long buyerId, List<Item> buyerItems) {
        for (Item buyerItem : buyerItems) {
            String itemId = buyerItem.getId();
            Item item = meliIntegration.getItemById(itemId);
            String categoryId = item.getCategoryId();
            // Get item category
            List<Category> categories = meliIntegration.getCategoriesById(categoryId);
            buyerCollectorAggregator.add(buyerId, categories);
        }
    }

    @SuppressWarnings({ "serial", "unchecked" })
    @MeliAuthentication
    private List<Item> getBuyerItems(Long buyerId) {
        List<Map<String, Object>> feedbackList;
        feedbackList = meliIntegration.getFeedbackResults(buyerId);
        return feedbackList.stream().map(userFeedbacksMap -> {
            Item item = new Item();
            Map<String, Object> feedbackItemMap = (Map<String, Object>) userFeedbacksMap.get("item");
            item.setId(feedbackItemMap.get("id").toString());
            item.setDescription(feedbackItemMap.get("title").toString());
            return item;
        }).collect(toList());
    }

    public Collection<Category> getCategories(Long buyerId, String status) {
        List<Item> buyerItems = getBuyerItems(buyerId);
        aggregateCategories(buyerId, buyerItems);
        return buyerCollectorAggregator.getCollectionKinds(buyerId, THRESHOLD);
    }

    @MeliAuthentication
    public Collection<CategoryAmountDTO> getCategoriesAmount(Long buyerId) {
        List<CategoryAmountDTO> categoryAmountDTOs = Lists.newArrayList();
        List<Item> buyerItems = getBuyerItems(buyerId);
        buyerItems.stream().forEach(item -> {
            Item categoryItem = meliIntegration.getItemById(item.getId());
            List<Category> categories = meliIntegration.getCategoriesById(categoryItem.getCategoryId());
            buyerCollectorAggregator.addCategoryAmount(categoryAmountDTOs, categories);
        });
        return categoryAmountDTOs;
    }

}
