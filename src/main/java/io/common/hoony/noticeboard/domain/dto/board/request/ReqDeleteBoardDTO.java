package io.common.hoony.noticeboard.domain.dto.board.request;

import io.common.hoony.noticeboard.common.type.FlagType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReqDeleteBoardDTO {
    private Long boardId; //게시판 ID
    private Long userId; //작성자 ID
}
