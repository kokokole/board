package com.example.board.repository;

import com.example.board.entity.Board;
import com.example.board.repository.search.SearchBoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {

    //한개의 Row(Object) 내에 Object[]로 나옴
    @Query("select b, w from Board b left join b.writer w where b.bno =:bno") // 내부에 있는 엔티티를 이요할 때 LEFT JOIN 뒤에 ON~ 이 없다
    Object getBoardWithWriter(@Param("bno") Long bno);

    @Query("SELECT b, r FROM Board b LEFT JOIN Reply r ON r.board = b WHERE b.bno =:bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    @Query(value="SELECT b, w, count(r) FROM Board b LEFT JOIN b.writer w LEFT JOIN Reply r on r.board = b GROUP BY b", countQuery = "SELECT count(b) FROM Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);

    @Query("SELECT b, w, count(r) FROM Board b LEFT JOIN b.writer w LEFT OUTER JOIN Reply r ON r.board = b WHERE b.bno =:bno")
    Object getBoardByBno(@Param("bno") Long bno);
}
