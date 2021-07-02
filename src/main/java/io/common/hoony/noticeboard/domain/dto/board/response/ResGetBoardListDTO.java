package io.common.hoony.noticeboard.domain.dto.board.response;

import io.common.hoony.noticeboard.common.type.FlagType;
import io.common.hoony.noticeboard.domain.entity.Board;
import io.common.hoony.noticeboard.domain.entity.BoardCategory;
import io.common.hoony.noticeboard.domain.entity.Category;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResGetBoardListDTO {

    private List<GetBoardCategoryInfo> list = new ArrayList<>();

    public ResGetBoardListDTO(List<GetBoardCategoryInfo> getBoardCategoryInfos) {
        this.list = getBoardCategoryInfos;
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GetBoardCategoryInfo {

        private Long boardId; //게시판 ID
        private String title; //제목
        private String contents; //내용
        private Long userId; //작성자 ID
        private int views; //조회수
        private LocalDateTime updateAt; //수정 날짜
        private FlagType pwActiveFlag; //비밀번호 여부
        private FlagType attachmentsFlag; //첨부파일 여부
        private FlagType privateFlag; //비공개 여부
        private List<Category> category; //카테고리 List

        public GetBoardCategoryInfo(Board board, List<BoardCategory> boardCategories) {
            this.boardId = board.getBoardId();
            this.title = board.getTitle();
            this.userId = board.getUserId();
            this.contents = board.getContents();
            this.views = board.getViews();
            this.updateAt = board.getUpdateAt();
            this.pwActiveFlag = board.getPwActiveFlag();
            this.attachmentsFlag = board.getAttachmentsFlag();
            this.privateFlag = board.getPrivateFlag();

            //case 1
            this.category = boardCategories.stream().map(bc -> bc.getCategory()).collect(Collectors.toList());

            //case 2
            /*this.category = new ArrayList<>();

            for(int i=0; i<boardCategories.size(); i++) {
                    this.category.add(boardCategories.get(i).getCategory());
            }*/
        }
    }

}
