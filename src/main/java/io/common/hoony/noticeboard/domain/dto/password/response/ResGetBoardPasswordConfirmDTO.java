package io.common.hoony.noticeboard.domain.dto.password.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResGetBoardPasswordConfirmDTO {

    private Long boardId;
    private String password;

}
