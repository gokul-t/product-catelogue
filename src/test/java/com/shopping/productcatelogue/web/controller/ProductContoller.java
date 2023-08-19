package com.shopping.productcatelogue.web.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductContoller {
    private final MockMvc mockMvc;

    @Autowired
    public ProductContoller(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void shouldReturnProductList() throws Exception {
        mockMvc.perform(
                get("/api/products")
                        .param("pageNumber", "0")
                        .param("pageSize", "10"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size", is(10)));
    }

    @Test
    public void shouldThrowExceptionForNotValidProductId() throws Exception {
        mockMvc.perform(get("/api/products/{productId}", "hello")).andDo(print()).andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/products/{productId}", "100")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnProduct() throws Exception {
        mockMvc.perform(get("/api/products/{productId}", 1)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
