package org.zerock.board.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass //테이블로 생성되지 않는다.
@EntityListeners(value= {AuditingEntityListener.class})
//jpa내부에서 객체가 생성/변경되는 것을 감시한다.
@Getter
abstract class BaseEntity {

    @CreatedDate //등록일을 db테이블에 적용하도록 필드 생성
    @Column(name = "regdate", updatable = false) //수정이 안되게
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column(name = "moddate")  //수정일은 변경 되도록 설정
    private LocalDateTime modDate;
}
