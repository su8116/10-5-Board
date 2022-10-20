package org.zerock.board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void insertBoard() {

        IntStream.rangeClosed(1, 100).forEach(i -> {

            Member member = Member.builder().email("user" + i + "@aaa.com").build();
            // 멤메 객체(엔티티)를 이용하여 이메일에 있는 객체를 활용한 맴버 객체 생성
            Board board = Board.builder()
                    .title("Title..." + i)
                    .content("Content...." + i)
                    .writer(member)
                    .build();

            boardRepository.save(board);

        });

    }

    @Transactional
    @Test
    public void testRead1(){

        Optional<Board> result= boardRepository.findById(100L);
        // 보드테이블에 100번값을 갖는 자료를 찾는다.

        Board board = result.get();  //Board객체에 있는 모든 자료를 찾는다.

        System.out.println(board);
        System.out.println(board.getWriter()); // 작성자를 찾는다.
    }

    @Test
    public void testReadWithWriter(){

        Object result = boardRepository.getBoardWithWriter(100L);

        Object[] arr = (Object[])result;

        System.out.println("--------------------");
        System.out.println(Arrays.toString(arr));

    }

    @Test
    public void testGetBoardWithReply(){

        List<Object[]> result = boardRepository.getBoardWithReply(100L);

        for (Object[] arr : result) {
            System.out.println(Arrays.toString(arr));
        }
    }

    @Test
    public void testWithReplyCount(){

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Object[]> result=boardRepository.getBoardWithReplyCount(pageable);

        result.get().forEach( row -> {

            Object[] arr = (Object[])row;
            System.out.println(Arrays.toString(arr));
        });
    }  // 리스트 화면에 처리 결과 확인
    //[Board(bno=100, title=Title...100, content=Content....100), Member(email=user100@aaa.com, password=1111, name=USER100), 4]
    //[Board(bno=99, title=Title...99, content=Content....99), Member(email=user99@aaa.com, password=1111, name=USER99), 3]
    //[Board(bno=98, title=Title...98, content=Content....98), Member(email=user98@aaa.com, password=1111, name=USER98), 4]
    //[Board(bno=97, title=Title...97, content=Content....97), Member(email=user97@aaa.com, password=1111, name=USER97), 1]
    //[Board(bno=96, title=Title...96, content=Content....96), Member(email=user96@aaa.com, password=1111, name=USER96), 3]
    //[Board(bno=95, title=Title...95, content=Content....95), Member(email=user95@aaa.com, password=1111, name=USER95), 3]
    //[Board(bno=94, title=Title...94, content=Content....94), Member(email=user94@aaa.com, password=1111, name=USER94), 2]
    //[Board(bno=93, title=Title...93, content=Content....93), Member(email=user93@aaa.com, password=1111, name=USER93), 5]
    //[Board(bno=92, title=Title...92, content=Content....92), Member(email=user92@aaa.com, password=1111, name=USER92), 2]
    //[Board(bno=91, title=Title...91, content=Content....91), Member(email=user91@aaa.com, password=1111, name=USER91), 1]


    @Test
    public void testRead3(){

        Object result = boardRepository.getBoardByBno(100L);

        Object[] arr= (Object[]) result;
        System.out.println(Arrays.toString(arr));
    }
    // [Board(bno=100, title=Title...100, content=Content....100), 보드테이블에 정보
    // Member(email=user100@aaa.com, password=1111, name=USER100), 맴버테이블에 정보
    // 4] 댓글 수 카운트 정보

    @Test
    public void testSearch1() {

        boardRepository.search1();
    }
    //select board
    //from Board board
    //where board.bno = ?1
    //Hibernate:
    //    select
    //        board0_.bno as bno1_0_,
    //        board0_.moddate as moddate2_0_,
    //        board0_.regdate as regdate3_0_,
    //        board0_.content as content4_0_,
    //        board0_.title as title5_0_,
    //        board0_.writer_email as writer_e6_0_
    //    from
    //        board board0_
    //    where
    //        board0_.bno=?

    @Test
    public void testSearchPage() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending()
                .and(Sort.by("title").ascending()));

        Page<Object[]> result = boardRepository.searchPage("t", "1", pageable);

    }
    //Hibernate:
    //    select
    //        board0_.bno as col_0_0_,
    //        member1_.email as col_1_0_,
    //        count(reply2_.rno) as col_2_0_,
    //        board0_.bno as bno1_0_0_,
    //        member1_.email as email1_1_1_,
    //        board0_.moddate as moddate2_0_0_,
    //        board0_.regdate as regdate3_0_0_,
    //        board0_.content as content4_0_0_,
    //        board0_.title as title5_0_0_,
    //        board0_.writer_email as writer_e6_0_0_,
    //        member1_.moddate as moddate2_1_1_,
    //        member1_.regdate as regdate3_1_1_,
    //        member1_.name as name4_1_1_,
    //        member1_.password as password5_1_1_
    //    from
    //        board board0_
    //    left outer join
    //        member member1_
    //            on (
    //                board0_.writer_email=member1_.email
    //            )
    //    left outer join
    //        reply reply2_
    //            on (
    //                reply2_.board_bno=board0_.bno
    //            )
    //    where
    //        board0_.bno>?
    //        and (
    //            board0_.title like ? escape '!'
    //        )
    //    group by
    //        board0_.bno
    //    order by
    //        board0_.bno desc,
    //        board0_.title asc limit ?

}