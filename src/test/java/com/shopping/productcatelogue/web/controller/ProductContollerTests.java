package com.shopping.productcatelogue.web.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.productcatelogue.model.ProductDTO;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductContollerTests {
    private final MockMvc mockMvc;

    @Autowired
    public ProductContollerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    @DisplayName("Save Product - ")
    @Nested
    class TestSaveProduct {
        @Test
        void shouldReturnBadRequest1() throws Exception {
            ProductDTO productDTO = ProductDTO.builder().build();
            mockMvc.perform(
                    post("/api/products").content(asJsonString(productDTO)).contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturnBadRequest2() throws Exception {
            ProductDTO productDTO = ProductDTO.builder().name("Chair").size("No").quantity(100).build();
            mockMvc.perform(
                    post("/api/products").content(asJsonString(productDTO)).contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturnProduct() throws Exception {
            ProductDTO productDTO = ProductDTO.builder().name("Chair").size("Small").quantity(100).build();
            mockMvc.perform(
                    post("/api/products").content(asJsonString(productDTO)).contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }
    }

    @DisplayName("Update Product - ")
    @Nested
    class TestUpdateProduct {
        @Test
        void shouldReturnBadRequest1() throws Exception {
            ProductDTO productDTO = ProductDTO.builder().build();
            mockMvc.perform(
                    put("/api/products/{productId}", 1).content(asJsonString(productDTO))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturnBadRequest2() throws Exception {
            ProductDTO productDTO = ProductDTO.builder().id(1000L).name("Chair").size("No").quantity(100).build();
            mockMvc.perform(
                    put("/api/products/{productId}", 1000).content(asJsonString(productDTO))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldUpdateProduct() throws Exception {
            ProductDTO productDTO = ProductDTO.builder().id(1L).name("Chair").size("Small").quantity(100).build();
            mockMvc.perform(
                    put("/api/products/{productId}", productDTO.getId()).content(asJsonString(productDTO))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }
    }

    @DisplayName("Delete Product - ")
    @Nested
    class TestDeleteProduct {
        @Test
        void shouldReturnBadRequest() throws Exception {
            mockMvc.perform(delete("/api/products/{productId}", "hello")).andDo(print())
                    .andExpect(status().isBadRequest());
            mockMvc.perform(delete("/api/products/{productId}", "100000")).andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldDeleteProduct() throws Exception {
            mockMvc.perform(delete("/api/products/{productId}", 101)).andDo(print()).andExpect(status().isNoContent());
        }
    }

    @DisplayName("Get Product - ")
    @Nested
    class TestGetProduct {
        @Test
        void shouldReturnBadRequest1() throws Exception {
            mockMvc.perform(get("/api/products/{productId}", "hello")).andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturnBadRequest2() throws Exception {
            mockMvc.perform(get("/api/products/{productId}", 100000)).andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturnBadRequest3() throws Exception {
            mockMvc.perform(get("/api/products/{productId}", -5)).andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturnProduct() throws Exception {
            mockMvc.perform(get("/api/products/{productId}", 1)).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
    }

    @DisplayName("List Products - ")
    @Nested
    class TestListProducts {
        @Test
        void shouldReturnBadRequest() throws Exception {
            mockMvc.perform(
                    get("/api/products")
                            .param("pageNumber", "-1")
                            .param("pageSize", "10"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
            mockMvc.perform(
                    get("/api/products")
                            .param("pageNumber", "0")
                            .param("pageSize", "-5"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturnProductList() throws Exception {
            mockMvc.perform(
                    get("/api/products")
                            .param("pageNumber", "0")
                            .param("pageSize", "10"))
                    .andDo(print()).andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.size", is(10)));
        }
    }

}
