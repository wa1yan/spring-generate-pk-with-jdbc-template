package com.waiyanhtet.demo.dao;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import com.waiyanhtet.demo.model.Category;

@Service
public class CategoryDao {

	@Autowired
	private SimpleJdbcInsert simpleJdbcInsert;

	@Value("${dml.category.insert}")
	private String INSERT_SQL;

	@Value("${dql.category.select}")
	private String SELECT_SQL;

	@Value("${dao.category.update}")
	private String UPDATE_SQL;

	@Value("${dao.category.delete}")
	private String DELETE_SQL;

	@Value("${dao.category.findById}")
	private String FIND_BY_ID_SQL;

	@Value("${dao.category.findCountByNameLike}")
	private String FIND_COUNT_BY_NAME_SQL;

	@Value("${dao.category.findCategoryByNameLike}")
	private String FIND_CATEGORY_BY_NAME_SQL;

	private RowMapper<Category> rowMapper;

	public CategoryDao() {
		rowMapper = new BeanPropertyRowMapper<>(Category.class);
	}

	public int createWithPrepareStatementCreator(Category c) {

		PreparedStatementCreator creator = (Connection con) -> {
			var stmt = con.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
			stmt.setNString(1, c.getName());
			return stmt;
		};

		PreparedStatementCallback<Integer> callback = stmt -> {
			stmt.execute();
			var key = stmt.getGeneratedKeys();
			while (key.next()) {
				return key.getInt(1);
			}
			return 0;
		};

		return simpleJdbcInsert.getJdbcTemplate().execute(creator, callback);
	}

	public int createWithPrepareStatementCreatorFactory(Category c) {

		var factory = new PreparedStatementCreatorFactory(INSERT_SQL, Types.VARCHAR);
		factory.setReturnGeneratedKeys(true);
		var creator = factory.newPreparedStatementCreator(List.of(c.getName()));
		PreparedStatementCallback<Integer> callback = stmt -> {
			stmt.execute();
			var key = stmt.getGeneratedKeys();
			while (key.next()) {
				return key.getInt(1);
			}
			return 0;
		};
		return simpleJdbcInsert.getJdbcTemplate().execute(creator, callback);
	}

	public int createWithCreatorAndKeyHolder(Category c) {
		var factory = new PreparedStatementCreatorFactory(INSERT_SQL, Types.VARCHAR);
		factory.setReturnGeneratedKeys(true);
		var creator = factory.newPreparedStatementCreator(List.of(c.getName()));

		KeyHolder key = new GeneratedKeyHolder();

		simpleJdbcInsert.getJdbcTemplate().update(creator, key);
		return key.getKey().intValue();
	}

	public int createWithSimpleJdbcInsert(Category c) {
		Map<String, Object> map = new HashMap<>();
		map.put("name", c.getName());
		return simpleJdbcInsert.executeAndReturnKey(map).intValue();
	}

	public List<Category> retrieve() {
		return simpleJdbcInsert.getJdbcTemplate().query(SELECT_SQL, rowMapper);
	}

	public int update(Category c) {
		return simpleJdbcInsert.getJdbcTemplate().update(UPDATE_SQL, c.getName(), c.getId());
	}

	public Category findById(int id) {
		return simpleJdbcInsert.getJdbcTemplate().queryForObject(FIND_BY_ID_SQL, rowMapper, id);
	}

	public int findCountByNameLike(String name) {
		return simpleJdbcInsert.getJdbcTemplate().queryForObject(FIND_COUNT_BY_NAME_SQL, Integer.class,
				name.toLowerCase().concat("%"));
	}

	public List<Category> findCategoryByNameLike(String name) {
		return simpleJdbcInsert.getJdbcTemplate().query(FIND_CATEGORY_BY_NAME_SQL, rowMapper,
				name.toLowerCase().concat("%"));
	}

	public int delete(int id) {
		return simpleJdbcInsert.getJdbcTemplate().update(DELETE_SQL, id);
	}

}
