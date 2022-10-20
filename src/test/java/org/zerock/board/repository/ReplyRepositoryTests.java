package org.zerock.board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void insertReply() {

        IntStream.rangeClosed(1, 300).forEach(i -> {
            //1부터 100까지의 임의의 번호
            long bno = (long) (Math.random() * 100) + 1;
            // 댓글이 없는 게시물도 있고 댓글이 많은 게시물도 존재
            Board board = Board.builder().bno(bno).build();
            // bno는 보드 객체를 활용하여 생성하도록 유도
            Reply reply = Reply.builder()
                    .text("Reply......." + i)
                    .board(board) //board에 bno가 작성된다.
                    .replyer("guest")
                    .build();

            replyRepository.save(reply);

        });
    }

    @Test
    public void readReply1(){

        Optional<Reply> result= replyRepository.findById(1L);

        Reply reply = result.get();

        System.out.println(reply);
        System.out.println(reply.getBoard());
    }

    @Test
    public void testListByBoard(){

        List<Reply> replyList = replyRepository.getRepliesByBoardOrderByRno(
                Board.builder().bno(93L).build());

        replyList.forEach(reply -> System.out.println(reply));
    } //결과 : 게시글이 93번인 댓글에 대한 목록을 가져온다
    //Hibernate:
    //    select
    //        reply0_.rno as rno1_2_,
    //        reply0_.moddate as moddate2_2_,
    //        reply0_.regdate as regdate3_2_,
    //        reply0_.board_bno as board_bn6_2_,
    //        reply0_.replyer as replyer4_2_,
    //        reply0_.text as text5_2_
    //    from
    //        reply reply0_
    //    where
    //        reply0_.board_bno=?
    //    order by
    //        reply0_.rno asc
    //Reply(rno=150, text=Reply.......150, replyer=guest)
    //Reply(rno=172, text=Reply.......172, replyer=guest)
    //Reply(rno=251, text=Reply.......251, replyer=guest)
    //Reply(rno=254, text=Reply.......254, replyer=guest)
    //Reply(rno=263, text=Reply.......263, replyer=guest)
}