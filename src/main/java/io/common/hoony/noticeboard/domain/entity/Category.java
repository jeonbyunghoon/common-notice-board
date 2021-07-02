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
public class Category {

    //카테고리 ID [PK]
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT(20) COMMENT '카테고리 ID'", insertable = true, updatable = true)
    private Long categoryId;

    //카테고리 명
    @Column(columnDefinition = "VARCHAR(30) COMMENT '카테고리 명'", nullable = false, insertable = true, updatable = true)
    private String categoryName;

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

}
