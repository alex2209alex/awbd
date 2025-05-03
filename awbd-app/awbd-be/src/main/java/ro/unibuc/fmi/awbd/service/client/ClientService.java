package ro.unibuc.fmi.awbd.service.client;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.awbd.common.exception.BadRequestException;
import ro.unibuc.fmi.awbd.common.exception.NotFoundException;
import ro.unibuc.fmi.awbd.common.utils.ErrorMessageUtils;
import ro.unibuc.fmi.awbd.controller.models.ClientCreationDto;
import ro.unibuc.fmi.awbd.controller.models.ClientDetailsDto;
import ro.unibuc.fmi.awbd.controller.models.ClientUpdateDto;
import ro.unibuc.fmi.awbd.domain.user.repository.UserRepository;
import ro.unibuc.fmi.awbd.domain.user.repository.client.ClientRepository;
import ro.unibuc.fmi.awbd.service.client.mapper.ClientMapper;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;

    // TODO validate user is client

    @Transactional(readOnly = true)
    public ClientDetailsDto getClientDetails(Long clientId) {
        val client = clientRepository.findById(clientId).orElseThrow(() ->
                new NotFoundException(String.format(ErrorMessageUtils.CLIENT_NOT_FOUND, clientId))
        );
        return clientMapper.mapToClientDetailsDto(client);
    }

    @Transactional
    public void createClient(ClientCreationDto clientCreationDto) {
        if (userRepository.existsByEmail(clientCreationDto.getEmail())) {
            throw new BadRequestException(String.format(ErrorMessageUtils.USER_WITH_EMAIL_ALREADY_EXISTS, clientCreationDto.getEmail()));
        }
        val client = clientMapper.mapToClient(clientCreationDto);
        clientRepository.save(client);
    }

    @Transactional
    public void updateClient(Long clientId, ClientUpdateDto clientUpdateDto) {
        val client = clientRepository.findById(clientId).orElseThrow(() ->
                new NotFoundException(String.format(ErrorMessageUtils.CLIENT_NOT_FOUND, clientId))
        );
        clientMapper.mergeToClient(client, clientUpdateDto);
    }
}
