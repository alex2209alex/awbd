package ro.unibuc.fmi.awbd.domain.ingredient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.unibuc.fmi.awbd.domain.ingredient.model.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
