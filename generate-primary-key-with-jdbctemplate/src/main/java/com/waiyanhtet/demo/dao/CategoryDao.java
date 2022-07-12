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
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import com.waiyanhtet.demo.model.Category;

@Service
public class CategoryDao {

	@Autowired
	private JdbcOperations jdbcTemplate;

	@Autowired
	private SimpleJdbcInsert simpleJdbcInsert;

	@Value("${dml.category.insert}")
	private String insert;

	@Value("${dql.category.select}")
	private String select;

	public int createWithPrepareStatementCreator(Category c) {
		
		PreparedStatementCreator creator = (Connection con) -> {
			var stmt = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
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

		return jdbcTemplate.execute(creator, callback);
	}

	public int createWithPrepareStatementCreatorFactory(Category c) {
		var factory = new PreparedStatementCreatorFactory(insert, Types.VARCHAR);
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
		return jdbcTemplate.execute(creator, callback);
	}

	public int createWithCreatorAndKeyHolder(Category c) {
		var factory = new PreparedStatementCreatorFactory(insert, Types.VARCHAR);
		factory.setReturnGeneratedKeys(true);
		var creator = factory.newPreparedStatementCreator(List.of(c.getName()));

		KeyHolder key = new GeneratedKeyHolder();

		jdbcTemplate.update(creator, key);
		return key.getKey().intValue();
	}

	public int createWithSimpleJdbcInsert(Category c) {
		Map<String, Object> map = new HashMap<>();
		map.put("name", c.getName());
		return simpleJdbcInsert.executeAndReturnKey(map).intValue();
	}

	public List<Category> retrieve() {
		return jdbcTemplate.query(select, new BeanPropertyRowMapper<>(Category.class));
	}

}
