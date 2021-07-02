package io.common.hoony.noticeboard.repository;

import io.common.hoony.noticeboard.domain.entity.Board;
import io.common.hoony.noticeboard.domain.entity.BoardPassword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardPasswordRepository extends JpaRepository<BoardPassword, Long> {

   Optional<BoardPassword> findByBoard(Board board);
}
