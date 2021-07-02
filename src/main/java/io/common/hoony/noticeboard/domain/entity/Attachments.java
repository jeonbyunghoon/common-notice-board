package io.common.hoony.noticeboard.domain.entity;

import io.common.hoony.noticeboard.common.type.FlagType;
import io.common.hoony.noticeboard.common.type.FlagTypeConverter;
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
public class Attachments {

    //첨부파일 ID [PK]
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT(20) COMMENT '첨부파일 ID'", insertable = true, updatable = false)
    private Long attachmentsId;

    //게시글 ID [FK]
    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    //첨부파일 명
    @Column(columnDefinition = "VARCHAR(50) COMMENT '첨부파일 명'", nullable = false, insertable = true, updatable = false)
    private String attachmentsName;

    //첨부파일 경로
    @Column(columnDefinition = "VARCHAR(800) COMMENT '첨부파일 경로'", nullable = false, insertable = true, updatable = true)
    private String locationPath;

    //첨부파일 타입
    @Column(columnDefinition = "VARCHAR(500) COMMENT '첨부파일 타입'", nullable = false, insertable = true, updatable = true)
    private String fileType;

    //첨부파일 크기
    @Column(columnDefinition = "BIGINT(20) COMMENT '첨부파일 크기'", nullable = false, insertable = true, updatable = false)
    private long size;

    //삭제 여부
    @Column(columnDefinition = "VARCHAR(2) COMMENT '삭제 여부'", nullable = false, insertable = true, updatable = true)
    @Convert(converter = FlagTypeConverter.class)
    private FlagType deleteFlag;

    //생성 날짜
    @Column(columnDefinition = "DATETIME COMMENT '생성 날짜'", nullable = false, insertable = true, updatable = false)
    private LocalDateTime createAt;

    //삭제 날짜
    @Column(columnDefinition = "DATETIME COMMENT '삭제 날짜'", nullable = true, insertable = true, updatable = true)
    private LocalDateTime deleteAt;

    public Attachments (String fileName) {
        this.attachmentsName = fileName;
        this.deleteFlag = FlagType.N;
        this.createAt = LocalDateTime.now();
        this.deleteAt = null;

    }

}
