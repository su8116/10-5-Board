package org.zerock.board.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {

    private Long bno;  //Board 테이블

    private String title; //Board 테이블

    private String content; //Board 테이블

    private LocalDateTime regDate ; //Board 테이블

    private LocalDateTime modDate ; //Board 테이블

    private String writerEmail; // Member 테이블

    private String writerName; // Member 테이블

    private int replyCount ; // reply 테이블 count
}
