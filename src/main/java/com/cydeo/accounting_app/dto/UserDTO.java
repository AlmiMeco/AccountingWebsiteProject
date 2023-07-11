package com.cydeo.accounting_app.dto;

import lombok.*;


import javax.validation.constraints.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    @Email
    @NotBlank(message = "Email is a required field")
    private String username;


    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*_0-9])(.{4,})$", message = "Password should be at least 4 characters long and needs to contain 1 capital letter, 1 small letter and 1 special character or number")
    private String password;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*_0-9])(.{4,})$", message = "\"Confirm Password\" must match \"Password\"")
    private String confirmPassword;

    @NotBlank(message = "First Name is a required field")
    @Size(max = 50, min = 2, message = "First name must be between 2 and 50 characters long")
    private String firstname;

    @NotBlank(message = "Last Name is a required field")
    @Size(max = 50, min = 2, message = "Last name must be between 2 and 50 characters long")
    private String lastname;

    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", message = "Phone Number is required field and may be in any valid phone number format")
    private String phone;

    @NotNull(message = "Please select a Role")
    private RoleDTO role;

    @NotNull(message = "Please select a Company")
    private CompanyDTO company;

    private boolean isOnlyAdmin;


    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        checkConfirmPassword();
    }

    private void checkConfirmPassword(){

        if (this.password == null || this.confirmPassword == null) {
            return;
        }else if (!this.password.equals(this.confirmPassword))
            this.confirmPassword = null;
    }

}
