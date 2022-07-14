package com.waiyanhtet.demo.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import com.waiyanhtet.demo.model.Category;
import com.waiyanhtet.demo.model.Product;

@Service
public class ProductDao {

	@Autowired
	private NamedParameterJdbcOperations jdbcOperation;

	@Value("${dao.product.create}")
	private String INSERT_SQL;

	@Value("${dao.product.findById}")
	private String FIND_BY_ID_SQL;

	@Value("${dao.product.select}")
	private String SELECT_SQL;

	@Value("${dao.product.findByCategory}")
	private String FIND_BY_CATEGORY_SQL;

	@Value("${dao.product.search}")
	private String SEARCH_SQL;

	@Value("${dao.product.update}")
	private String UPDATE_SQL;

	@Value("${dao.product.delete}")
	private String DELETE_SQL;

	private RowMapper<ProductDto> rowMapper;

	public ProductDao() {
		rowMapper = new BeanPropertyRowMapper<ProductDto>(ProductDto.class);
	}

	public int create(Product p) {

		KeyHolder key = new GeneratedKeyHolder();
		var params = new MapSqlParameterSource();
		params.addValue("categoryId", p.getCategory().getId());
		params.addValue("name", p.getName());
		params.addValue("price", p.getPrice());

		jdbcOperation.update(INSERT_SQL, params, key);
		return key.getKey().intValue();
	}

	public Product findById(int id) {
		var params = new MapSqlParameterSource();
		params.addValue("id", id);
		return jdbcOperation.queryForStream(FIND_BY_ID_SQL, params, rowMapper).findAny().orElseGet(() -> null);
	}

	public List<Product> findByCategory(int categoryId) {
		var params = new MapSqlParameterSource();
		params.addValue("categoryId", categoryId);

		return jdbcOperation.queryForStream(FIND_BY_CATEGORY_SQL, params, rowMapper).map(ProductDto::toProduct)
				.toList();
	}

	public List<Product> search(String keyword) {
		var params = new HashMap<String, Object>();
		params.put("keyword", keyword.toLowerCase().concat("%"));
		return jdbcOperation.queryForStream(SEARCH_SQL, params, rowMapper).map(ProductDto::toProduct).toList();
	}

	public int update(Product product) {
		var params = new MapSqlParameterSource();
		params.addValue("id", product.getId());
		params.addValue("name", product.getName());
		params.addValue("price", product.getPrice());
		return jdbcOperation.update(UPDATE_SQL, params);
	}

	public int delete(int id) {
		var params = new MapSqlParameterSource();
		params.addValue("id", id);
		return jdbcOperation.update(DELETE_SQL, params);
	}

	public static class ProductDto extends Product {

		public void setCategoryId(int id) {
			if (null == getCategory()) {
				setCategory(new Category());
			}

			getCategory().setId(id);
		}

		public void setCategoryName(String name) {
			if (null == getCategory()) {
				setCategory(new Category());
			}

			getCategory().setName(name);
		}

		public Product toProduct() {
			return this;
		}
	}

}
