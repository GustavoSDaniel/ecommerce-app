package com.gustavosdaniel.ecommerce_api.address;

import com.gustavosdaniel.ecommerce_api.user.User;
import com.gustavosdaniel.ecommerce_api.user.UserRepository;
import com.gustavosdaniel.ecommerce_api.user.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    // Arrange (preparação), act (ação), assert (Verificação)

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private AddressServiceImpl addressService;

    @Nested
    class createAddress {

        @Test
        @DisplayName("Should create a Address with success")
        void shoulRegisterAddress() {

            UUID userId = UUID.randomUUID();
            String password = "senha123";
            String email = "gustavosdanieel@hotmail.com";

            User user = new User("Gustavo", password, email);
            user.setUserRole(UserRole.ADMIN);
            ReflectionTestUtils.setField(user, "id", userId);

            Long addressId = 1L;
            String houseNumber = "1122";
            String complement = "RUA PERTO DOS DEVS";
            String zipCode = "11222333";
            String street = "Rua dos dev 1000";
            String bairro = "Bairro dos dev";
            String cidade = "Devolandia";
            String estado = "DV";
            String pais = "Pais do desenvolvimento";

            Address address = new Address(
                    houseNumber, complement, zipCode, street, bairro, cidade, estado, pais
            );
            address.setUser(user);

            AddressRequest addressRequest = new AddressRequest(
                    userId, houseNumber, complement, zipCode, street, bairro, cidade, estado, pais);

            AddressResponse addressResponse = new AddressResponse(
                    addressId, "Gustavo", houseNumber, complement,
                    zipCode, street, bairro, cidade, estado, pais);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(addressMapper.toAddress(addressRequest)).thenReturn(address);
            when(addressRepository.save(any(Address.class))).thenReturn(address);
            when(addressMapper.toAddressResponse(address)).thenReturn(addressResponse);

            AddressResponse output = addressService.createAddress(addressRequest);

            assertNotNull(output);
            assertEquals(addressResponse, output);

            verify(userRepository).findById(userId);
            verify(addressRepository).save(any(Address.class));
            verify(addressMapper).toAddress(addressRequest);
            verify(addressMapper).toAddressResponse(address);


        }

    }
}