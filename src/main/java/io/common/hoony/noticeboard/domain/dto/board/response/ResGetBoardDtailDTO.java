package io.common.hoony.noticeboard.domain.dto.board.response;

import io.common.hoony.noticeboard.common.type.FlagType;
import io.common.hoony.noticeboard.domain.entity.Attachments;
import io.common.hoony.noticeboard.domain.entity.Board;
import io.common.hoony.noticeboard.domain.entity.BoardCategory;
import io.common.hoony.noticeboard.domain.entity.Category;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResGetBoardDtailDTO {

    private Long boardId; //게시판 ID
    private Long userId; //작성자 ID
    private String title; //제목
    private String contents; //내용
    private int views; //조회수
    private List<Category> category; //카테고리 List
    private List<Attachments> attachments; //첨부파일 List
    private FlagType pwActiveFlag; //비밀번호 여부
    private FlagType attachmentsFlag; //첨부파일 여부
    private FlagType privateFlag; //비공개 여부
    private FlagType commentFlag; //댓글 여부
    private FlagType deleteFlag; //삭제 여부
    private LocalDateTime createAt; //생성 날짜
    private LocalDateTime updateAt; //수정 날짜


    public ResGetBoardDtailDTO(Board board, List<BoardCategory> boardCategories, List<Attachments> attachments) {
        this.boardId = board.getBoardId();
        this.userId = board.getUserId();
        this.title = board.getTitle();
        this.contents = board.getContents();
        this.views = board.getViews();
        this.category = boardCategories.stream().map(bc -> bc.getCategory()).collect(Collectors.toList());
        this.attachments = attachments;
        this.pwActiveFlag = board.getPwActiveFlag();
        this.attachmentsFlag = board.getAttachmentsFlag();
        this.privateFlag = board.getPrivateFlag();
        this.commentFlag = board.getCommentFlag();
        this.deleteFlag = board.getDeleteFlag();
        this.createAt = board.getCreateAt();
        this.updateAt = board.getUpdateAt();
    }

}
