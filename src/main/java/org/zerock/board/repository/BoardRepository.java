package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.entity.Board;
import org.zerock.board.repository.search.SearchBoardRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {

    @Query("select b, w from Board b left join b.writer w where b.bno=:bno")
    Object getBoardWithWriter(@Param("bno") Long bno);
    // 한개의 Object에 배열 값으로 나옴
    // Board를 사용하고 있지만 Member를 같이 조회해야 하는 상황
    // b.writer는 Board 클래스는 Member와 연관관계를 맺고 있음
    // 보드 입장에서 보면 writer와 연관관계가 있어서 on을 생략함.

    @Query("select b, r from Board b LEFT JOIN Reply r ON r.board = b where b.bno=:bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);
    // 보드 입장에서 보면 Reply에 연관 관계가 없어서 on을 붙인다.

    @Query(value = "select b, w, count(r) " +
            " From Board b LEFT JOIN b.writer w LEFT JOIN Reply r ON r.board = b " +
            " GROUP BY b", countQuery = "SELECT count(b) FROM Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable); // 페이징 처리
    // Board에 모든 것 Writer에 모든 것 Reply에 개수(count)를 필드로 가져온다.


    @Query("SELECT b, w , count(r) " +
           "FROM Board b LEFT JOIN b.writer w LEFT OUTER JOIN Reply r ON r.board = b " +
           "WHERE b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);

}
