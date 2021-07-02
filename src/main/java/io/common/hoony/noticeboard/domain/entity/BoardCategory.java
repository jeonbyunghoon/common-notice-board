package io.common.hoony.noticeboard.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class BoardCategory {

    //게시판 카테고리 ID [PK]
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT(20) COMMENT '게시판 카테고리 ID'", insertable = true, updatable = false)
    private Long boardCategoryId;

    //게시글 ID [FK]
    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    //카테고리 ID [FK]
    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    public BoardCategory (Category category) {
        this.category = category;
    }

}
