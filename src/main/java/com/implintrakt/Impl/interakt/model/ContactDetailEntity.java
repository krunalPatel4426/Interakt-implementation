package com.implintrakt.Impl.interakt.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Data
@Table(name = "contact_details")
public class ContactDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_detail_id")
    private Long contactDetailId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "status")
    private String status;
}
