package com.petMed.mapper;

import com.petMed.user.model.User;
import com.petMed.web.dto.VetData;
import lombok.experimental.UtilityClass;

@UtilityClass
public class VetMapper {

    public static VetData mapUserToVetData(User user) {
        return VetData.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .clinicName(user.getClinic() != null ? user.getClinic().getName() : "")
                .address(user.getClinic() != null ? user.getClinic().getAddress() : "")
                .city(user.getClinic() != null ? user.getClinic().getCity() : null)
                .site(user.getClinic() != null ? user.getClinic().getSite() : "")
                .photo(null)
                .build();
    }
}
