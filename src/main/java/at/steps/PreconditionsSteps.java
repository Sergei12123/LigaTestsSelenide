package at.steps;

import at.model.Product;
import at.model.enums.Category;
import at.model.enums.SubCategory;
import at.parser.Context;

public class PreconditionsSteps {
    public static void existsProduct(Category category, SubCategory subCategory) {
        Product product = new Product();
        product.setCategory(category);
        product.setSubCategory(subCategory);
        Context.saveObject("Продукт", product);
    }
}
