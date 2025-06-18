package ro.unibuc.fmi.awbd.service.product;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.unibuc.fmi.awbd.common.exception.BadRequestException;
import ro.unibuc.fmi.awbd.common.exception.ForbiddenException;
import ro.unibuc.fmi.awbd.common.exception.NotFoundException;
import ro.unibuc.fmi.awbd.controller.models.*;
import ro.unibuc.fmi.awbd.domain.ingredient.model.IngredientProductAssociation;
import ro.unibuc.fmi.awbd.domain.ingredient.model.IngredientProductAssociationId;
import ro.unibuc.fmi.awbd.domain.ingredient.repository.IngredientRepository;
import ro.unibuc.fmi.awbd.domain.product.model.ProductOnlineOrderAssociation;
import ro.unibuc.fmi.awbd.domain.product.model.Product;
import ro.unibuc.fmi.awbd.domain.product.repository.ProductRepository;
import ro.unibuc.fmi.awbd.domain.product.repository.ProductSearchRepository;
import ro.unibuc.fmi.awbd.fixtures.IngredientFixtures;
import ro.unibuc.fmi.awbd.fixtures.ProductFixtures;
import ro.unibuc.fmi.awbd.service.product.mapper.ProductMapper;
import ro.unibuc.fmi.awbd.service.user.UserInformationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    ProductSearchRepository productSearchRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductMapper productMapper;

    @Mock
    IngredientRepository ingredientRepository;

    @Mock
    UserInformationService userInformationService;

    @InjectMocks
    ProductService productService;

    @Test
    void whenGetProductsPage_thenGetProductsPage() {
        val pageRequest = ProductFixtures.getPageRequestFixture();
        val page = ProductFixtures.getPageOfProductPageElementDetailsFixture();
        val productsPageDto = new ProductsPageDto();

        Mockito.when(productSearchRepository.getProductsPage(pageRequest)).thenReturn(page);
        Mockito.when(productMapper.mapToProductsPageDto(page)).thenReturn(productsPageDto);

        Assertions.assertEquals(productsPageDto, productService.getProductsPage(pageRequest));
    }

    @Test
    void whenGetProducts_thenGetProducts() {
        val products = List.of(ProductFixtures.getProductFixture());
        List<ProductSearchDetailsDto> productSearchDetailsDtos = List.of();

        Mockito.when(productRepository.findAllByOrderByName()).thenReturn(products);
        Mockito.when(productMapper.mapToProductSearchDetailsDtos(products)).thenReturn(productSearchDetailsDtos);

        Assertions.assertEquals(productSearchDetailsDtos, productService.getProducts());
    }

    @Test
    void givenNonExistentProduct_whenGetProductDetails_thenNotFoundException() {
        val productId = 1L;

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        val exc = Assertions.assertThrows(NotFoundException.class, () -> productService.getProductDetails(productId));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Product with ID " + productId + " not found", exc.getMessage());
    }

    @Test
    void whenGetProductDetails_thenGetProductDetails() {
        val productId = 1L;
        val product = new Product();
        val productDetailsDto = new ProductDetailsDto();

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Mockito.when(productMapper.mapToProductDetailsDto(product)).thenReturn(productDetailsDto);

        Assertions.assertEquals(productDetailsDto, productService.getProductDetails(productId));
    }

    @Test
    void givenDuplicateIngredients_whenCreateProduct_thenBadRequestException() {
        val productCreationDto = ProductFixtures.getProductCreationDtoFixture();
        productCreationDto.setIngredients(List.of(new ProductIngredientCreationDto(1L, 100.), new ProductIngredientCreationDto(1L, 200.)));

        val exc = Assertions.assertThrows(BadRequestException.class, () -> productService.createProduct(productCreationDto));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Duplicate ingredients present", exc.getMessage());
    }

    @Test
    void givenIngredientsNotFound_whenCreateProduct_thenNotFoundException() {
        val productCreationDto = ProductFixtures.getProductCreationDtoFixture();
        productCreationDto.setIngredients(List.of(new ProductIngredientCreationDto(1L, 100.)));

        Mockito.when(ingredientRepository.findAllByIdIn(any())).thenReturn(List.of());

        val exc = Assertions.assertThrows(NotFoundException.class, () -> productService.createProduct(productCreationDto));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Ingredients not found", exc.getMessage());
    }

    @Test
    void whenCreateProduct_thenCreateProduct() {
        val productCreationDto = ProductFixtures.getProductCreationDtoFixture();
        productCreationDto.setIngredients(List.of(new ProductIngredientCreationDto(1L, 100.), new ProductIngredientCreationDto(2L, 100.)));
        val ingredient = IngredientFixtures.getIngredientFixture();
        ingredient.setId(1L);
        val ingredient2 = IngredientFixtures.getIngredientFixture();
        ingredient2.setId(2L);
        val product = ProductFixtures.getProductFixture();
        product.setIngredientProductAssociations(List.of());

        Mockito.when(ingredientRepository.findAllByIdIn(any())).thenReturn(List.of(ingredient, ingredient2));
        Mockito.when(productMapper.mapToProduct(productCreationDto)).thenReturn(product);
        Mockito.when(productRepository.save(product)).thenReturn(product);

        Assertions.assertDoesNotThrow(() -> productService.createProduct(productCreationDto));

        Assertions.assertEquals(2, product.getIngredientProductAssociations().size());
        Assertions.assertEquals(1L, product.getIngredientProductAssociations().getFirst().getId().getIngredientId());
        Assertions.assertEquals(100., product.getIngredientProductAssociations().getFirst().getQuantity());
        Assertions.assertEquals(2L, product.getIngredientProductAssociations().get(1).getId().getIngredientId());
        Assertions.assertEquals(100., product.getIngredientProductAssociations().get(1).getQuantity());

        Mockito.verify(productRepository, Mockito.times(1)).save(product);
    }

    @Test
    void givenDuplicateIngredients_whenUpdateProduct_thenBadRequestException() {
        val productUpdateDto = ProductFixtures.getProductUpdateDtoFixture();
        productUpdateDto.setIngredients(List.of(new ProductIngredientUpdateDto(1L, 100.), new ProductIngredientUpdateDto(1L, 200.)));

        val exc = Assertions.assertThrows(BadRequestException.class, () -> productService.updateProduct(1L, productUpdateDto));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Duplicate ingredients present", exc.getMessage());
    }

    @Test
    void givenIngredientsNotFound_whenUpdateProduct_thenNotFoundException() {
        val productUpdateDto = ProductFixtures.getProductUpdateDtoFixture();
        productUpdateDto.setIngredients(List.of(new ProductIngredientUpdateDto(1L, 100.)));

        Mockito.when(ingredientRepository.findAllByIdIn(any())).thenReturn(List.of());

        val exc = Assertions.assertThrows(NotFoundException.class, () -> productService.updateProduct(1L, productUpdateDto));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Ingredients not found", exc.getMessage());
    }

    @Test
    void givenProductNotFound_whenUpdateProduct_thenNotFoundException() {
        val productId = 1L;
        val productUpdateDto = ProductFixtures.getProductUpdateDtoFixture();
        productUpdateDto.setIngredients(List.of(new ProductIngredientUpdateDto(1L, 100.)));
        val ingredient = IngredientFixtures.getIngredientFixture();
        ingredient.setId(1L);

        Mockito.when(ingredientRepository.findAllByIdIn(any())).thenReturn(List.of(ingredient));
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.empty());

        val exc = Assertions.assertThrows(NotFoundException.class, () -> productService.updateProduct(productId, productUpdateDto));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Product with ID " + productId + " not found", exc.getMessage());
    }

    @Test
    void whenUpdateProduct_thenUpdateProduct() {
        val productId = 1L;
        val productUpdateDto = ProductFixtures.getProductUpdateDtoFixture();
        productUpdateDto.setIngredients(List.of(new ProductIngredientUpdateDto(1L, 100.), new ProductIngredientUpdateDto(2L, 100.)));
        val ingredient = IngredientFixtures.getIngredientFixture();
        ingredient.setId(1L);
        val ingredient2 = IngredientFixtures.getIngredientFixture();
        ingredient2.setId(2L);
        val product = ProductFixtures.getProductFixture();
        val ingredientProductAssociation = IngredientProductAssociation.builder()
                .id(IngredientProductAssociationId.builder()
                        .ingredientId(1L)
                        .productId(productId)
                        .build())
                .quantity(10.)
                .build();
        product.setIngredientProductAssociations(new ArrayList<>());
        product.getIngredientProductAssociations().add(ingredientProductAssociation);

        Mockito.when(ingredientRepository.findAllByIdIn(any())).thenReturn(List.of(ingredient, ingredient2));
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Assertions.assertDoesNotThrow(() -> productService.updateProduct(productId, productUpdateDto));

        Assertions.assertEquals(2, product.getIngredientProductAssociations().size());
        Assertions.assertEquals(1L, product.getIngredientProductAssociations().getFirst().getId().getIngredientId());
        Assertions.assertEquals(100., product.getIngredientProductAssociations().getFirst().getQuantity());
        Assertions.assertEquals(2L, product.getIngredientProductAssociations().get(1).getId().getIngredientId());
        Assertions.assertEquals(100., product.getIngredientProductAssociations().get(1).getQuantity());

        Mockito.verify(productMapper, Mockito.times(1)).mergeToProduct(product, productUpdateDto);
    }

    @Test
    void givenNonExistentProduct_whenDeleteProduct_thenNotFoundException() {
        val productId = 1L;

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        val exc = Assertions.assertThrows(NotFoundException.class, () -> productService.deleteProduct(productId));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Product with ID " + productId + " not found", exc.getMessage());
    }

    @Test
    void givenProductWithOnlineOrders_whenDeleteProduct_thenForbiddenException() {
        val productId = 1L;
        val product = new Product();
        product.setProductOnlineOrderAssociations(List.of(new ProductOnlineOrderAssociation()));

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        val exc = Assertions.assertThrows(ForbiddenException.class, () -> productService.deleteProduct(productId));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Product with ID " + productId + " has dependencies and cannot be deleted", exc.getMessage());
    }

    @Test
    void whenDeleteProduct_thenDeleteProduct() {
        val productId = 1L;
        val product = new Product();
        product.setProductOnlineOrderAssociations(List.of());

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Assertions.assertDoesNotThrow(() -> productService.deleteProduct(productId));

        Mockito.verify(productRepository, Mockito.times(1)).delete(product);
    }
}
