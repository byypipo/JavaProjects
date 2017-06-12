package com.sebatmedikal.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sebatmedikal.configuration.GeneralConfiguration;
import com.sebatmedikal.domain.Brand;
import com.sebatmedikal.domain.Product;
import com.sebatmedikal.domain.Stock;
import com.sebatmedikal.domain.User;
import com.sebatmedikal.repository.ProductRepository;
import com.sebatmedikal.util.ImageUtil;

@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private BrandService brandService;

	@Autowired
	private StockService stockService;

	@Autowired
	private UserService userService;

	public void save(Product product) {
		save(product, null);
	}

	public void save(Product product, MultipartFile originalImage) {

		if (originalImage != null) {
			if (!originalImage.isEmpty()) {
				try {
					byte[] bytes = originalImage.getBytes();
					byte[] image = ImageUtil.resize(bytes);
					product.setImage(image);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		Stock stock = stockService.createAndSaveForMe(product);
		product.setStock(stock);
		productRepository.save(product);
	}

	public void save(Iterable<Product> products) {
		productRepository.save(products);
	}

	public Product findByName(String name) {
		return productRepository.findByProductName(name);
	}

	public Product findById(long id) {
		return productRepository.findOne(id);
	}

	public Product findByBarcod(String barcod) {
		return productRepository.findByBarcod(barcod);
	}

	public List<Product> findAll() {
		return (List<Product>) productRepository.findAll();
	}

	public void delete(long id) {
		productRepository.delete(id);
	}

	public long count() {
		return productRepository.count();
	}

	public List<Product> getProductPage(int begin) {
		return productRepository.queryByOrderByCreatedDate(new PageRequest(begin, GeneralConfiguration.PAGESIZE));
	}

	public List<String> getProductNames() {
		List<String> productNames = new ArrayList<String>();
		Iterator<Product> iterator = findAll().iterator();

		while (iterator.hasNext()) {
			Product product = (Product) iterator.next();
			productNames.add(product.getProductName());
		}

		return productNames;
	}

	public boolean isProductExist(Product product) {
		if (product.getBarcod() != null) {
			if (findByBarcod(product.getBarcod()) != null) {
				return true;
			}
		}

		if (findByName(product.getProductName()) != null) {
			return true;
		}

		return false;
	}

	public List<Product> findByBrand(Brand brand) {
		return productRepository.findByBrand(brand);
	}

	public List<Product> findByBrandId(long id) {
		Brand brand = brandService.findById(id);
		return productRepository.findByBrand(brand);
	}

	public User getCreatedBy(Product product) {
		return userService.findByUsername(product.getCreatedBy());
	}
}
