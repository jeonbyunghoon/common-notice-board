package io.common.hoony.noticeboard.domain.dto.comment.response;

import io.common.hoony.noticeboard.common.type.FlagType;
import io.common.hoony.noticeboard.domain.entity.Comment;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResDeleteCommentDTO {
    private Long commentId; //댓글 ID

    private Long boardId; //게시판 ID
    private Long userId; //작성자
    private String contents; //내용

    private int commentGroup; //댓글 집합
    private int commentOrder; //댓글 순서
    private int commentLayer; //댓글 계층

    private FlagType deleteFlag; //삭제여부

    private LocalDateTime createAt; //생성 날짜
    private LocalDateTime updateAt; //수정 날짜
    private LocalDateTime deleteAt; //삭제 날짜

    public ResDeleteCommentDTO(Comment comment) {
        this.commentId = comment.getCommentId();
        this.boardId = comment.getBoard().getBoardId();
        this.userId = comment.getUserId();
        this.contents = comment.getContents();
        this.commentGroup = comment.getCommentGroup();
        this.commentOrder = comment.getCommentOrder();
        this.commentLayer = comment.getCommentLayer();
        this.deleteFlag = comment.getDeleteFlag();
        this.createAt = comment.getCreateAt();
        this.updateAt = comment.getUpdateAt();
        this.deleteAt = comment.getDeleteAt();
    }
}
