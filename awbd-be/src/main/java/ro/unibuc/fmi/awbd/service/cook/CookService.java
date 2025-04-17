package ro.unibuc.fmi.awbd.service.cook;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.awbd.common.exception.BadRequestException;
import ro.unibuc.fmi.awbd.common.exception.NotFoundException;
import ro.unibuc.fmi.awbd.common.model.Page;
import ro.unibuc.fmi.awbd.common.model.PageRequest;
import ro.unibuc.fmi.awbd.common.utils.ErrorMessageUtils;
import ro.unibuc.fmi.awbd.controller.models.*;
import ro.unibuc.fmi.awbd.domain.product.repository.ProductRepository;
import ro.unibuc.fmi.awbd.domain.user.repository.UserRepository;
import ro.unibuc.fmi.awbd.domain.user.repository.cook.CookRepository;
import ro.unibuc.fmi.awbd.domain.user.repository.cook.CookSearchRepository;
import ro.unibuc.fmi.awbd.service.cook.mapper.CookMapper;
import ro.unibuc.fmi.awbd.service.cook.model.CookFilter;
import ro.unibuc.fmi.awbd.service.cook.model.CookPageElementDetails;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CookService {
    private final CookSearchRepository cookSearchRepository;
    private final CookRepository cookRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CookMapper cookMapper;

    // TODO validate user is restaurant admin for all functions

    @Transactional(readOnly = true)
    public CooksPageDto getCooksPage(PageRequest<CookFilter> pageRequest) {
        Page<CookPageElementDetails> page = cookSearchRepository.getCooksPage(pageRequest);
        return cookMapper.mapToCookPageDto(page);
    }

    @Transactional(readOnly = true)
    public CookDetailsDto getCookDetails(Long cookId) {
        val cook = cookRepository.findById(cookId).orElseThrow(() ->
                new NotFoundException(String.format(ErrorMessageUtils.COOK_NOT_FOUND, cookId))
        );
        return cookMapper.mapToCookDetailsDto(cook);
    }

    @Transactional
    public void createCook(CookCreationDto cookCreationDto) {
        val productIds = cookCreationDto.getProducts()
                .stream()
                .map(CookProductCreationDto::getId)
                .collect(Collectors.toSet());
        if (productIds.size() != cookCreationDto.getProducts().size()) {
            throw new BadRequestException(ErrorMessageUtils.DUPLICATE_PRODUCTS_PRESENT);
        }
        val products = productRepository.findAllByIdIn(productIds);
        if (products.size() != cookCreationDto.getProducts().size()) {
            throw new NotFoundException(ErrorMessageUtils.PRODUCTS_NOT_FOUND);
        }
        if (userRepository.existsByEmail(cookCreationDto.getEmail())) {
            throw new BadRequestException(String.format(ErrorMessageUtils.USER_WITH_EMAIL_ALREADY_EXISTS, cookCreationDto.getEmail()));
        }
        val cook = cookMapper.mapToCook(cookCreationDto);
        cook.setProducts(products);
        cookRepository.save(cook);
    }

    @Transactional
    public void updateCook(Long cookId, CookUpdateDto cookUpdateDto) {
        val productIds = cookUpdateDto.getProducts()
                .stream()
                .map(CookProductUpdateDto::getId)
                .collect(Collectors.toSet());
        if (productIds.size() != cookUpdateDto.getProducts().size()) {
            throw new BadRequestException(ErrorMessageUtils.DUPLICATE_PRODUCTS_PRESENT);
        }
        val products = productRepository.findAllByIdIn(productIds);
        if (products.size() != cookUpdateDto.getProducts().size()) {
            throw new NotFoundException(ErrorMessageUtils.PRODUCTS_NOT_FOUND);
        }

        val cook = cookRepository.findById(cookId).orElseThrow(() ->
                new NotFoundException(String.format(ErrorMessageUtils.COOK_NOT_FOUND, cookId))
        );
        cookMapper.mergeToCook(cook, cookUpdateDto);
        cook.setProducts(products);
    }

    @Transactional
    public void deleteCook(Long cookId) {
        val cook = cookRepository.findById(cookId).orElseThrow(() ->
                new NotFoundException(String.format(ErrorMessageUtils.COOK_NOT_FOUND, cookId))
        );
        cookRepository.delete(cook);
    }
}
