package com.waiyanhtet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.waiyanhtet.demo.dao.CategoryDao;
import com.waiyanhtet.demo.model.Category;

@TestMethodOrder(OrderAnnotation.class)
@SpringJUnitConfig(locations = "/application.xml")
public class CategoryDaoTest {

	@Autowired
	private CategoryDao dao;

	@Test
	@DisplayName("2. Create With PrepareStatementCreator")
	@Order(1)
	void test1() {
		Category category = new Category();
		category.setName("Food");
		var id = dao.createWithPrepareStatementCreator(category);
		Assertions.assertEquals(1, id);

		var data = dao.retrieve();
		System.out.println(data);
	}

	@Test
	@DisplayName("2. Create With PrepareStatementCreatorFactory")
	@Order(2)
	void test2() {
		Category category = new Category();
		category.setName("Accessories");
		var id = dao.createWithPrepareStatementCreatorFactory(category);
		Assertions.assertEquals(2, id);

		var data = dao.retrieve();
		System.out.println(data);
	}

	@Test
	@DisplayName("3. Create With PrepareStatementCreator and KeyHolder")
	@Order(3)
	void test3() {
		Category category = new Category();
		category.setName("Fashion");
		var id = dao.createWithCreatorAndKeyHolder(category);
		Assertions.assertEquals(3, id);

		var data = dao.retrieve();
		System.out.println(data);
	}

	@Test
	@DisplayName("3. Create With SimpleJdbcInsert")
	@Order(4)
	void test4() {
		Category category = new Category();
		category.setName("Electronics");
		var id = dao.createWithSimpleJdbcInsert(category);
		Assertions.assertEquals(4, id);

		var data = dao.retrieve();
		System.out.println(data);
	}
}
