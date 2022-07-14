package com.waiyanhtet;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.waiyanhtet.demo.config.AppConfig;
import com.waiyanhtet.demo.dao.CategoryDao;
import com.waiyanhtet.demo.dao.ProductDao;
import com.waiyanhtet.demo.model.Product;

@TestMethodOrder(OrderAnnotation.class)
@SpringJUnitConfig(classes = AppConfig.class)
public class ProductDaoTest {

	@Autowired
	private ProductDao dao;

	@Autowired
	private CategoryDao categories;

	@Test
	@DisplayName("1. Create Product")
	@Order(1)
	@Sql(statements = { "insert into category(name) values('Drinks');", "insert into category(name) values('Foods');" })
	void test1() {
		var category = categories.findById(1);
		System.out.println(category);
		Product p = new Product();
		p.setCategory(category);
		p.setName("Coca cola");
		p.setPrice(600);
		int id = dao.create(p);
		assertEquals(1, id);

		System.out.println(categories.retrieve());
	}

	@Test
	@DisplayName("2. Find Product By Id")
	@Order(2)
	void test2() {
		Product product = dao.findById(1);
		assertNotNull(product);
		assertEquals("Drinks", product.getCategory().getName());
		assertEquals("Coca cola", product.getName());
		assertEquals(600, product.getPrice());

		assertNull(dao.findById(2));
	}

	@Test
	@DisplayName("3. Find Product By Category")
	@Order(3)
	void test3() {
		List<Product> list = dao.findByCategory(1);
		assertEquals(1, list.size());

		assertTrue(dao.findByCategory(2).isEmpty());
	}

	@Test
	@DisplayName("4. Search Product")
	@Order(4)
	void test4() {
		assertEquals(1, dao.search("Coca").size());

	}

	@Test
	@DisplayName("5. Update Product")
	@Order(5)
	void test5() {
		var product = dao.findById(1);
		product.setName("Coca cola 400ml");
		product.setPrice(700);
		int count = dao.update(product);
		assertEquals(1, count);

		var other = dao.findById(1);
		assertEquals(product.getName(), other.getName());
		assertEquals(product.getPrice(), other.getPrice());
	}

	@Test
	@DisplayName("6. Delete Product")
	@Order(6)
	void test6() {
		int count = dao.delete(1);
		assertEquals(1, count);

		assertNull(dao.findById(1));
	}

}
