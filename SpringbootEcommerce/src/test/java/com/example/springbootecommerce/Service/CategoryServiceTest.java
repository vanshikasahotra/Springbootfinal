package com.example.springbootecommerce.Service;

import com.example.springbootecommerce.ExceptionHandler.CategoryException;
import com.example.springbootecommerce.Model.Category;
import com.example.springbootecommerce.Repository.CategoryRepo;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RunWith(SpringJUnit4ClassRunner.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepo CategoryRepo;
    @InjectMocks
    private CategoryService CategoryService;
    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllCategories() {
        List<Category> CategoryList = new ArrayList<Category>();
        CategoryList.add(new Category(new ObjectId("6577e2dab2c0522674b97f39"), "Vivo","mobile"));
        CategoryList.add(new Category(new ObjectId("6577e2dab2c0512674b77f39"), "MacBook","laptop"));
        CategoryList.add(new Category(new ObjectId("6537e2dab2c0522674b97f29"), "Ipad","tablet"));

        //mocks
        when(CategoryRepo.findAll()).thenReturn(CategoryList);
        List<Category> CategoryResult = CategoryService.getAllCategories();

        assertEquals(3, CategoryResult.size());
    }

    @Test
    public void getCategoryById() throws CategoryException {
        List<Category> categoryList = new ArrayList<Category>();
        categoryList.add(new Category(new ObjectId("6577e2dab2c0522674b97f39"), "Oppo","mobile"));
        categoryList.add(new Category(new ObjectId("6577e2dab2c0512674b77f39"), "ThinkPad","laptop"));
        categoryList.add(new Category(new ObjectId("6537e2dab2c0522674b97f29"), "Iwatch","watch"));


        ObjectId id = new ObjectId("6577e2dab2c0522674b97f39");

        //mocks
        when(CategoryRepo.findById(id)).thenReturn(Optional.ofNullable(categoryList.get(categoryList.size()-3)));
        Category CategoryResult = CategoryService.getCategoryById(id);

        // assertions
        assertEquals(id, CategoryResult.getId());
        assertEquals("Oppo", CategoryResult.getName());
        assertEquals("mobile",CategoryResult.getDescription());
    }

    @Test
    public void saveCategory(){

        Category Category = new Category(new ObjectId("658013a4839663734de1b6a6"), "PS5", "console");

        when(CategoryRepo.save(Category)).thenReturn(Category);

        Category savedCategory = CategoryService.createCategory(Category);

        assertEquals(new ObjectId("658013a4839663734de1b6a6"), savedCategory.getId());
        assertEquals("PS5", savedCategory.getName());
        assertEquals("console", savedCategory.getDescription());
    }


    @Test
    public void deleteCategoryById(){

        Category category = new Category(new ObjectId("65801229839663734de1b6a4"), "Nintendo", "console");

        when(CategoryRepo.findById(category.getId())).thenReturn(Optional.ofNullable(category));

        CategoryService.deleteCategoryById(category.getId());
        verify(CategoryRepo, times(1)).deleteById(category.getId());
    }

}
