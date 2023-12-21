package com.example.springbootecommerce.Service;

import com.example.springbootecommerce.ExceptionHandler.CategoryException;
import com.example.springbootecommerce.ExceptionHandler.ProductException;
import com.example.springbootecommerce.Model.Category;
import com.example.springbootecommerce.Repository.CategoryRepo;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private CategoryRepo categoryRepo;

    public CategoryService(CategoryRepo categoryRepo) { this.categoryRepo = categoryRepo;}

    // add category
    public Category createCategory(Category category) {
        return categoryRepo.save(category);
    }

    //get category by id
    public Category getCategoryById(ObjectId id) throws CategoryException{
        Optional<Category> optCategory = categoryRepo.findById(id);
        if(optCategory.isEmpty() || optCategory == null) {
            throw new CategoryException("Category doesn't exist");
        }
        return optCategory.orElseGet(optCategory::get);
    }

    //get category by any field
    public List<Category> getCategoryByField(String field, String value) throws CategoryException {
        List<Category> listCategory = switch (field) {
            case "id" -> categoryRepo.findByID(new ObjectId(value));
            case "name" -> categoryRepo.findByName(value);
            case "description" -> categoryRepo.findByDesc(value);
            default -> throw new CategoryException("incorrect field");
        };

        if(listCategory.isEmpty()) {
            throw new CategoryException("Category doesn't exist");
        }
        return listCategory;
    }


    //get all categories
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    // update Category
    public String updateCategory(String id, Category category) {
        ObjectId objectId = new ObjectId(id);
        Optional<Category> optCategory = categoryRepo.findById(objectId);
        if(optCategory.isEmpty()) {
            throw new RuntimeException("given category doesn't exist");
        }
        Category categoryRec = optCategory.get();
        if (category.getName() != null)
            categoryRec.setName(category.getName());
        if (category.getDescription() != null)
            categoryRec.setDescription(category.getDescription());

        categoryRepo.save(categoryRec);
        return "{" +
                "\"message\":"+"Successfully updated category\",\n"+
                "\"data\":"+categoryRec+",\n"+
                "}";
    }

    // delete category by id
    public String deleteCategoryById(ObjectId id) {
        Optional<Category> optCategory = categoryRepo.findById(id);
        if(optCategory.isEmpty()) {
            throw new RuntimeException("Category id " + id + " doesn't exist");
        }
        categoryRepo.deleteById(id);
        return "{" +
                "\"message\":"+"Successfully deleted category\",\n"+
                "\"id\":"+id+",\n"+
                "}";
    }

    
}
