package io.common.hoony.noticeboard.domain.dto.category.response;

import io.common.hoony.noticeboard.common.type.FlagType;
import io.common.hoony.noticeboard.domain.entity.Category;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResGetCategoryListDTO {

    private List<GetCategory> list = new ArrayList<>();

    public ResGetCategoryListDTO(List<Category> categories) {
        this.list = categories.stream().map(c -> new GetCategory(c)).collect(Collectors.toList());
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GetCategory {

        private Long categoryId; //카테고리 ID
        private String categoryName; //카테고리 명
        //private LocalDateTime createAt; //생성 날짜
        //private LocalDateTime deleteAt; //삭제 날짜
        //private FlagType deleteFlag; //삭제 여부
        //private LocalDateTime updateAt; //수정 날짜

        public GetCategory(Category category) {
                this.categoryId = category.getCategoryId();
                this.categoryName = category.getCategoryName();
                //this.createAt = category.getCreateAt();
                //this.createAt = category.getDeleteAt();
                //this.deleteFlag = category.getDeleteFlag();
                //this.updateAt = category.getUpdateAt();
        }
    }


}
