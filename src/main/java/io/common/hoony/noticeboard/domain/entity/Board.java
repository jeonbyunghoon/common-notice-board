package io.common.hoony.noticeboard.domain.entity;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import io.common.hoony.noticeboard.common.type.FlagType;
import io.common.hoony.noticeboard.common.type.FlagTypeConverter;
import io.common.hoony.noticeboard.domain.dto.board.request.ReqCreateBoardDTO;
import io.common.hoony.noticeboard.domain.dto.board.response.ResCreateBoardDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Board {

    //게시글 ID [PK]
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT(20) COMMENT '게시글 ID'", insertable = true, updatable = false)
    private Long boardId;

    //작성자
    @Column(columnDefinition = "BIGINT(20) COMMENT '작성자 ID'", nullable = false, insertable = true, updatable = true)
    private Long userId;

    //제목
    @Column(columnDefinition = "VARCHAR(60) COMMENT '제목'", nullable = false, insertable = true, updatable = true)
    private String title;

    //내용
    @Column(columnDefinition = "TEXT COMMENT '내용'", nullable = false, insertable = true, updatable = true)
    private String contents;

    //조회수
    @Column(columnDefinition = "INT(11) COMMENT '조회수'", nullable = false, insertable = true, updatable = true)
    //@ColumnDefault("0")
    private int views;

    //비밀번호 여부
    @Column(columnDefinition = "VARCHAR(2) COMMENT '비밀번호 여부'", nullable = false, insertable = true, updatable = true)
    @Convert(converter = FlagTypeConverter.class)
    private FlagType pwActiveFlag;

    //첨부파일 여부
    @Column(columnDefinition = "VARCHAR(2) COMMENT '첨부파일 여부'", nullable = false, insertable = true, updatable = true)
    @Convert(converter = FlagTypeConverter.class)
    private FlagType attachmentsFlag;

    //비공개 여부
    @Column(columnDefinition = "VARCHAR(2) COMMENT '비공개 여부'", nullable = false, insertable = true, updatable = true)
    @Convert(converter = FlagTypeConverter.class)
    private FlagType privateFlag;

    //댓글 여부
    @Column(columnDefinition = "VARCHAR(2) COMMENT '댓글 여부'", nullable = false, insertable = true, updatable = true)
    @Convert(converter = FlagTypeConverter.class)
    private FlagType commentFlag;

    //삭제 여부
    @Column(columnDefinition = "VARCHAR(2) COMMENT '삭제 여부'", nullable = false, insertable = true, updatable = true)
    @Convert(converter = FlagTypeConverter.class)
    private FlagType deleteFlag;

    //생성 날짜
    @Column(columnDefinition = "DATETIME COMMENT '생성 날짜'", nullable = false, insertable = true, updatable = false)
    private LocalDateTime createAt;

    //수정 날짜
    @Column(columnDefinition = "DATETIME COMMENT '수정 날짜'", nullable = false, insertable = true, updatable = true)
    private LocalDateTime updateAt;

    //삭제 날짜
    @Column(columnDefinition = "DATETIME COMMENT '삭제 날짜'", nullable = true, insertable = true, updatable = true)
    private LocalDateTime deleteAt;

    public Board(ReqCreateBoardDTO reqCreateBoardDTO) {
        this.userId = reqCreateBoardDTO.getUserId();
        this.title = reqCreateBoardDTO.getTitle();
        this.contents = reqCreateBoardDTO.getContents();
        this.views = 0;
        this.pwActiveFlag = reqCreateBoardDTO.getPwActiveFlag();
        this.attachmentsFlag = reqCreateBoardDTO.getAttachmentsFlag();
        this.privateFlag = reqCreateBoardDTO.getPrivateFlag();
        this.commentFlag = reqCreateBoardDTO.getCommentFlag();
        this.deleteFlag = FlagType.N;
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }

    public Board(Long boardId) {
        this.boardId = boardId;

    }

}
