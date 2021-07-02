package io.common.hoony.noticeboard.service;

import io.common.hoony.noticeboard.common.type.FlagType;
import io.common.hoony.noticeboard.domain.dto.category.response.ResGetCategoryListDTO;
import io.common.hoony.noticeboard.domain.entity.Category;
import io.common.hoony.noticeboard.error.DkargoException;
import io.common.hoony.noticeboard.error.ErrorCode;
import io.common.hoony.noticeboard.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 목록 조회 - Service
     * @return
     */
    public ResGetCategoryListDTO getCategoryList() {

        List<Category> categories = categoryRepository.findByDeleteFlag(FlagType.N);

//        if(categories.size() == 0){
//            throw new DkargoException(ErrorCode.CATEGORY_LIST_NO_CONTENT);
//        } else{
            return new ResGetCategoryListDTO(categories);
    }

}
