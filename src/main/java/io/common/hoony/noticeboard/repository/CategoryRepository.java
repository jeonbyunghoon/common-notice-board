package io.common.hoony.noticeboard.repository;

import io.common.hoony.noticeboard.common.type.FlagType;
import io.common.hoony.noticeboard.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    //삭제 여부값에 대한 카테고리 List 조회
    List<Category> findByDeleteFlag (FlagType deleteFlag);

}
