package io.common.hoony.noticeboard.controller;

import io.common.hoony.noticeboard.domain.dto.board.response.ResGetBoardListDTO;
import io.common.hoony.noticeboard.domain.dto.category.response.ResGetCategoryListDTO;
import io.common.hoony.noticeboard.service.CategoryService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리 목록 조회 - Controller
     * @return
     */
    @ApiOperation(
            value = "카테고리 목록 List 조회",
            notes = "- 검샘/등록 시 카테고리 목록을 조회합니다.\t\n" +
                    "- 카테고리는 사용자가 추가할 수 없습니다.\t\n" +
                    "- 1-'전체', 2-'BACK', 3-'FRONT', 4-'BLOCKCHAIN', 5-'BUSINESS'"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = ResGetBoardListDTO.class)
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResGetCategoryListDTO getCategoryList() {
        //카테고리 목록 조회
        ResGetCategoryListDTO result = categoryService.getCategoryList();

        return result;
    }

}
