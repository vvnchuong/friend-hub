package com.friendhub.mapper;

import com.friendhub.dto.request.ReportCreationRequest;
import com.friendhub.dto.response.ReportResponse;
import com.friendhub.entity.Report;
import com.friendhub.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "resolvedAt", ignore = true)
    @Mapping(target = "reporter", source = "reporter")
    @Mapping(target = "id", ignore = true)
    Report toReport(ReportCreationRequest request, User reporter);

    ReportResponse toResponse(Report report);

}
