import com.google.gson.Gson;
import com.rest.webservices.EmailData;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
      System.out.println(new Gson().toJson(new EmailData("7620679165", "1000", "123456", "prabin", "pnb", "prabin.tripathi@gmail.com", "123")));
	}

}
