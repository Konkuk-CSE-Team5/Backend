package org.example.backend.domain.organization.dto;

import org.example.backend.domain.matching.model.MatchingStatus;
import org.example.backend.domain.senior.model.Senior;
import org.example.backend.global.util.AgeUtils;

import java.util.List;

public record GetOrgSeniorsResponse (
    List<SeniorDto> seniors
){
    public record SeniorDto(
            Long seniorId,
            String name,
            Integer age,
            String code,
            String matchingStatus
    ){
        public static SeniorDto entityToDto(Senior entity, MatchingStatus matchingStatus) {
            return new SeniorDto(entity.getId(),
                    entity.getName(),
                    AgeUtils.toAge(entity.getBirthday()),
                    entity.getCode(),
                    matchingStatus.name());
        }
    }
}
