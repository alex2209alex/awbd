package ro.unibuc.fmi.awbd.service.product;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.awbd.common.exception.BadRequestException;
import ro.unibuc.fmi.awbd.common.exception.ForbiddenException;
import ro.unibuc.fmi.awbd.common.exception.NotFoundException;
import ro.unibuc.fmi.awbd.common.model.Page;
import ro.unibuc.fmi.awbd.common.model.PageRequest;
import ro.unibuc.fmi.awbd.common.utils.ErrorMessageUtils;
import ro.unibuc.fmi.awbd.controller.models.*;
import ro.unibuc.fmi.awbd.domain.ingredient.model.IngredientProductAssociation;
import ro.unibuc.fmi.awbd.domain.ingredient.model.IngredientProductAssociationId;
import ro.unibuc.fmi.awbd.domain.ingredient.repository.IngredientRepository;
import ro.unibuc.fmi.awbd.domain.product.repository.ProductRepository;
import ro.unibuc.fmi.awbd.domain.product.repository.ProductSearchRepository;
import ro.unibuc.fmi.awbd.service.product.mapper.ProductMapper;
import ro.unibuc.fmi.awbd.service.product.model.ProductFilter;
import ro.unibuc.fmi.awbd.service.product.model.ProductPageElementDetails;
import ro.unibuc.fmi.awbd.service.user.UserInformationService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductSearchRepository productSearchRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final IngredientRepository ingredientRepository;
    private final UserInformationService userInformationService;

    @Transactional(readOnly = true)
    public ProductsPageDto getProductsPage(PageRequest<ProductFilter> pageRequest) {
        userInformationService.ensureCurrentUserIsRestaurantAdminOrClient();
        Page<ProductPageElementDetails> page = productSearchRepository.getProductPage(pageRequest);
        return productMapper.mapToProductsPageDto(page);
    }

    @Transactional(readOnly = true)
    public List<ProductSearchDetailsDto> getProducts() {
        userInformationService.ensureCurrentUserIsRestaurantAdmin();
        val products = productRepository.findAllByOrderByName();
        return productMapper.mapToProductSearchDetailsDtos(products);
    }

    @Transactional(readOnly = true)
    public ProductDetailsDto getProductDetails(Long productId) {
        userInformationService.ensureCurrentUserIsRestaurantAdminOrClient();
        val product = productRepository.findById(productId).orElseThrow(() ->
                new NotFoundException(String.format(ErrorMessageUtils.PRODUCT_NOT_FOUND, productId))
        );
        return productMapper.mapToProductDetailsDto(product);
    }

    @Transactional
    public void createProduct(ProductCreationDto productCreationDto) {
        userInformationService.ensureCurrentUserIsRestaurantAdmin();
        val ingredientIds = productCreationDto.getIngredients()
                .stream()
                .map(ProductIngredientCreationDto::getId)
                .collect(Collectors.toSet());
        if (ingredientIds.size() != productCreationDto.getIngredients().size()) {
            throw new BadRequestException(ErrorMessageUtils.DUPLICATE_INGREDIENTS_PRESENT);
        }
        val ingredients = ingredientRepository.findAllByIdIn(ingredientIds);
        if (ingredients.size() != productCreationDto.getIngredients().size()) {
            throw new NotFoundException(ErrorMessageUtils.INGREDIENTS_NOT_FOUND);
        }
        val product = productMapper.mapToProduct(productCreationDto);
        val persistedProduct = productRepository.save(product);
        persistedProduct.setIngredientProductAssociations(new ArrayList<>());
        productCreationDto.getIngredients().forEach(productIngredientCreationDto -> {
            val ingredientProductAssociationId = IngredientProductAssociationId
                    .builder()
                    .productId(persistedProduct.getId())
                    .ingredientId(productIngredientCreationDto.getId())
                    .build();
            val ingredientProductAssociation = IngredientProductAssociation.builder()
                    .id(ingredientProductAssociationId)
                    .quantity(productIngredientCreationDto.getQuantity())
                    .build();
            persistedProduct.getIngredientProductAssociations().add(ingredientProductAssociation);
        });
    }

    @Transactional
    public void updateProduct(Long productId, ProductUpdateDto productUpdateDto) {
        userInformationService.ensureCurrentUserIsRestaurantAdmin();
        val ingredientIds = productUpdateDto.getIngredients()
                .stream()
                .map(ProductIngredientUpdateDto::getId)
                .collect(Collectors.toSet());
        if (ingredientIds.size() != productUpdateDto.getIngredients().size()) {
            throw new BadRequestException(ErrorMessageUtils.DUPLICATE_INGREDIENTS_PRESENT);
        }
        val ingredients = ingredientRepository.findAllByIdIn(ingredientIds);
        if (ingredients.size() != productUpdateDto.getIngredients().size()) {
            throw new NotFoundException(ErrorMessageUtils.INGREDIENTS_NOT_FOUND);
        }
        val product = productRepository.findById(productId).orElseThrow(() ->
                new NotFoundException(String.format(ErrorMessageUtils.PRODUCER_NOT_FOUND, productId))
        );
        productMapper.mergeToProduct(product, productUpdateDto);
        val oldIngredientProductAssociationsMap = product.getIngredientProductAssociations().stream()
                .collect(Collectors.toMap(IngredientProductAssociation::getId, Function.identity()));
        product.getIngredientProductAssociations().clear();
        productUpdateDto.getIngredients().forEach(productIngredientUpdateDto -> {
            val ingredientProductAssociationId = IngredientProductAssociationId
                    .builder()
                    .productId(product.getId())
                    .ingredientId(productIngredientUpdateDto.getId())
                    .build();
            IngredientProductAssociation ingredientProductAssociation;
            if (oldIngredientProductAssociationsMap.containsKey(ingredientProductAssociationId)) {
                ingredientProductAssociation = oldIngredientProductAssociationsMap.get(ingredientProductAssociationId);
                ingredientProductAssociation.setQuantity(productIngredientUpdateDto.getQuantity());
                oldIngredientProductAssociationsMap.remove(ingredientProductAssociationId);
            }
            else {
                ingredientProductAssociation = IngredientProductAssociation.builder()
                        .id(ingredientProductAssociationId)
                        .quantity(productIngredientUpdateDto.getQuantity())
                        .build();
            }
            product.getIngredientProductAssociations().add(ingredientProductAssociation);
        });
    }

    @Transactional
    public void deleteProduct(Long productId) {
        userInformationService.ensureCurrentUserIsRestaurantAdmin();
        val product = productRepository.findById(productId).orElseThrow(() ->
                new NotFoundException(String.format(ErrorMessageUtils.PRODUCER_NOT_FOUND, productId))
        );
        if (!product.getProductOnlineOrderAssociations().isEmpty()) {
            throw new ForbiddenException(String.format(ErrorMessageUtils.PRODUCT_HAS_DEPENDENCIES_AND_CANNOT_BE_DELETED, productId));
        }
        productRepository.delete(product);
    }
}
