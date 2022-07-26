package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.PageRequestDTO;
import com.example.board.dto.PageResultDTO;
import com.example.board.entity.Board;
import com.example.board.entity.Member;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService{

    private final BoardRepository repository;

    private final ReplyRepository replyRepository;

    @Override
    public Long register(BoardDTO dto) {
        log.info(dto);
        Board board = dtoToEntity(dto);
        repository.save(board);

        return board.getBno();
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        log.info(pageRequestDTO);

        Function<Object[], BoardDTO> fn = (en -> {
//            log.info("@@@ " + new Member((String)en[1], null, null));
            return entityToDTO((Board)en[0], new Member((String)en[1], null, null), (Long)en[2]);
        });

        //Page<Object[]> result = repository.getBoardWithReplyCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));

        Page<Object[]> result = repository.searchPage(pageRequestDTO.getType(), pageRequestDTO.getKeyword(), pageRequestDTO.getPageable(Sort.by("bno").descending()));

        return new PageResultDTO<>(result, fn);
    }


    @Override
    public BoardDTO get(Long bno) {
        Object result = repository.getBoardByBno(bno);
        Object[] arr = (Object[])result;
        return entityToDTO((Board)arr[0], (Member)arr[1], (Long)arr[2]);
    }

    @Transactional
    @Override
    public void removeWithReplies(Long bno) {
        replyRepository.deleteByBno(bno);

        repository.deleteById(bno);

    }

    @Transactional
    @Override
    public void modify(BoardDTO boardDTO) {
        Board board = repository.getReferenceById(boardDTO.getBno());  //@Transactional 어노테이션 안쓰면 proxy에러 발생

        board.changeTitle(boardDTO.getTitle());
        board.changeContent(boardDTO.getContent());

        repository.save(board);
    }


}
