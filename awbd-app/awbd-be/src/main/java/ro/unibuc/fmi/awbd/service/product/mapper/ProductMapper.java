package ro.unibuc.fmi.awbd.service.product.mapper;

import org.mapstruct.*;
import ro.unibuc.fmi.awbd.common.model.Page;
import ro.unibuc.fmi.awbd.controller.models.*;
import ro.unibuc.fmi.awbd.domain.ingredient.model.IngredientProductAssociation;
import ro.unibuc.fmi.awbd.domain.product.model.Product;
import ro.unibuc.fmi.awbd.service.product.model.ProductFilter;
import ro.unibuc.fmi.awbd.service.product.model.ProductPageElementDetails;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ProductMapper {
    ProductFilter mapToProductFilter(String name, String description);

    @Mapping(target = "pagination", source = "paginationInformation")
    ProductsPageDto mapToProductsPageDto(Page<ProductPageElementDetails> page);

    List<ProductSearchDetailsDto> mapToProductSearchDetailsDtos(List<Product> products);

    @Mapping(target = "calories", source = "ingredientProductAssociations", qualifiedByName = "mapToCalories")
    @Mapping(target = "ingredients", source = "ingredientProductAssociations")
    ProductDetailsDto mapToProductDetailsDto(Product product);

    @Mapping(target = "id", source = "id.ingredientId")
    @Mapping(target = "name", source = "ingredient.name")
    ProductIngredientDetailsDto mapToProductIngredientDetailsDto(IngredientProductAssociation ingredientProductAssociation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ingredientProductAssociations", ignore = true)
    @Mapping(target = "productOnlineOrderAssociations", ignore = true)
    @Mapping(target = "cooks", ignore = true)
    Product mapToProduct(ProductCreationDto productCreationDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ingredientProductAssociations", ignore = true)
    @Mapping(target = "productOnlineOrderAssociations", ignore = true)
    @Mapping(target = "cooks", ignore = true)
    void mergeToProduct(@MappingTarget Product product, ProductUpdateDto productUpdateDto);

    @Named("mapToCalories")
    default Double mapToCalories(List<IngredientProductAssociation> ingredientProductAssociations) {
        return ingredientProductAssociations.stream()
                .map(ingredientProductAssociation -> ingredientProductAssociation.getQuantity() * ingredientProductAssociation.getIngredient().getCalories())
                .reduce(Double::sum)
                .orElse(0.);
    }
}
