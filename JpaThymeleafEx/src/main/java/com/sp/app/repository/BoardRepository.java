package com.sp.app.repository;

import java.sql.SQLException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sp.app.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long>{
	/*
	 - LIKE 검색
	   findBy컬럼명Containing(String 필드)
	   findBy컬럼명1ContainingOrFindBy컬럼명2Containing(String 필드명1, String 필드명2)
	   findBy컬럼명1ContainingAndFindBy컬럼명2Containing(String 필드명1, String 필드명2)
	 */
	// public Page<Board> findByNameContaining(String kwd, Pageable pageable);
	public Page<Board> findBySubjectContainingOrContentContaining(String subject, String content, Pageable pageable);
	
	// JPQL을 이용한 검색. 페이징 처리를 위해 countQuery 속성 추가
	@Query(
			value = "Select b From Board b WHERE b.name LIKE %:kwd%",
			countQuery = "SELECT COUNT(b.num) FROM Board b WHERE b.name LIKE %:kwd%"
	)
	public Page<Board> findByName(@Param("kwd") String kwd, Pageable pageable);
	
	// nativeQuery : false 이면 JPQL, true 이면 SQL, 기본은 false
	// @Modifying : update, delete 시 추가
	@Modifying
	@Query(value = "UPDATE bbs SET hitCount=hitCount+1 WHERE num = :num", nativeQuery = true)
	public void updateHitCount(@Param("num") long num) throws SQLException;
	
	@Query(value = "SELECT * FROM bbs WHERE num>:num ORDER BY num ASC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
	public Board findByPrev(@Param("num") long num);
	
	@Query(value = "SELECT * FROM bbs WHERE num >:num AND name LIKE '%'||:kwd||'%' ORDER BY num ASC FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
	public Board findByPrevName(@Param("num") long num, @Param("kwd") String kwd);
	
	@Query(value = "SELECT * FROM bbs WHERE num >:num AND (subject LIKE '%'||:kwd||'%' OR content LIKE '%'||:kwd||'%') ORDER BY num ASC FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
	public Board findByPrevAll(@Param("num") long num, @Param("kwd") String kwd);
	
	@Query(value = "SELECT * FROM bbs WHERE num<:num ORDER BY num DESC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
	public Board findByNext(@Param("num") long num);
	
	@Query(value = "SELECT * FROM bbs WHERE num <:num AND name LIKE '%'||:kwd||'%' ORDER BY num DESC FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
	public Board findByNextName(@Param("num") long num, @Param("kwd") String kwd);
	
	@Query(value = "SELECT * FROM bbs WHERE num <:num AND (subject LIKE '%'||:kwd||'%' OR content LIKE '%'||:kwd||'%') ORDER BY num DESC FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
	public Board findByNextAll(@Param("num") long num, @Param("kwd") String kwd);
	
}
