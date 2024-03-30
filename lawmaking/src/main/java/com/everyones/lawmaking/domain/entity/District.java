package com.everyones.lawmaking.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "district_id")
    private long id;

    @Column(name = "district_name")
    private String districtName;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "gu_name")
    private String guName;

    @Column(name = "assembly_number")
    private int assemblyNumber;

}
