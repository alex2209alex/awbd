package ro.unibuc.fmi.awbd.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.fmi.awbd.common.mapper.PaginationMapper;
import ro.unibuc.fmi.awbd.common.model.PageRequest;
import ro.unibuc.fmi.awbd.common.utils.LogUtils;
import ro.unibuc.fmi.awbd.controller.api.ProductApi;
import ro.unibuc.fmi.awbd.controller.models.*;
import ro.unibuc.fmi.awbd.service.product.ProductService;
import ro.unibuc.fmi.awbd.service.product.mapper.ProductMapper;
import ro.unibuc.fmi.awbd.service.product.model.ProductFilter;
import ro.unibuc.fmi.awbd.service.product.model.ProductSortableColumn;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController implements ProductApi {
    private static final Integer PAGE_SIZE_LIMIT = 50;
    private static final String DEFAULT_SORT = "+name";

    private final ProductMapper productMapper;
    private final PaginationMapper paginationMapper;
    private final ProductService productService;

    @Override
    public ProductsPageDto getProductsPage(
            @RequestParam(value = "sort", required = false, defaultValue = "+name") String sort,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "1") Long offset,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description
    ) {
        log.info(LogUtils.GET_PRODUCTS_PAGE_REQUEST);

        val productFilter = Optional.ofNullable(productMapper.mapToProductFilter(
                name,
                description
        )).orElse(new ProductFilter());

        val paginationRequest = paginationMapper.mapToPaginationRequest(
                offset,
                Math.min(limit, PAGE_SIZE_LIMIT),
                parseSort(sort)
        );

        return productService.getProductsPage(PageRequest.of(productFilter, paginationRequest));
    }

    @Override
    public List<ProductSearchDetailsDto> getProducts() {
        log.info(LogUtils.GET_PRODUCTS_REQUEST);
        return productService.getProducts();
    }

    @Override
    public ProductDetailsDto getProductDetails(
            @PathVariable("productId") Long productId
    ) {
        log.info(LogUtils.GET_PRODUCT_DETAILS_REQUEST);
        return productService.getProductDetails(productId);
    }

    @Override
    public void createProduct(
            @RequestBody ProductCreationDto productCreationDto
    ) {
        log.info(LogUtils.CREATE_PRODUCT_REQUEST);
        productService.createProduct(productCreationDto);
    }

    @Override
    public void updateProduct(
            @PathVariable("productId") Long productId,
            @RequestBody ProductUpdateDto productUpdateDto
    ) {
        log.info(LogUtils.UPDATE_PRODUCT_REQUEST);
        productService.updateProduct(productId, productUpdateDto);
    }

    @Override
    public void deleteProduct(
            @PathVariable("productId") Long productId
    ) {
        log.info(LogUtils.DELETE_PRODUCT_REQUEST);
        productService.deleteProduct(productId);
    }

    private String parseSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return DEFAULT_SORT;
        }

        sort = sort.toLowerCase().strip();

        if (!sort.startsWith("+") && !sort.startsWith("-")) {
            sort = "+" + sort;
        }

        if (!ProductSortableColumn.isValidSortableColumn(sort.substring(1))) {
            return DEFAULT_SORT;
        }

        return sort;
    }
}
