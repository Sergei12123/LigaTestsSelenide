package at.model.enums;

import lombok.Getter;

public enum Category {
    REQUEST("Заявки");
    @Getter
    private final String type;

    <T extends String> Category(T type)
    {
        this.type = type;
    }
}
