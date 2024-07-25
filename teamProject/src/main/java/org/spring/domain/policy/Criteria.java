package org.spring.domain.policy;

import lombok.Data;

// 사용자의 선택에 따라 결과가 달라질 경우에 필요한 필드, 기능 구현
@Data
public class Criteria {
	
	// 현재 사용자의 페이지 위치: 초기값 1
	private int pageNum;
	
	// 한 페이지에 표시될 게시물의 개수: 10
	private int amount;
	
	// 페이지에 따라 첫번째 글이 어디부터 시작해야 하는지 저장
	private int start;
	
	// 검색에 필요한 필드 선언
	private String type; // 종류
	private String district; // 지역
	private String keyword; // 검색어
	
	// 기본 생성자: 첫 페이지(1), 페이지당 10개 게시물
	public Criteria() {
		this(1,10);
	}
	
	public Criteria(int pageNum, int amount) {
		this.pageNum = pageNum;
		this.amount = amount;
		this.start = getStart();
	}

	// 오버라이딩: 수제가 우선순위 높다
	public int getStart() {
		return (this.pageNum-1) * this.amount;
	}
}