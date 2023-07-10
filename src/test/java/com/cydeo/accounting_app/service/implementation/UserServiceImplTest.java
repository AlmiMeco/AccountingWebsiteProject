package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.TestDocumentInitializer;
import com.cydeo.accounting_app.entity.User;
import com.cydeo.accounting_app.mapper.MapperUtil;
import com.cydeo.accounting_app.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl service;
    @Mock
    UserRepository repository;

    @Spy
    PasswordEncoder passwordEncoder;

    @Spy
    static MapperUtil mapperUtil = new MapperUtil(new ModelMapper());

    @Test
    @DisplayName("When_find_by_id_then_success")
    public void GIVEN_ID_WHEN_FIND_BY_ID_THEN_SUCCESS() {
        // Given
        User user = TestDocumentInitializer.getUserEntity("Admin");
        // When
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        var returnedUser = service.findById(user.getId());
        // Then
        assertThat(returnedUser.getFirstname().equals(user.getFirstname()));
    }

    @Test
    @DisplayName("When_given_non_existing_id_then_fail")
    public void GIVEN_NON_EXISTING_ID_WHEN_FIND_BY_ID_THEN_FAIL() {
        when(repository.findById(anyLong())).thenThrow(NoSuchElementException.class); // Mockito return null by itself...
        assertThrows(NoSuchElementException.class, () -> service.findById(anyLong()));
    }

    @Test
    @DisplayName("When_given_null_id_then_fail")
    public void GIVEN_NULL_ID_WHEN_FIND_BY_ID_THEN_FAIL() {
        when(repository.findById(null)).thenThrow(NoSuchElementException.class); // Mockito return null by itself...
        assertThrows(NoSuchElementException.class, () -> service.findById(null));
    }


    @Test
    @DisplayName("When_find_by_user_name_then_success")
    public void GIVEN_USERNAME_WHEN_FIND_BY_USERNAME_THEN_SUCCESS() {
        // Given
        User user = TestDocumentInitializer.getUserEntity("Admin");
        when(repository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // When
        var returnedUser = service.findByUsername(user.getUsername());

        // Then
        assertThat(returnedUser.getFirstname().equals(user.getFirstname()));
    }
}