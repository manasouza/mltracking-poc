package br.com.mls.mltracking.service;

import br.com.mls.mltracking.exception.InternalServerErrorException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by manasses on 10/8/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class BuyerCollectorServiceIntegrationMockTest {

    @InjectMocks
    private BuyerCollectorService service;

    @Mock
    private MeliIntegration meliIntegration;

    @Test
    public void failureOnHistoryCollectorBuyersReceivedQuestions() throws IOException {
        // GIVEN
        when(meliIntegration.getReceivedQuestions()).thenThrow(new IOException("JSON error"));

        // WHEN
        try {
            service.getHistoryCollectorBuyers();
        } catch (Exception e) {
            // THEN
            assertThat(e, is(instanceOf(InternalServerErrorException.class)));
        }
    }
}
