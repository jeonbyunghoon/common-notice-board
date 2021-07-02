package io.common.hoony.noticeboard.domain.dto.board.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResGetBoardStatusDTO {

    private String status; //자신의 글(me), 다른사람(other), 다른사람의 글이고 패스워드 설정(password)

    public ResGetBoardStatusDTO(String status) {
        this.status = status;
    }
}
