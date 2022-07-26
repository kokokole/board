package com.example.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "writer") // ToString 출력에서 writer 변수 제외
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY) // 명시적 Lazy 로딩 설정
    private Member writer;

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeContent(String content){
        this.content = content;
    }




}
