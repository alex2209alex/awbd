package ro.unibuc.fmi.awbd.domain.onlineorder.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Repository;
import ro.unibuc.fmi.awbd.common.model.Page;
import ro.unibuc.fmi.awbd.common.model.PageRequest;
import ro.unibuc.fmi.awbd.common.model.PaginationInformation;
import ro.unibuc.fmi.awbd.domain.onlineorder.model.OnlineOrder;
import ro.unibuc.fmi.awbd.domain.onlineorder.model.OnlineOrderStatus;
import ro.unibuc.fmi.awbd.domain.onlineorder.model.OnlineOrder_;
import ro.unibuc.fmi.awbd.domain.user.model.User_;
import ro.unibuc.fmi.awbd.domain.user.model.client.Client;
import ro.unibuc.fmi.awbd.domain.user.model.courier.Courier;
import ro.unibuc.fmi.awbd.service.onlineorder.model.OnlineOrderPageElementDetails;
import ro.unibuc.fmi.awbd.service.user.UserInformationService;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OnlineOrderSearchRepository {
    private final UserInformationService userInformationService;

    @PersistenceContext
    private EntityManager entityManager;

    public Page<OnlineOrderPageElementDetails> getProductPage(PageRequest<Object> pageRequest) {
        val onlineOrders = fetchOnlineOrders(pageRequest);
        val count = countOnlineOrders();
        return Page.<OnlineOrderPageElementDetails>builder()
                .items(onlineOrders)
                .paginationInformation(PaginationInformation.of(count, pageRequest.getPagination()))
                .build();
    }

    private List<OnlineOrderPageElementDetails> fetchOnlineOrders(PageRequest<Object> pageRequest) {
        val cb = entityManager.getCriteriaBuilder();
        val query = cb.createQuery(OnlineOrderPageElementDetails.class);
        val root = query.from(OnlineOrder.class);
        Join<OnlineOrder, Client> clientOnlineOrderAssociationJoin = root.join(OnlineOrder_.CLIENT);
        Join<OnlineOrder, Courier> courierOnlineOrderAssociationJoin = root.join(OnlineOrder_.COURIER, JoinType.LEFT);

        query.multiselect(
                root.get(OnlineOrder_.ID),
                clientOnlineOrderAssociationJoin.get(User_.EMAIL),
                courierOnlineOrderAssociationJoin.get(User_.EMAIL),
                root.get(OnlineOrder_.ONLINE_ORDER_STATUS),
                root.get(OnlineOrder_.PRICE)
        );

        query.orderBy(cb.desc(root.get(OnlineOrder_.CREATION_TIME)));

        query.where(getSearchPredicates(cb, root, clientOnlineOrderAssociationJoin, courierOnlineOrderAssociationJoin));

        return entityManager.createQuery(query)
                .setFirstResult((pageRequest.getPagination().getPage() - 1) * pageRequest.getPagination().getPageSize())
                .setMaxResults(pageRequest.getPagination().getPageSize())
                .getResultList();
    }

    private Long countOnlineOrders() {
        val cb = entityManager.getCriteriaBuilder();
        val countQuery = cb.createQuery(Long.class);
        val root = countQuery.from(OnlineOrder.class);
        Join<OnlineOrder, Client> clientOnlineOrderAssociationJoin = root.join(OnlineOrder_.CLIENT);
        Join<OnlineOrder, Courier> courierOnlineOrderAssociationJoin = root.join(OnlineOrder_.COURIER, JoinType.LEFT);

        countQuery.select(cb.count(root));

        countQuery.where(getSearchPredicates(cb, root, clientOnlineOrderAssociationJoin, courierOnlineOrderAssociationJoin));

        return entityManager.createQuery(countQuery)
                .getSingleResult();
    }

    private Predicate[] getSearchPredicates(
            CriteriaBuilder cb,
            Root<OnlineOrder> root,
            Join<OnlineOrder, Client> clientOnlineOrderAssociationJoin,
            Join<OnlineOrder, Courier> courierOnlineOrderAssociationJoin
    ) {
        val predicates = new ArrayList<Predicate>();

        if (userInformationService.isCurrentUserClient()) {
            predicates.add(cb.equal(clientOnlineOrderAssociationJoin.get(User_.ID), userInformationService.getCurrentUser().getId()));
        } else if (userInformationService.isCurrentUserCook()) {
            predicates.add(cb.equal(root.get(OnlineOrder_.ONLINE_ORDER_STATUS), OnlineOrderStatus.IN_PREPARATION));
        } else if (userInformationService.isCurrentUserCourier()) {
            predicates.add(cb.or(
                    cb.equal(root.get(OnlineOrder_.ONLINE_ORDER_STATUS), OnlineOrderStatus.READY),
                    cb.equal(courierOnlineOrderAssociationJoin.get(User_.ID), userInformationService.getCurrentUser().getId())
            ));
        }

        return predicates.toArray(new Predicate[]{});
    }
}
