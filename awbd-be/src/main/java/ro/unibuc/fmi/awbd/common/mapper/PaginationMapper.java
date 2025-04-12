package ro.unibuc.fmi.awbd.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ro.unibuc.fmi.awbd.common.model.PaginationRequest;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PaginationMapper {
    PaginationRequest mapToPaginationRequest(Long page, Integer pageSize, String sort);
}
