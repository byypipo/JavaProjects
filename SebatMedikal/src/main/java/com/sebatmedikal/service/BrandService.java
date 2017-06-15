package com.sebatmedikal.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sebatmedikal.domain.Brand;
import com.sebatmedikal.domain.User;
import com.sebatmedikal.repository.BrandRepository;
import com.sebatmedikal.util.ImageUtil;

@Service
public class BrandService {
	@Autowired
	private BrandRepository brandRepository;

	@Autowired
	private UserService userService;

	public void save(Brand brand) {
		save(brand, null);
	}

	public void save(Brand brand, MultipartFile originalImage) {

		if (originalImage != null) {
			if (!originalImage.isEmpty()) {
				try {
					byte[] bytes = originalImage.getBytes();
					byte[] image = ImageUtil.resize(bytes);
					brand.setImage(image);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		brandRepository.save(brand);
	}

	public void save(Iterable<Brand> brands) {
		brandRepository.save(brands);
	}

	public Brand findByName(String name) {
		return brandRepository.findByBrandName(name);
	}

	public Brand findById(long id) {
		return brandRepository.findOne(id);
	}

	public List<Brand> findAll() {
		return (List<Brand>) brandRepository.findAll();
	}

	public void delete(long id) {
		brandRepository.delete(id);
	}

	public List<String> findAllOnlyName() {
		List<String> brandNames = new ArrayList<String>();
		Iterator<Brand> iterator = findAll().iterator();

		while (iterator.hasNext()) {
			Brand brand = (Brand) iterator.next();
			brandNames.add(brand.getBrandName());
		}

		return brandNames;
	}

	public boolean isBrandExist(Brand brand) {
		if (findByName(brand.getBrandName()) != null) {
			return true;
		}

		return false;
	}

	public User getCreatedBy(Brand brand) {
		return userService.findByUsername(brand.getCreatedBy());
	}
}
