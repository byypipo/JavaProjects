package com.sebatmedikal.mapper;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.sebatmedikal.domain.Brand;
import com.sebatmedikal.domain.Operation;
import com.sebatmedikal.domain.OperationType;
import com.sebatmedikal.domain.Product;
import com.sebatmedikal.domain.Role;
import com.sebatmedikal.domain.Stock;
import com.sebatmedikal.domain.User;
import com.sebatmedikal.model.response.ResponseModel;
import com.sebatmedikal.model.response.ResponseModelError;
import com.sebatmedikal.model.response.ResponseModelSuccess;
import com.sebatmedikal.util.NullUtil;

public class Mapper {
	private static ObjectMapper mapper = new ObjectMapper();

	public static ResponseModel responseModelMapper(String response) {
		try {
			ResponseModelSuccess responseModelSuccess = null;
			ResponseModelError responseModelError = null;

			for (int i = 0; i < 2; i++) {
				try {

					if (i == 0) {
						responseModelSuccess = mapper.readValue(response, ResponseModelSuccess.class);
					} else {
						responseModelError = mapper.readValue(response, ResponseModelError.class);
					}

					break;
				} catch (UnrecognizedPropertyException e) {
					e.printStackTrace();
				}
			}

			if (NullUtil.isNotNull(responseModelSuccess)) {
				return responseModelSuccess;
			}

			if (NullUtil.isNotNull(responseModelError)) {
				return responseModelError;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public static Brand brandMapper(ResponseModelSuccess responseModelSuccess) {
		try {
			Brand brand = mapper.readValue(mapper.writeValueAsString(responseModelSuccess.getContent()), Brand.class);
			return brand;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Brand> brandListMapper(ResponseModelSuccess responseModelSuccess) {
		try {
			List<Brand> brandList = mapper.readValue(mapper.writeValueAsString(responseModelSuccess.getContent()), new TypeReference<List<Brand>>() {
			});
			return brandList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Operation operationMapper(ResponseModelSuccess responseModelSuccess) {
		try {
			Operation operation = mapper.readValue(mapper.writeValueAsString(responseModelSuccess.getContent()), Operation.class);
			return operation;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Operation> operationListMapper(ResponseModelSuccess responseModelSuccess) {
		try {
			List<Operation> operationList = mapper.readValue(mapper.writeValueAsString(responseModelSuccess.getContent()), new TypeReference<List<Operation>>() {
			});
			return operationList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static OperationType operationTypeMapper(ResponseModelSuccess responseModelSuccess) {
		try {
			OperationType operationType = mapper.readValue(mapper.writeValueAsString(responseModelSuccess.getContent()), OperationType.class);
			return operationType;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<OperationType> operationTypeListMapper(ResponseModelSuccess responseModelSuccess) {
		try {
			List<OperationType> operationTypeList = mapper.readValue(mapper.writeValueAsString(responseModelSuccess.getContent()), new TypeReference<List<OperationType>>() {
			});
			return operationTypeList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Product productMapper(ResponseModelSuccess responseModelSuccess) {
		try {
			Product product = mapper.readValue(mapper.writeValueAsString(responseModelSuccess.getContent()), Product.class);
			return product;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Product> productListMapper(ResponseModelSuccess responseModelSuccess) {
		try {
			List<Product> productList = mapper.readValue(mapper.writeValueAsString(responseModelSuccess.getContent()), new TypeReference<List<Product>>() {
			});
			return productList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Role roleMapper(ResponseModelSuccess responseModelSuccess) {
		try {
			Role role = mapper.readValue(mapper.writeValueAsString(responseModelSuccess.getContent()), Role.class);
			return role;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Role> roleListMapper(ResponseModelSuccess responseModelSuccess) {
		try {
			List<Role> roleList = mapper.readValue(mapper.writeValueAsString(responseModelSuccess.getContent()), new TypeReference<List<Role>>() {
			});
			return roleList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Stock stockMapper(ResponseModelSuccess responseModelSuccess) {
		try {
			Stock stock = mapper.readValue(mapper.writeValueAsString(responseModelSuccess.getContent()), Stock.class);
			return stock;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Stock> stockListMapper(ResponseModelSuccess responseModelSuccess) {
		try {
			List<Stock> stockList = mapper.readValue(mapper.writeValueAsString(responseModelSuccess.getContent()), new TypeReference<List<Stock>>() {
			});
			return stockList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static User userMapper(ResponseModelSuccess responseModelSuccess) {
		try {
			User user = mapper.readValue(mapper.writeValueAsString(responseModelSuccess.getContent()), User.class);
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<User> userListMapper(ResponseModelSuccess responseModelSuccess) {
		try {
			List<User> userList = mapper.readValue(mapper.writeValueAsString(responseModelSuccess.getContent()), new TypeReference<List<User>>() {
			});
			return userList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String writeValueAsString(Object object) {
		try {
			if (NullUtil.isNull(object)) {
				return null;
			}

			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
