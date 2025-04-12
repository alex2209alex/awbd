package ro.unibuc.fmi.awbd.domain.ingredient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.unibuc.fmi.awbd.domain.ingredient.model.Ingredient;

import java.util.List;
import java.util.Set;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findAllByOrderByName();

    List<Ingredient> findAllByIdIn(Set<Long> ids);
}
