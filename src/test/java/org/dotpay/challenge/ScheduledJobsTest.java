package org.dotpay.challenge;

import static org.mockito.Mockito.when;

import org.dotpay.challenge.services.ScheduledOperations;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@AutoConfigureWebMvc
public class ScheduledJobsTest {
    @MockBean
    private ScheduledOperations scheduledOperations;

    @Test
    public void  runScheduledJobs() {
        run12amJobs();
        run3AmJobs();
    }

    void run12amJobs() {
        when(scheduledOperations.transactionAnalysisOperation()).thenReturn(true);
    }
    
    void run3AmJobs() {
        when(scheduledOperations.summarizeTransactions()).thenReturn(true);
    }
}
