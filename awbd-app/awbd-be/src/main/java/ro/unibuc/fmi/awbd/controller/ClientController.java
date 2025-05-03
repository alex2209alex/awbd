package ro.unibuc.fmi.awbd.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.fmi.awbd.common.utils.LogUtils;
import ro.unibuc.fmi.awbd.controller.api.ClientApi;
import ro.unibuc.fmi.awbd.controller.models.ClientCreationDto;
import ro.unibuc.fmi.awbd.controller.models.ClientDetailsDto;
import ro.unibuc.fmi.awbd.controller.models.ClientUpdateDto;
import ro.unibuc.fmi.awbd.service.client.ClientService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ClientController implements ClientApi  {
    private final ClientService clientService;

    @Override
    public ClientDetailsDto getClientDetails(
            @Parameter(name = "clientId", description = "Id of searched Client.", required = true, in = ParameterIn.PATH) @PathVariable("clientId") Long clientId
    ) {
        log.info(LogUtils.GET_CLIENT_DETAILS_REQUEST);
        return clientService.getClientDetails(clientId);
    }

    @Override
    public void createClient(
            @RequestBody ClientCreationDto clientCreationDto
    ) {
        log.info(LogUtils.CREATE_CLIENT_REQUEST);
        clientService.createClient(clientCreationDto);
    }

    @Override
    public void updateClient(
            @PathVariable("clientId") Long clientId,
            @RequestBody ClientUpdateDto clientUpdateDto
    ) {
        log.info(LogUtils.UPDATE_CLIENT_REQUEST);
        clientService.updateClient(clientId, clientUpdateDto);
    }
}
