package org.zerock.board.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.QBoard;
import org.zerock.board.entity.QMember;
import org.zerock.board.entity.QReply;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {


    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Board search1() {

        log.info("search!......................");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board) ;
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        //jpqlQuery.select(board, member.email, reply.count()).groupBy(board);
        // Member에 대한 left join, select뒤에 groupby를 적용
        // select() 내에도 여러 객체를 가져오는 형태

        // 엔티티를 객체 단위가 아니라 각각의 데이터를 추출하는 경우에는 Tuple를 이용한다.
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());
        tuple.groupBy(board);

        log.info("----------------");
        log.info(jpqlQuery);
        log.info("----------------");

        //List<Board> result= jpqlQuery.fetch(); //여러객체 처리
        List<Tuple> result= tuple.fetch(); // 튜플로 처리
        return null;
    }


    //select board, member1.email, count(reply)
    //from Board board
    //  left join Member member1 with board.writer = member1
    //  left join Reply reply with reply.board = board
    //group by board
    //2022-09-29 10:16:26.026  INFO 1980 --- [    Test worker] o.z.b.r.s.SearchBoardRepositoryImpl      : ----------------
    //Hibernate:
    //    select
    //        board0_.bno as col_0_0_,
    //        member1_.email as col_1_0_,
    //        count(reply2_.rno) as col_2_0_,
    //        board0_.bno as bno1_0_,
    //        board0_.moddate as moddate2_0_,
    //        board0_.regdate as regdate3_0_,
    //        board0_.content as content4_0_,
    //        board0_.title as title5_0_,
    //        board0_.writer_email as writer_e6_0_
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
    //    group by
    //        board0_.bno

    // 튜플로 처리
    //select board, member1.email, count(reply)
    //from Board board
    //  left join Member member1 with board.writer = member1
    //  left join Reply reply with reply.board = board
    //group by board
    //2022-09-29 10:25:25.425  INFO 6876 --- [    Test worker] o.z.b.r.s.SearchBoardRepositoryImpl      : ----------------
    //Hibernate:
    //    select
    //        board0_.bno as col_0_0_,
    //        member1_.email as col_1_0_,
    //        count(reply2_.rno) as col_2_0_,
    //        board0_.bno as bno1_0_,
    //        board0_.moddate as moddate2_0_,
    //        board0_.regdate as regdate3_0_,
    //        board0_.content as content4_0_,
    //        board0_.title as title5_0_,
    //        board0_.writer_email as writer_e6_0_
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
    //    group by
    //        board0_.bno

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {

        log.info("searchPage..........................");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        //SELECT b, w, count(r) FROM Board b
        //LEFT JOIN b.writer w LEFT JOIN Reply r ON r.board = b
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = board.bno.gt(0L);

        booleanBuilder.and(expression);

        if(type != null){
            String[] typeArr = type.split("");
            //검색 조건을 작성하기
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for (String t:typeArr) {
                switch (t){
                    case "t":
                        conditionBuilder.or(board.title.contains(keyword));
                        break;
                    case "w":
                        conditionBuilder.or(member.email.contains(keyword));
                        break;
                    case "c":
                        conditionBuilder.or(board.content.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }

        tuple.where(booleanBuilder);

        //order by
        Sort sort = pageable.getSort();

        //tuple.orderBy(board.bno.desc());

        sort.stream().forEach(order -> {
            Order direction = order.isAscending()? Order.ASC: Order.DESC;
            String prop = order.getProperty();

            PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));

        });
        tuple.groupBy(board);

        //page 처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        log.info(result);

        long count = tuple.fetchCount();

        log.info("COUNT: " +count);

        return new PageImpl<Object[]>(
                result.stream().map(t -> t.toArray()).collect(Collectors.toList()),
                pageable,
                count);

        // return null;
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
}
