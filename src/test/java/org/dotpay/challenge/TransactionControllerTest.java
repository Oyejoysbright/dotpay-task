package org.dotpay.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.dotpay.challenge.services.TransactionService;
import org.dotpay.challenge.utils.MockData;
import org.dotpay.challenge.utils.ServerResponse.ResponseMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();
    @MockBean
    private TransactionService service;


    @Test
    public void performDataInserting() throws Exception {
        addCustomers();
        requestIntraTransfer();
        requestInterTransfer();
    }
    
    @Test
    public void performDataRetrieving() throws Exception {
        getTransactions();
        getInsufficientFundTransactions();
        getSuccessfulTransactions();
        getFailedTransactions();
        getDateRangedTransactions();
        getTransactionSummary();
        getTransactionSummaryWithDate();
    }

    void addCustomers() throws Exception {
        when(service.addCustomers(MockData.getCustomers())).thenReturn(true);
    }

    void requestIntraTransfer() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/transactions/request")
            .content(mapper.writeValueAsString(MockData.getIntraTransferRequestInstance())).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    void requestInterTransfer() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/transactions/request")
            .content(mapper.writeValueAsString(MockData.getInterTransferRequestInstance())).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andDo(MockMvcResultHandlers.print()).andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    void getTransactions() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/transactions");
        String contentAsString = mockMvc.perform(requestBuilder).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        if (!contentAsString.isBlank()) {
            ResponseMessage<?> contentAsClass = mapper.readValue(contentAsString, ResponseMessage.class);
            assertEquals(false, contentAsClass.getHasError());
            assertEquals(anyList(), contentAsClass.getData());            
        }
    }

    void getSuccessfulTransactions() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/transactions")
                .param("status", "SUCCESSFUL");
        String contentAsString = mockMvc.perform(requestBuilder).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        if (!contentAsString.isBlank()) {
            ResponseMessage<?> contentAsClass = mapper.readValue(contentAsString, ResponseMessage.class);
            assertEquals(false, contentAsClass.getHasError());
            assertEquals(anyList(), contentAsClass.getData());            
        }
    }

    void getInsufficientFundTransactions() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/transactions")
                .param("status", "INSUFFICIENT_FUND");
        String contentAsString = mockMvc.perform(requestBuilder).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        if (!contentAsString.isBlank()) {
            ResponseMessage<?> contentAsClass = mapper.readValue(contentAsString, ResponseMessage.class);
            assertEquals(false, contentAsClass.getHasError());
            assertEquals(anyList(), contentAsClass.getData());            
        }
    }

    void getFailedTransactions() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/transactions")
                .param("status", "FAILED");
        String contentAsString = mockMvc.perform(requestBuilder).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        if (!contentAsString.isBlank()) {
            ResponseMessage<?> contentAsClass = mapper.readValue(contentAsString, ResponseMessage.class);
            assertEquals(false, contentAsClass.getHasError());
            assertEquals(anyList(), contentAsClass.getData());            
        }
    }

    void getDateRangedTransactions() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/transactions")
                .param("startDate", "2022-03-23 00:00:00").param("endDate", "2022-03-24 00:00:00");
        String contentAsString = mockMvc.perform(requestBuilder).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        if (!contentAsString.isBlank()) {
            ResponseMessage<?> contentAsClass = mapper.readValue(contentAsString, ResponseMessage.class);
            assertEquals(false, contentAsClass.getHasError());
            assertEquals(anyList(), contentAsClass.getData());            
        }
    }

    void getTransactionSummary() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/transactions/summary");
        mockMvc.perform(requestBuilder).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

    void getTransactionSummaryWithDate() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/transactions/summary").param("date", "2022-03-24 00:00:00");
        mockMvc.perform(requestBuilder).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }
}