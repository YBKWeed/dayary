package com.inc.dayary.repository;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.inc.dayary.domain.Diary;

@Repository
public class DiaryDao {
	@Autowired
	private SqlSession sqlSession;
	
	public List<Diary> list() {
		return sqlSession.selectList("diary.list");
	}
}