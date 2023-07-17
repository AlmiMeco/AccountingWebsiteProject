package com.cydeo.accounting_app.ClientVendorServiceImpl;

import com.cydeo.accounting_app.TestDocumentInitializer;
import com.cydeo.accounting_app.dto.ClientVendorDTO;
import com.cydeo.accounting_app.dto.CompanyDTO;
import com.cydeo.accounting_app.dto.UserDTO;
import com.cydeo.accounting_app.entity.ClientVendor;
import com.cydeo.accounting_app.entity.Company;
import com.cydeo.accounting_app.entity.User;
import com.cydeo.accounting_app.enums.ClientVendorType;
import com.cydeo.accounting_app.enums.CompanyStatus;
import com.cydeo.accounting_app.exception.ClientVendorNotFoundException;
import com.cydeo.accounting_app.mapper.MapperUtil;
import com.cydeo.accounting_app.repository.ClientVendorRepository;
import com.cydeo.accounting_app.service.SecurityService;
import com.cydeo.accounting_app.service.implementation.ClientVendorServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientVendorServiceImpl_UnitTest {
    @Mock
    private SecurityService securityService;
    @Mock
    private ClientVendorRepository clientVendorRepository;
    @InjectMocks
    private ClientVendorServiceImpl clientVendorService;
    @Spy
    static MapperUtil mapperUtil = new MapperUtil(new ModelMapper());

    @Test
    @DisplayName("get_Current_User")
    void getCurrentUser() {
        User expectedUser = TestDocumentInitializer.getUserEntity("Admin");
        UserDTO expectedUserDTO = mapperUtil.convert(expectedUser, new UserDTO());
        when(securityService.getLoggedInUser()).thenReturn(expectedUserDTO);
        User currentUser = clientVendorService.getCurrentUser();
        assertEquals(expectedUser.getId(), currentUser.getId());
    }


    @Test
    @DisplayName("get_Company")
    void getCompany() {
        CompanyDTO companyDTO = TestDocumentInitializer.getCompany(CompanyStatus.ACTIVE);
        Company convert = mapperUtil.convert(companyDTO, new Company());
        UserDTO userDTO = new UserDTO();
        userDTO.setCompany(companyDTO);
        when(securityService.getLoggedInUser()).thenReturn(userDTO);
        Company company1 = clientVendorService.getCurrentUser().getCompany();
        assertEquals(convert.getId(), company1.getId());
    }

    @Test
    @DisplayName("find_by_id")
    void findById() {
        when_client_vendor_doesnt_exist();
        when_client_vendor_exist();
    }

    @Test
    @DisplayName("create_Client_Vendor")
    void createClientVendor() {
        testCreateClientVendor();
    }


    @Test
    @DisplayName("clients_vendors_types_existing")
    void clientVendorType() {
        when_client_vendor_type_vendor_exist();
        when_client_vendor_type_client_exist();
    }

    @Test
    @DisplayName("delete_Client_Vendor_By_Id")
    void deleteClientVendorById() {
        delete_client_vendor();
    }




    @Test
    @DisplayName("when_client_vendor_type_vendor_exist")
    public void when_client_vendor_type_vendor_exist() {
        ClientVendor clientVendorEntity = TestDocumentInitializer.getClientVendorEntity(ClientVendorType.VENDOR);
        when(clientVendorRepository.findById(clientVendorEntity.getId())).thenReturn(Optional.of(clientVendorEntity));
        ClientVendorDTO byId = clientVendorService.findById(clientVendorEntity.getId());
        assertThat(byId.getClientVendorName().equals(clientVendorEntity.getClientVendorName()));
    }

    @Test
    @DisplayName("when_client_vendor_type_client_exist")
    public void when_client_vendor_type_client_exist() {
        ClientVendor clientVendorEntity = TestDocumentInitializer.getClientVendorEntity(ClientVendorType.CLIENT);
        when(clientVendorRepository.findById(clientVendorEntity.getId())).thenReturn(Optional.of(clientVendorEntity));
        ClientVendorDTO byId = clientVendorService.findById(clientVendorEntity.getId());
        assertThat(byId.getClientVendorName().equals(clientVendorEntity.getClientVendorName()));
    }

    @Test
    @DisplayName("when_client_vendor_doesnt_exist")
    public void when_client_vendor_doesnt_exist() {
        when(clientVendorRepository.findById(1L)).thenReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> clientVendorService.findById(1L));
        assertThat(throwable).isInstanceOf(ClientVendorNotFoundException.class);
    }

    @Test
    @DisplayName("when_client_vendor_exist")
    public void when_client_vendor_exist() {
        Long clientVendorId = 1L;
        when(clientVendorRepository.findById(clientVendorId)).thenReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> clientVendorService.findById(clientVendorId));
        assertThat(throwable).isInstanceOf(ClientVendorNotFoundException.class);
    }

    @Test
    @DisplayName("create_client_vendor")
    public void testCreateClientVendor() {
        // Given
        UserDTO loggedInUser = new UserDTO();
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(1L);
        loggedInUser.setCompany(companyDTO);

        ClientVendorDTO clientVendorDTO = new ClientVendorDTO();
        clientVendorDTO.setClientVendorName("Test Vendor");
        // Set other fields as needed for the test

        when(securityService.getLoggedInUser()).thenReturn(loggedInUser);

        // When
        ClientVendorDTO result = clientVendorService.createClientVendor(clientVendorDTO);

        // Then
        assertEquals(loggedInUser.getCompany(), clientVendorDTO.getCompany());
        // Add additional assertions as needed to verify the behavior and result.
        // For example, you can verify that the save method of the repository is called.
        verify(clientVendorRepository, times(1)).save(any(ClientVendor.class));
    }

    @Test
    @DisplayName("delete_client_vendor")
    public void delete_client_vendor() {
        Long clientVendorId = 1L;
        ClientVendor clientVendor = TestDocumentInitializer.getClientVendorEntity(ClientVendorType.CLIENT);
        when(clientVendorRepository.findById(clientVendorId)).thenReturn(Optional.of(clientVendor));
        when(clientVendorRepository.save(clientVendor)).thenReturn(clientVendor);
        clientVendorService.deleteClientVendorById(clientVendorId);
        verify(clientVendorRepository, times(1)).save(clientVendor);
        assertTrue(clientVendor.getIsDeleted());
    }

    @Test
    @DisplayName("update_client_vendor_not_found")
    public void testUpdateClientVendorNotFound() {
        // Given
        Long clientId = 1L;
        ClientVendorDTO clientVendorDTO = new ClientVendorDTO();
        clientVendorDTO.setId(clientId);
        clientVendorDTO.setClientVendorName("Updated Vendor");
        // Set other fields as needed for the test

        // When
        when(clientVendorRepository.findById(clientId)).thenReturn(Optional.empty());

        // Then
        assertThrows(ClientVendorNotFoundException.class, () -> clientVendorService.updateClientVendor(clientId, clientVendorDTO));
        // Verify that the findById and save methods are called with the correct arguments
        verify(clientVendorRepository, times(1)).findById(clientId);
        verify(clientVendorRepository, never()).save(any(ClientVendor.class));
    }


}




