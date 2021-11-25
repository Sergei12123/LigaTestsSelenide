package at.model;

import at.model.enums.Category;
import at.model.enums.SubCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    private Category category;
    private SubCategory subCategory;
    private String number;
    private String status;
    private String candidateName;
    private String date;
}
