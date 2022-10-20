package org.zerock.board.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString //나중에 엔티티 객체를 사용하여 문자열검색이 용이하도록
// 연관관계 설정할 때 제외부분 명심
public class Member extends BaseEntity {

    @Id
    private String email;

    private String password;

    private String name;
}
