package com.sebatmedikal.configuration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sebatmedikal.domain.Brand;
import com.sebatmedikal.domain.Operation;
import com.sebatmedikal.domain.OperationType;
import com.sebatmedikal.domain.Product;
import com.sebatmedikal.domain.Role;
import com.sebatmedikal.domain.User;
import com.sebatmedikal.gcm.NotificationSender;
import com.sebatmedikal.gcm.gcm.GcmPushImpl;
import com.sebatmedikal.service.BrandService;
import com.sebatmedikal.service.OperationService;
import com.sebatmedikal.service.OperationTypeService;
import com.sebatmedikal.service.ProductService;
import com.sebatmedikal.service.RoleService;
import com.sebatmedikal.service.UserService;
import com.sebatmedikal.util.LogUtil;

@Component
public class TestDataInitializer implements CommandLineRunner {
	private int initializeProductCount = 10;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@Autowired
	private BrandService brandService;

	@Autowired
	private ProductService productService;

	@Autowired
	private OperationTypeService operationTypeService;

	@Autowired
	private OperationService operationService;

	@Override
	public void run(String... arg0) throws Exception {
		List<Role> roles = new ArrayList<>();
		roles.add(new Role("ADMIN", "ADMIN", "\\R"));
		roles.add(new Role("USER", "USER", "\\R"));
		roleService.save(roles);
		LogUtil.logMessage(this.getClass(), roles.size() + " role saved");
		LogUtil.logMessage(this.getClass(), roleService.findAll().toString());

		User user = new User();
		user.setFirstName("SEBAT");
		user.setLastName("MEDIKAL");
		user.setUsername("sebat.medikal");
		user.setPassword("sebat12345");
		user.setRole(roles.get(0));
		user.setCreatedBy("\\R");
		userService.save(user);

		User user1 = new User();
		user1.setFirstName("Orhan");
		user1.setLastName("Yılmaz");
		user1.setUsername("byypipo");
		user1.setPassword("a");
		user1.setRole(roles.get(0));
		user1.setCreatedBy("sebat.medikal");
		user1.setFcmRegistrationId("etxLfzp1kQ8:APA91bGk1OwOT2w0H3M-Ye6MK_hd5zN-TEiRMnqx9qI3AquOu8uvc25eYYAeX3IfAMQR8bCy4aLe0PyC9uPp7dS2XBD5AIz0JEDrf_ogfMnKiv-f6tEUnwTvlO_oYiY8VMchjd3Gx--M");
		userService.save(user1);

		List<Brand> brands = new ArrayList<>();
		brands.add(new Brand("BRAND01", user.getUsername()));
		brands.add(new Brand("BRAND02", user.getUsername()));
		brandService.save(brands);
		LogUtil.logMessage(this.getClass(), brands.size() + " brand saved");

		List<Product> products = new ArrayList<>();
		for (int i = 1; i <= initializeProductCount; i++) {
			String index = "0" + i;
			if (i < 10) {
				index = "0" + index;
			}
			products.add(new Product("PRODUCT" + index, brands.get(i % 2), user.getUsername(), new BigDecimal((i * 10) + "." + (i * 10))));
		}

		for (Product product : products) {
			productService.save(product);
		}
		LogUtil.logMessage(this.getClass(), brands.size() + " product saved");

		List<OperationType> operationTypes = new ArrayList<>();
		operationTypes.add(new OperationType("ALIM", user.getUsername(), false));
		operationTypes.add(new OperationType("SATIM", user.getUsername(), true));
		for (OperationType operationType : operationTypes) {
			operationTypeService.save(operationType);
		}
		LogUtil.logMessage(this.getClass(), operationTypes.size() + " operationType saved");

		List<Operation> operations = new ArrayList<>();
		for (int i = 0; i < 25; i++) {
			operations.add(new Operation(operationTypes.get(0), products.get(0), 1, user.getUsername()));
			operations.add(new Operation(operationTypes.get(0), products.get(0), 30, user.getUsername()));
			operations.add(new Operation(operationTypes.get(1), products.get(0), 13, user.getUsername()));
			operations.add(new Operation(operationTypes.get(1), products.get(0), 1, user.getUsername()));
			operations.add(new Operation(operationTypes.get(0), products.get(1), 17, user.getUsername()));
			operations.add(new Operation(operationTypes.get(0), products.get(1), 40, user.getUsername()));
			operations.add(new Operation(operationTypes.get(1), products.get(1), 56, user.getUsername()));
			operations.add(new Operation(operationTypes.get(0), products.get(2), 10, user.getUsername()));
		}
		for (Operation operation : operations) {
			operationService.save(operation);
		}
		LogUtil.logMessage(this.getClass(), operations.size() + " operation saved");

		GcmPushImpl gcmPushImpl = new GcmPushImpl(SecurityConfiguration.FCM_SERVER_KEY);
		NotificationSender.serverOnline(gcmPushImpl, userService.findAllFcmRegistrationIds());
	}

}