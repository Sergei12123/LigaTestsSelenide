package at.model.enums;

import lombok.Getter;

public enum SubCategory {
    OUT_LEARNING("Внешнее обучение"),
    TRANSFER_BETWEEN_BLOCKS("Перевод между блоками");
    @Getter
    private final String subType;

    <T extends String> SubCategory(T subType) {
        this.subType = subType;
    }
}
