package com.waiyanhtet;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

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
public class CateogoryDaoTest {

	@Autowired
	private CategoryDao dao;
	
	@Test
	@DisplayName("1. Create category ")
	@Order(1)
	void test1() {
		var c = new Category();
		c.setName("Foods");
		var data = dao.createWithSimpleJdbcInsert(c);
		assertEquals(1,data);
		System.out.println(dao.retrieve());
	}
	
	@Test
	@DisplayName("2. Update category ")
	@Order(2)
	void test2() {
		
		var c = new Category();
		c.setId(1);
		c.setName("Drinks");
		var count = dao.update(c);
		assertEquals(1,count);
	}
	
	@Test
	@DisplayName("3. Find category by id")
	@Order(3)
	void test3() {	
		Category data = dao.findById(1);
		assertEquals("Drinks",data.getName());
	
	}
	
	@Test
	@DisplayName("4. Find count by name like")
	@Order(4)
	void test4() {	
		int count = dao.findCountByNameLike("Drin");
		assertEquals(1,count);	
	}
	
	@Test
	@DisplayName("5. Find Category by name like")
	@Order(5)
	void test5() {	
		List<Category> list = dao.findCategoryByNameLike("Drin");
		assertEquals(1,list.size());
	
	}
	
	@Test
	@DisplayName("6. Delete By Id")
	@Order(6)
	void test6() {	
		int count = dao.delete(1);
		assertEquals(1,count);
	}
}
