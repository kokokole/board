package com.example.board.repository;

import com.example.board.entity.Board;
import com.example.board.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Modifying //JPQL을 이용해서 UPDATE나 DELETE를 실행하려면 해당 어노테이션 필요
    @Query("DELETE FROM Reply r WHERE r.board.bno =:bno")
    void deleteByBno(Long bno);

    List<Reply> getRepliesByBoardOrderByRno(Board board);
}
