package org.zerock.board.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.dto.ReplyDTO;

import java.util.List;

@SpringBootTest
public class ReplyServiceTests {

    @Autowired
    private ReplyService service;

    @Test
    public void testGetList(){

        Long bno = 100L; //데이터베이스에 존재하는 번호

        List<ReplyDTO> replyDTOList = service.getList(bno);

        replyDTOList.forEach(replyDTO -> System.out.println(replyDTO));
    } //결과 : 100번 게시물의 댓글 목록
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
    //ReplyDTO(rno=50, text=Reply.......50, replyer=guest, Bno=null, regDate=2022-09-27T09:36:35.833884, modDate=2022-09-27T09:36:35.833884)
    //ReplyDTO(rno=99, text=Reply.......99, replyer=guest, Bno=null, regDate=2022-09-27T09:36:37.080283, modDate=2022-09-27T09:36:37.080283)
    //ReplyDTO(rno=103, text=Reply.......103, replyer=guest, Bno=null, regDate=2022-09-27T09:36:37.112269, modDate=2022-09-27T09:36:37.112269)
    //ReplyDTO(rno=196, text=Reply.......196, replyer=guest, Bno=null, regDate=2022-09-27T09:36:38.237042, modDate=2022-09-27T09:36:38.237042)


}
