package org.zerock.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "board") //연관관계에서 @ManyToOne 설정 시 제외
public class Reply extends BaseEntity{ //extends BaseEntity 등록 시간과 수정 시간을 자동 적용

    @Id //PK 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY)//자동번호 생성
    private Long rno; //댓글 번호
    private String text; //댓글 내용
    private String replyer; //댓글 작성자

    @ManyToOne(fetch = FetchType.LAZY) //지연 로딩을 해서 모든 정보를 가져오지 않게 설정
    private Board board; //보드 타입에 보드 객체를 사용하여 연관관계를 맺으면
    // 알아서 pk를 연관 관계로 설정한다.
    //board와 연관 관계는 차후에 설정
}
