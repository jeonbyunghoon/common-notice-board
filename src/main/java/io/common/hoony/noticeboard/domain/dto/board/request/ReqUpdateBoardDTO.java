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
public class ReqUpdateBoardDTO {

    private Long boardId; //게시판 ID
    private Long userId; //작성자 ID
    private String title; //제목
    private String contents; //내용
    private FlagType pwActiveFlag; //비밀번호 여부
    private String password; //비밀번호
    private FlagType attachmentsFlag; //첨부파일 여부
    private FlagType privateFlag; //비공개 여부
    private FlagType commentFlag; //댓글 여부
    private List<Long> categoryId; //카테고리 검색 목록에 대한 ID List

}
