package org.gig.myplayrightapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "player", schema = "OGP")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer instance;

    private Integer partner;

    private String alias;

    private String password;

    private Integer type;

    private Integer status;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    private Integer language;

    private Integer gender;

    private String email;

    @Column(name = "chat_enabled")
    private Boolean chatEnabled;

    @Column(name = "promo_enabled")
    private Boolean promoEnabled;

    @Column(name = "max_credit_cards")
    private Integer maxCreditCards;

    @Column(name = "activation_code")
    private String activationCode;

    @Column(name = "login_fails")
    private Integer loginFails;

    private Integer affiliate;

    private Integer source;

    private String phone;

    @Column(name = "street_type")
    private Integer streetType;

    private String address;

    private String city;

    private Integer state;

    private Integer country;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "source_tag")
    private String sourceTag;

    private Integer certified;

    private Integer currency;

    private Integer verified;

    @Column(name = "national_id")
    private String nationalId;

    private Integer nationality;

    @Column(name = "tax_country")
    private Integer taxCountry;

    @Column(name = "tax_state")
    private Integer taxState;

    @Column(name = "security_question")
    private String securityQuestion;

    @Column(name = "security_response")
    private String securityResponse;

    private String mobile;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "promo_selection")
    private Integer promoSelection;

    @Column(name = "national_id_type")
    private Integer nationalIdType;

    @Column(name = "national_id_creation_date")
    private LocalDate nationalIdCreationDate;

    @Column(name = "national_id_creation_place")
    private String nationalIdCreationPlace;

    @Column(name = "national_id_expiration_date")
    private LocalDate nationalIdExpirationDate;

    @Column(name = "marital_status")
    private Integer maritalStatus;

    private Integer reason;

    @Column(name = "exclusion_date")
    private LocalDateTime exclusionDate;

    private String profession;

    @Column(name = "social_security_number")
    private String socialSecurityNumber;
}
