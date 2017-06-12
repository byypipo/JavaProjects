//import java.io.BufferedReader;
//import java.io.ByteArrayOutputStream;
//import java.io.DataOutputStream;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLConnection;
//import java.util.List;
//
//import javax.net.ssl.HttpsURLConnection;
//
//import org.apache.tomcat.util.http.fileupload.IOUtils;
//
//import com.sebatmedikal.domain.Product;
//import com.sebatmedikal.mapper.Mapper;
//import com.sebatmedikal.model.LoginModel;
//import com.sebatmedikal.model.response.ResponseModel;
//import com.sebatmedikal.model.response.ResponseModelError;
//import com.sebatmedikal.model.response.ResponseModelSuccess;
//
//public class Test {
//	static String URL = "http://localhost:8080/usersRest/login";
//	private final String USER_AGENT = "Mozilla/5.0";
//
//	public static void main(String[] args) throws Exception {
//		Test http = new Test();
//
//		System.out.println("Testing 1 - Send Http GET request");
//		
//		System.out.println(http.sendGet("http://localhost:8080/hello"));;
//		// String JSON = http.sendGet(URL);
//
//		LoginModel loginModel = new LoginModel();
//		loginModel.setUsername("orhan");
//		loginModel.setPassword("orhan123");
//
//		System.out.println(loginModel.toString());
//		String JSON = http.sendPost(URL, loginModel.toString());
//
//		System.out.println(JSON);
//
//		ResponseModel responseModel = Mapper.responseModelMapper(JSON);
//
//		if (responseModel instanceof ResponseModelSuccess) {
//			ResponseModelSuccess responseModelSuccess = (ResponseModelSuccess) responseModel;
//			System.out.println("ResponseModelSuccess");
//
//			System.out.println(responseModelSuccess.getIsOk());
//			System.out.println(responseModelSuccess.getContent());
//
//			// Product product = Mapper.productMapper(responseModelSuccess);
//			List<Product> products = Mapper.productListMapper(responseModelSuccess);
//			Product product = products.get(1);
//
//			System.out.println("getProductName: " + product.getProductName());
//			System.out.println("getBrand: " + product.getBrand());
//			System.out.println("getBrand.getBrandName: " + product.getBrand().getBrandName());
//			System.out.println("getStock.getCount: " + product.getStock().getCount());
//			System.out.println("getCreatedDate: " + product.getCreatedDate());
//
//		} else if (responseModel instanceof ResponseModelError) {
//			ResponseModelError responseModelError = (ResponseModelError) responseModel;
//			System.out.println("ResponseModelError");
//
//			System.out.println(responseModelError.getIsOk());
//			System.out.println(responseModelError.getError());
//			System.out.println(responseModelError.getErrorCode());
//		}
//	}
//
//	// HTTP GET request
//	private String sendGet(String url) throws Exception {
//		URL obj = new URL(url);
//		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//		// optional default is GET
//		con.setRequestMethod("GET");
//
//		// add request header
//		con.setRequestProperty("User-Agent", USER_AGENT);
//
//		int responseCode = con.getResponseCode();
//		System.out.println("\nSending 'GET' request to URL : " + url);
//		System.out.println("Response Code : " + responseCode);
//
//		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//		String inputLine;
//		StringBuffer response = new StringBuffer();
//
//		while ((inputLine = in.readLine()) != null) {
//			response.append(inputLine);
//		}
//		in.close();
//
//		// print result
//		// System.out.println(response.toString());
//
//		return response.toString();
//	}
//
//	// HTTP POST request
//	// private String sendPost(String URL,String content) throws Exception {
//	//
//	// URL obj = new URL(URL);
//	// HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//	//
//	// // add reuqest header
//	// con.setRequestMethod("POST");
//	// con.setRequestProperty("User-Agent", USER_AGENT);
//	// con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
//	//
//	//// String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
//	//
//	// // Send post request
//	// con.setDoOutput(true);
//	// DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//	// wr.writeBytes(content);
//	// wr.flush();
//	// wr.close();
//	//
//	// int responseCode = con.getResponseCode();
//	// System.out.println("\nSending 'POST' request to URL : " + URL);
//	// System.out.println("Post parameters : " + content);
//	// System.out.println("Response Code : " + responseCode);
//	//
//	// BufferedReader in = new BufferedReader(new
//	// InputStreamReader(con.getInputStream()));
//	// String inputLine;
//	// StringBuffer response = new StringBuffer();
//	//
//	// while ((inputLine = in.readLine()) != null) {
//	// response.append(inputLine);
//	// }
//	// in.close();
//	//
//	// // print result
//	// return response.toString();
//	//
//	// }
//
//	private String sendPost(String URL, String BODY) throws Exception {
//		URL url = new URL(URL);
//		URLConnection urlConnection = url.openConnection();
//		HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
//		httpConnection.setRequestProperty("Content-Type", "application/json");
//		httpConnection.setRequestProperty("Connection", "keep-alive");
//		httpConnection.setRequestMethod("POST");
//
//		httpConnection.setDoOutput(true);
//		httpConnection.setDoInput(true);
//
//		OutputStream outputStream = httpConnection.getOutputStream();
//		outputStream.write(BODY.getBytes("UTF8"));
//		outputStream.flush();
//		outputStream.close();
//
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//
//		InputStream is = httpConnection.getInputStream();
//
//		// IOUtils.copy(is, bos);
//		// is.close();
//		// bos.close();
//
//		BufferedReader in = new BufferedReader(new InputStreamReader(is));
//		String inputLine;
//		StringBuffer response = new StringBuffer();
//
//		while ((inputLine = in.readLine()) != null) {
//			response.append(inputLine);
//		}
//		in.close();
//
//		// print result
//		return response.toString();
//
////		return bos.toByteArray().toString();
//	}
//}
