package org.zerock.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.repository.BoardRepository;
import org.zerock.board.repository.ReplyRepository;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService{

    private final BoardRepository repository; // 보드 생성자 자동 주입 final

    private final ReplyRepository replyrepository; // 댓글 생성자 자동 주입

    @Override
    public Long register(BoardDTO dto) {

        log.info(dto);

        Board board = dtoToEntity(dto);
        repository.save(board) ; //JPA를 이용해서 board를 처리 save

        return board.getBno() ; // 처리는 번호를 리턴한다.

    } // 등록하는 메서드 BoardDTO를 활용하여 전달받은 자료를 처리한다.

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        log.info(pageRequestDTO);

        Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board)en[0],(Member)en[1],(Long)en[2]));
//        Page<Object[]> result = repository.getBoardWithReplyCount(
//                pageRequestDTO.getPageable(Sort.by("bno").descending())  );
            Page<Object[]> result = repository.searchPage(
                    pageRequestDTO.getType(),
                    pageRequestDTO.getKeyword(),
                    pageRequestDTO.getPageable(Sort.by("bno").descending()));


        return new PageResultDTO<>(result, fn);

    }

    @Override
    public BoardDTO get(Long bno) {

        Object result = repository.getBoardByBno(bno);

        Object[] arr = (Object[])result;

        return entityToDTO((Board)arr[0], (Member)arr[1], (Long)arr[2]);

    }


     // 댓글과 게시글을 동시에 삭제 처리하기 위함
    @Override
    public void removeWithReplies(Long bno) {

        replyrepository.deleteByBno(bno); // 댓글을 삭제한다.
        repository.deleteById(bno); // 게시글을 삭제한다.

    } // 삭제 기능 구현, 트랜젝션을 이용하여 게시글과 댓글을 동시에 삭제

    @Override
    @Transactional
    public void modify(BoardDTO boardDTO) {

        Board board = repository.getOne(boardDTO.getBno());
            board.changeTitle(boardDTO.getTitle());
            board.changeContent(boardDTO.getContent());

            repository.save(board);
    }

    @Override
    public Board dtoToEntity(BoardDTO dto) {
        return BoardService.super.dtoToEntity(dto);
    }

    @Override
    public BoardDTO entityToDTO(Board board, Member member, Long replyCount) {
        return BoardService.super.entityToDTO(board, member, replyCount);
    }
}
