package com.friendhub.dto.request;

import com.friendhub.enums.GroupRole;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateMemberRoleRequest {

    GroupRole role;

}
