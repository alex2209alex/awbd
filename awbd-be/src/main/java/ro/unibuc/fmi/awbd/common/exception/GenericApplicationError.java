package ro.unibuc.fmi.awbd.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
@Getter
public class GenericApplicationError {
    private final int status;
    private final String detail;
}
