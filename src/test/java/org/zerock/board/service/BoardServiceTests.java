package org.zerock.board.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;

@SpringBootTest
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister(){

        BoardDTO dto = BoardDTO.builder()
                .title("Test.")
                .content("Test......")
                .writerEmail("user55@aaa.com") //현재 db에 이메일이 있는지 확인
                .build();
        Long bno = boardService.register(dto); //정상처리시 bno가 넘어오도록 처리

    }

    @Test
    public  void testList() {

        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        // PageRequestDTO 객체를 생성하여 pageRequestDTO변수로 선언한다.
        PageResultDTO<BoardDTO, Object[]> result = boardService.getList(pageRequestDTO);
        // PageResultDTO 객체를 BoardDTO, Object[] 통로도 사용하여 result 변수로 선언
        // boardService.getList 메서드를 사용하여 pageRequestDTO를 처리한다.
        for(BoardDTO boardDTO : result.getDtoList()) {
            System.out.println(boardDTO);
        } // BoardDTO 객체를 향상된 for문을 이용하여 boardDTO를 출력한다.
    }
    //결과
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
    //            on board0_.writer_email=member1_.email
    //    left outer join
    //        reply reply2_
    //            on (
    //                reply2_.board_bno=board0_.bno
    //            )
    //    group by
    //        board0_.bno
    //    order by
    //        board0_.bno desc limit ?
    //Hibernate:
    //    select
    //        count(board0_.bno) as col_0_0_
    //    from
    //        board board0_
    //BoardDTO(bno=101, title=Test., content=Test......, regDate=2022-09-28T10:11:05.726402, modDate=2022-09-28T10:11:05.726402, writerEmail=user55@aaa.com, writerName=USER55, replyCount=0)
    //BoardDTO(bno=100, title=Title...100, content=Content....100, regDate=2022-09-27T09:35:50.321916, modDate=2022-09-27T09:35:50.321916, writerEmail=user100@aaa.com, writerName=USER100, replyCount=4)
    //BoardDTO(bno=99, title=Title...99, content=Content....99, regDate=2022-09-27T09:35:50.311068, modDate=2022-09-27T09:35:50.311068, writerEmail=user99@aaa.com, writerName=USER99, replyCount=3)
    //BoardDTO(bno=98, title=Title...98, content=Content....98, regDate=2022-09-27T09:35:50.269943, modDate=2022-09-27T09:35:50.269943, writerEmail=user98@aaa.com, writerName=USER98, replyCount=4)
    //BoardDTO(bno=97, title=Title...97, content=Content....97, regDate=2022-09-27T09:35:50.247135, modDate=2022-09-27T09:35:50.247135, writerEmail=user97@aaa.com, writerName=USER97, replyCount=1)
    //BoardDTO(bno=96, title=Title...96, content=Content....96, regDate=2022-09-27T09:35:50.214895, modDate=2022-09-27T09:35:50.214895, writerEmail=user96@aaa.com, writerName=USER96, replyCount=3)
    //BoardDTO(bno=95, title=Title...95, content=Content....95, regDate=2022-09-27T09:35:50.199986, modDate=2022-09-27T09:35:50.199986, writerEmail=user95@aaa.com, writerName=USER95, replyCount=3)
    //BoardDTO(bno=94, title=Title...94, content=Content....94, regDate=2022-09-27T09:35:50.186471, modDate=2022-09-27T09:35:50.186471, writerEmail=user94@aaa.com, writerName=USER94, replyCount=2)
    //BoardDTO(bno=93, title=Title...93, content=Content....93, regDate=2022-09-27T09:35:50.170865, modDate=2022-09-27T09:35:50.170865, writerEmail=user93@aaa.com, writerName=USER93, replyCount=5)
    //BoardDTO(bno=92, title=Title...92, content=Content....92, regDate=2022-09-27T09:35:50.147197, modDate=2022-09-27T09:35:50.147197, writerEmail=user92@aaa.com, writerName=USER92, replyCount=2)

    @Test
    public void testGet() {

        Long bno = 100L; // 조회할 게시물 100번을 선정한다.

        BoardDTO boardDTO = boardService.get(bno); // boardService.get 메서를 사용한다.

        System.out.println(boardDTO); // boardDTO결과를 출력한다.
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
    //            on board0_.writer_email=member1_.email
    //    left outer join
    //        reply reply2_
    //            on (
    //                reply2_.board_bno=board0_.bno
    //            )
    //    where
    //        board0_.bno=?
    // BoardDTO(bno=100, title=Title...100, content=Content....100,
    // regDate=2022-09-27T09:35:50.321916, modDate=2022-09-27T09:35:50.321916,
    // writerEmail=user100@aaa.com, writerName=USER100, replyCount=4)

    @Test
    public void testRemove(){

        Long bno = 1L;

        boardService.removeWithReplies(bno);
    }  // 게시글의 댓글을 먼저 삭제하고 게시글을 삭제한다.
    //Hibernate:
    //    delete
    //    from
    //        reply
    //    where
    //        board_bno=?
    //Hibernate:
    //    select
    //        board0_.bno as bno1_0_0_,
    //        board0_.moddate as moddate2_0_0_,
    //        board0_.regdate as regdate3_0_0_,
    //        board0_.content as content4_0_0_,
    //        board0_.title as title5_0_0_,
    //        board0_.writer_email as writer_e6_0_0_
    //    from
    //        board board0_
    //    where
    //        board0_.bno=?
    //Hibernate:
    //    delete
    //    from
    //        board
    //    where
    //        bno=?

    @Test
    public void testModify(){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(2L)
                .title("제목 변경 수정")
                .content("내용 변경 테스트....")
                .build();

        boardService.modify(boardDTO);

    } //   @Transactional 처리를 해야 nosession에 대한 오류를 해결할 수 있다.
    //Hibernate:
    //    select
    //        board0_.bno as bno1_0_0_,
    //        board0_.moddate as moddate2_0_0_,
    //        board0_.regdate as regdate3_0_0_,
    //        board0_.content as content4_0_0_,
    //        board0_.title as title5_0_0_,
    //        board0_.writer_email as writer_e6_0_0_
    //    from
    //        board board0_
    //    where
    //        board0_.bno=?
    //Hibernate:
    //    update
    //        board
    //    set
    //        moddate=?,
    //        content=?,
    //        title=?,
    //        writer_email=?
    //    where
    //        bno=?
}
