package com.waiyanhtet;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.waiyanhtet.demo.config.AppConfig;
import com.waiyanhtet.demo.model.Category;

@TestMethodOrder(OrderAnnotation.class)
@SpringJUnitConfig(classes = AppConfig.class)
public class HsqlDataBaseTest {

	@Autowired
	private JdbcOperations jdbcTemplate;

	@Test
	@Order(1)
	void test1() {
		List<Object[]> params = new ArrayList<Object[]>();
		params.add(new Object[] { "Food" });
		params.add(new Object[] { "Fashion" });
		params.add(new Object[] { "Accessories" });

		var data = jdbcTemplate.batchUpdate("insert into category(name) values (?)", params);
		Assertions.assertEquals(3, data.length);

		var result = jdbcTemplate.query("select * from category", new BeanPropertyRowMapper<>(Category.class));
		System.out.println(result);
	}
}
