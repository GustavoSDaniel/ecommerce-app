package com.gustavosdaniel.ecommerce_api.address;

import com.gustavosdaniel.ecommerce_api.user.User;
import com.gustavosdaniel.ecommerce_api.user.UserNotFoundException;
import com.gustavosdaniel.ecommerce_api.user.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final UserRepository userRepository;
    private static final  Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);

    public AddressServiceImpl(AddressRepository addressRepository, AddressMapper addressMapper, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public AddressResponse createAddress(UUID userId, AddressRequest address) {

        log.info("Creating address para user ");

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Address newAddress = addressMapper.toAddress(address);
        newAddress.setUser(user);

        user.getAddresses().add(newAddress);

        Address savedAddress = addressRepository.save(newAddress);

        log.info("Created address : {}", savedAddress.getStreet());

        return  addressMapper.toAddressResponse(savedAddress);
    }

    @Override
    @Transactional
    public void deleteAddress(UUID userId, Long addressId) {

        log.info("Deleting address do user {}", userId);

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Address addressDelete = user.getAddresses()
                .stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(AddressNotFoundException::new);

        user.getAddresses().remove(addressDelete);
        addressDelete.setUser(null);

        userRepository.save(user);

        log.info("Deleted address : {}", addressDelete.getStreet());
    }
}
