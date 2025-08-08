package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class Order {
    private List<String> ingredients;
    public Order setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
        return this;
    }
}