package com.example.springbootecommerce.Controller;

import com.example.springbootecommerce.Model.Product;
import com.example.springbootecommerce.SpringBootEcommerceApplication;
import com.example.springbootecommerce.SpringBootEcommerceApplicationTests;
import com.example.springbootecommerce.Service.ProductServiceTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringBootEcommerceApplicationTests.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductControllerTest {
    // For mocking the web layer
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext productContext;
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(productContext).build();
    }

    public static String asJson(final Object object) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            final String jsonContent = objectMapper.writeValueAsString(object);
            System.out.println(jsonContent);
            return jsonContent;
        }catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void verifyGetProductById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/products/get/658016ed4df7bb26e3dc43a3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("658016ed4df7bb26e3dc43a3"))
                .andDo(print());
    }

    @Test
    public void verfiySaveProduct_EXCEPTION() throws Exception{
        Product product = new Product(new ObjectId("658016ed4df7bb26e3dc43a3"), "Chips", "snack",10);

        mockMvc.perform(MockMvcRequestBuilders.post("/products/add-product")
                        .content(asJson(product))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.message").value("PAYLOAD MALFORMED. OBJECT ID MUST NOT BE DEFINED"))
                .andDo(print());
    }



    @Test
    public void verifyUpdateProduct() throws Exception{
        Product product = new Product(new ObjectId("658016ed4df7bb26e3dc43a3"), "lays", "snack",20);

        mockMvc.perform(MockMvcRequestBuilders.patch("/products/update")
                        .content(asJson(product))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value("658016ed4df7bb26e3dc43a3"))
                .andExpect(jsonPath("$.name").value("lays"))
                .andExpect(jsonPath("$.description").value("snack"))
                .andExpect(jsonPath("$.price").value(20))
                .andDo(print());
    }

    @Test
    public void verifyUpdateProduct_EXCEPTION() throws Exception{
        Product product = new Product(new ObjectId("658016ed4df7bb26e3dc43a3"), "chips", "snack",20);

        mockMvc.perform(MockMvcRequestBuilders.patch("/products/update")
                        .content(asJson(product))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.message").value("Product DOESN'T EXISTS"))
                .andDo(print());
    }



    @Test
    public void verifyDeleteById() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/delete/6580189a55cec04f3cdf3d4d")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Successfully Deleted !!"))
                .andDo(print());
    }

    @Test
    public void verifyDeleteById_EXCEPTION() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/delete/6580189a55cec04f3cdf3d4d2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.message").value("Product DOESN'T Exists"))
                .andDo(print());
    }
}
