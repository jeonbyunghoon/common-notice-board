package io.common.hoony.noticeboard.domain.entity;

import io.common.hoony.noticeboard.domain.dto.board.request.ReqCreateBoardDTO;
import io.common.hoony.noticeboard.domain.dto.board.response.ResCreateBoardDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class BoardPassword {

    //게시글 비밀번호 ID [PK]
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT(20) COMMENT '게시글 비밀번호 ID'", insertable = true, updatable = false)
    private Long passwordId;

    //게시글 ID [FK]
    @OneToOne
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    //비밀번호
    @Column(columnDefinition = "VARCHAR(100) COMMENT '비밀번호'", nullable = true, insertable = true, updatable = true)
    private String password;

    //생성 날짜
    @Column(columnDefinition = "DATETIME COMMENT '생성 날짜'", nullable = false, insertable = true, updatable = false)
    private LocalDateTime createAt;

    //수정 날짜
    @Column(columnDefinition = "DATETIME COMMENT '수정 날짜'", nullable = false, insertable = true, updatable = true)
    private LocalDateTime updateAt;

    public BoardPassword (String password) {
        this.password = password;
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();

    }
}
