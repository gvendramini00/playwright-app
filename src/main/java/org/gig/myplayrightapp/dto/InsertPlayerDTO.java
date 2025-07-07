package org.gig.myplayrightapp.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record InsertPlayerDTO(
        String firstName,
        String middleName,
        String lastName,
        int gender,
        LocalDate birthDate,
        String nationalId,

        String email,
        String phone,
        String address,
        int state,
        int taxState,
        String city,
        String zipCode,

        String alias,
        String password,

        String securityQuestion,
        String securityResponse
) {}
