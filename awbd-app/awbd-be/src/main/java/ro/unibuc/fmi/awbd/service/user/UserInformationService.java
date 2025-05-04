package ro.unibuc.fmi.awbd.service.user;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.awbd.common.exception.ForbiddenException;
import ro.unibuc.fmi.awbd.common.utils.ErrorMessageUtils;
import ro.unibuc.fmi.awbd.domain.user.model.Role;
import ro.unibuc.fmi.awbd.domain.user.model.User;
import ro.unibuc.fmi.awbd.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserInformationService {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        val userId = getCurrentUserId();

        return userRepository.findById(userId).orElseThrow(() -> new ForbiddenException(ErrorMessageUtils.AUTHENTICATION_TOKEN_IS_INVALID));
    }

    public boolean isCurrentUserCook() {
        return Role.COOK.name().equals(getCurrentUserType());
    }

    public boolean isCurrentUserCourier() {
        return Role.COURIER.name().equals(getCurrentUserType());
    }

    public void ensureCurrentUserIsRestaurantAdmin() {
        if (isCurrentUserNotRestaurantAdmin()) {
            throw new ForbiddenException(ErrorMessageUtils.USER_NOT_RESTAURANT_ADMIN);
        }
    }

    public void ensureCurrentUserIsRestaurantAdminOrClient() {
        if (isCurrentUserNotRestaurantAdmin() && isCurrentUserNotClient()) {
            throw new ForbiddenException(ErrorMessageUtils.USER_NOT_RESTAURANT_ADMIN_OR_CLIENT);
        }
    }

    public boolean isCurrentUserNotRestaurantAdmin() {
        return !Role.RESTAURANT_ADMIN.name().equals(getCurrentUserType());
    }


    public void ensureCurrentUserIsClient() {
        if (isCurrentUserNotClient()) {
            throw new ForbiddenException(ErrorMessageUtils.USER_NOT_CLIENT);
        }
    }

    public boolean isCurrentUserNotClient() {
        return !Role.CLIENT.name().equals(getCurrentUserType());
    }

    private Long getCurrentUserId() {
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return Long.parseLong(authentication.getName());
        }

        throw new ForbiddenException(ErrorMessageUtils.AUTHENTICATION_TOKEN_IS_INVALID);
    }

    private String getCurrentUserType() {
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            return authentication.getAuthorities().iterator().next().getAuthority();
        }

        throw new ForbiddenException(ErrorMessageUtils.AUTHENTICATION_TOKEN_IS_INVALID);
    }
}
