import java.util.*;
import java.time.LocalDate;

public class SuperMarket{
	static Scanner s = new Scanner(System.in);

	public static void cls(){
		try{
			new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		}catch(Exception e){

		}
	}

	public static void enterWait(){
		System.out.print("Press Enter to continue...");
		s.nextLine();
	}

	public static int intInput(String wordings){
		System.out.print(wordings);
		int val = s.nextInt();s.nextLine();
		return val;
	}

	public static String input(String word){
		System.out.print(word);
		String str = s.nextLine();
		return str;
	}

	public static int page(boolean invalid, String heading, String exit, String... options){
		System.out.println("         "   + heading);
		for(int i = 0; i < options.length; i++){
			System.out.println((i+1) + " - " + options[i]);
		}
		System.out.println(0 + " - " + exit);
		if(invalid){
			System.out.println("Invalid Option");
		}
		System.out.print("Choise : ");
		int com = s.nextInt(); s.nextLine();
		return com;
	}

	public static void welcome(){
		int com = -1; boolean invalid = false;
		while(true){
			cls();
			com = page(invalid, "Welcome to Super Market", "Exit", "User", "Admin" );
			invalid = false;
			if(com == 1){
				Customer.login();
			}else if(com == 2){
				Admin.login();
			}else if(com == 0){
				break;
			}else{
				invalid = true;
			}

		}
	}

	public static void main(String[] args){
		welcome();
	}
}

class User{
	String name;
	String email;
	String password;

	public User(String name,String email, String password){
		this.name = name;
		this.email = email;
		this.password = password;
	}
}

class Customer extends User{
	static ArrayList<Customer> users = new ArrayList<Customer>();


	int credit;
	int loyalty;
	int totalPurchasePrice;
	ArrayList<CartItem> cart = new ArrayList<CartItem>();
	
	ArrayList<String> bills = new ArrayList<String>();


	static{
		Customer c1 = new Customer("Harikrishna D", "hari","1234");
		users.add(c1);	
	}

	class CartItem{
		int id;
		int count;

		public CartItem(int id,int count){
			this.id = id;
			this.count = count;
		}
	}

	public Customer(String name, String email, String password){
		super(name, email, password);
		this.credit = 1000;
	}

	public static int getUser(String email){
		for(int i = 0; i < users.size(); i++){
			if(users.get(i).email.equals(email)){
				return i;
			}
		}
		return -1;
	}

	public static int authUser(String email, String password){
		for(int i = 0; i < users.size(); i++){
			if(users.get(i).email.equals(email)){
				if(users.get(i).password.equals(password)){
					return i;
				}
			}
		}
		return -1;
	}

	public static int login(){
		SuperMarket.cls();
		System.out.println("Enter Login Credentials");
		System.out.print("Email : ");
		String email = SuperMarket.s.nextLine();
		System.out.print("Password : ");
		String password = SuperMarket.s.nextLine();
		int ind = authUser(email, password);
		if(ind == -1){System.out.println("Entered Credentials Wrong");}
		else{users.get(ind).welcome();return ind;}
		SuperMarket.enterWait();
		return ind;
	}

	public static ArrayList<Customer> sortByPurchase(){
		ArrayList<Customer> res = new ArrayList<Customer>(users);
		for(int i = 0; i < users.size(); i++){
			for(int j = i+1; j < users.size(); j++){
				if(res.get(i).totalPurchasePrice < res.get(j).totalPurchasePrice){
					Customer temp = res.get(i);
					res.set(i, res.get(j));
					res.set(j, temp);
				}
			}	
		}
		return res;
	}

	public void afterPurchase(int cost){
		if(cost >= 5000){
			credit -= (cost-100);
		}else{
			credit -= cost;
			loyalty += cost/100;
			int quo = loyalty/50;
			loyalty = loyalty%50;
			credit += quo*100;
		}
		totalPurchasePrice += cost;
	}

	public void cred(){
		SuperMarket.cls();
		System.out.println("Credit : " + credit);
		System.out.println("Loyalty : " + loyalty);
		SuperMarket.enterWait();
	}

	public void products(){
		int com = -1; boolean invalid = false;
		while(true){
			SuperMarket.cls();
			System.out.println("Products : \n");
			
			for(Product p:Product.getProducts()){
				p.display();
				System.out.println();
			}

			com = SuperMarket.page(invalid, "Options" , "Back", "Add to Cart" );

			invalid = false;
			if(com == 1){
				int prodId = SuperMarket.intInput("Product Id : ");
				Product prod = Product.getProductById(prodId);
				if(prod!=null){
					int count = SuperMarket.intInput("Enter Count : ");
					if(prod.stock >= count){
						addProd(prodId, count);
						Product.getProductById(prodId).stock -= count;
					}else{
						System.out.println("Requested Stock Unavailable");
					}
				}else{
					System.out.print("Entered Product Id Doesnot Exists");
					SuperMarket.enterWait();
				}
			}else if(com == 0){
				break;
			}else{
				invalid = true;
			}

		}



	}

	public int hasProd(int id){
		for(int i = 0; i < cart.size(); i++){
			if(cart.get(i).id == id){
				return i;
			}
		}
		return -1;
	}

	public int removeProd(int id){
		for(int i = 0; i < cart.size(); i++){
			if(cart.get(i).id == id){
				int count = cart.get(i).count;
				cart.remove(i);
				return count;
			}
		}
		return -1;
	}

	public boolean addProd(int id, int count){
		for(int i = 0; i < cart.size(); i++){
			if(cart.get(i).id == id){
				cart.get(i).count += count;
				return true;
			}
		}

		CartItem c = new CartItem(id, count);
		cart.add(c);
		return true;
	}

	public void clearCart(){
		for(CartItem c:cart){
			Product.getProductById(c.id).purchase += c.count;
		}
		cart.clear();
	}

	public void showBill(){
		for(String s:bills){
			SuperMarket.cls();
			System.out.println(s);
			SuperMarket.enterWait();
		}
	}

	public void cart(){
		int com = -1; boolean invalid = false;
		while(true){
			SuperMarket.cls();
			System.out.println("Cart : \n");
			
			String Bill = "Bill Date : " + LocalDate.now() + "\n";
			int totalPrice = 0;
			for(CartItem item:cart){
				Product temp = Product.getProductById(item.id);
				System.out.println( "ID: "+ temp.id +"   "+ temp.name + " Price : " + temp.price + " , Count : " + item.count);
				Bill += "ID: "+ temp.id +"   "+ temp.name + " Price : " + temp.price + " , Count : " + item.count + "\n";
				totalPrice += temp.price*item.count;
				System.out.println();
			}

			Bill += "Overall Cost of Bill : " + totalPrice;

			com = SuperMarket.page(invalid, "Overall Cost of Products in cart : " + totalPrice , "Back", "Remove Product From Cart", "Change Product Quantity" , "Check out" );

			invalid = false;
			if(com == 1){
				int prodId = SuperMarket.intInput("Product Id : ");
				int rmv = removeProd(prodId);
				if(rmv != -1){
					System.out.println("Product Removed Successfully!");
					Product.getProductById(prodId).stock += rmv;
				}else{
					System.out.print("Product NotFound in cart!");
				}
				SuperMarket.enterWait();
			}else if(com == 2){
				int prodId = SuperMarket.intInput("Product Id : ");
				Product prod = Product.getProductById(prodId);
				int ind = hasProd(prodId);
				if(ind != -1){
					int count = SuperMarket.intInput("Enter New Count : ");
					int prevCount = cart.get(ind).count;
					if(prod.stock >= count-prevCount){
						cart.get(ind).count = count;
						Product.getProductById(prodId).stock +=  prevCount - count;
					}else{
						System.out.println("Requested Stock Unavailable");
						SuperMarket.enterWait();
					}
				}else{
					System.out.print("Product Not Fount in cart");
					SuperMarket.enterWait();
				}
			}else if(com == 3){
				System.out.print("Confirm Purchase (y/n) : ");
				String conf =  SuperMarket.s.nextLine();
				if(conf.equals("y")){
					if(credit >= totalPrice){
						afterPurchase(totalPrice);
						System.out.println("Purchase Successful");
						clearCart();
						bills.add(0,Bill);
						SuperMarket.enterWait();
						break;
					}else{
						System.out.println("Insufficient Fund");
					}
					SuperMarket.enterWait();
				}
			}else if(com == 0){
				break;
			}else{
				invalid = true;
			}

		}
	}

	public void welcome(){
		int com = -1; boolean invalid = false;
		while(true){
			SuperMarket.cls();
			com = SuperMarket.page(invalid, "Welcome , " + name, "Main Menu", "Go in Store", "View Cart", "Credit Available", "Previous Billings");
			invalid = false;
			if(com == 1){
				products();
			}else if(com == 2){
				cart();
			}else if(com == 3){
				cred();
			}else if(com == 4){
				showBill();
			}else if(com == 0){
				break;
			}else{
				invalid = true;
			}

		}
	}
}

class Admin extends User{
	static ArrayList<Admin> users = new ArrayList<Admin>();

	public Admin(String name,String email, String password){
		super(name, email, password);
	}

	static{
		Admin adm = new Admin("Harikrishna D","hari", "1234");
		users.add(adm);
	}

	public static int getUser(String email){
		for(int i = 0; i < users.size(); i++){
			if(users.get(i).email.equals(email)){
				return i;
			}
		}
		return -1;
	}

	public static int authUser(String email, String password){
		for(int i = 0; i < users.size(); i++){
			if(users.get(i).email.equals(email)){
				System.out.println(users.get(i).email);
				if(users.get(i).password.equals(password)){
					return i;
				}
			}
		}
		return -1;
	}

	public static int login(){
		SuperMarket.cls();
		System.out.println("Enter Login Credentials");
		System.out.println("Users : " + users.size());
		System.out.print("Email : ");
		String email = SuperMarket.s.nextLine();
		System.out.print("Password : ");
		String password = SuperMarket.s.nextLine();
		int ind = authUser(email, password);
		if(ind == -1){System.out.println("Entered Credentials Wrong");}
		else{users.get(ind).welcome();return ind;}
		SuperMarket.enterWait();
		return ind;
	}

	public void addUser(){
		System.out.println("Add Admin/Customer");
		String userType = SuperMarket.input("User Type (A - Admin/ C - Customer) : ");
		String userName = SuperMarket.input("User Name : ");
		String userMail = SuperMarket.input("User Mail : ");
		String userPass = SuperMarket.input("User Password : ");
		String conf = SuperMarket.input("Confirm Entered Data ? (y/n) : ");
		if(conf.equals("y")){
			if(userType.equals("A")){
				Admin adm = new Admin(userName, userMail, userPass);
				users.add(adm);
				System.out.println("Admin Added Successfully");
			}else if(userType.equals("C")){
				Customer cus = new Customer(userName, userMail, userPass);
				Customer.users.add(cus);
				System.out.println("Customer Added Successfully");
			}
		}else{
			System.out.println("Entered Details Discarded");
		}
		SuperMarket.enterWait();
	}


	public void credUser(){
		SuperMarket.cls();
		System.out.println("Add Customer Credit");
		String userMail = SuperMarket.input("User Mail : ");
		int ind = Customer.getUser(userMail);
		if(ind != -1){
			System.out.println("Available Credit : " + Customer.users.get(ind).credit);
			int credit = SuperMarket.intInput("Enter the ammount to add : ");
			Customer.users.get(ind).credit += credit;
			System.out.println("Ammount Credited Successfully");
		}else{
			System.out.println("User Not Found");
		}
		SuperMarket.enterWait();
	}

	public void addProd(){
		SuperMarket.cls();
		System.out.println("Add New Product");
		String prodName = SuperMarket.input("Product Name : ");
		int price = SuperMarket.intInput("Product Price : ");
		int stock = SuperMarket.intInput("Product Stock : ");
		String conf = SuperMarket.input("Confirm ? (y/n) : ");
		if(conf.equals("y")){
			Product p = new Product(prodName, price, stock);
			Product.products.add(p);
		}else{
			System.out.println("Entered Details Discarded");
		}
		SuperMarket.enterWait();
	}

	public void modProd(){
		SuperMarket.cls();
		System.out.println("Modify Product");
		int prodId = SuperMarket.intInput("Product ID : ");
		Product curProd = Product.getProductById(prodId);
		if(curProd != null){
			System.out.println("Product : ");
			curProd.display(0);
			int com = SuperMarket.intInput("Value to Change : \n1 - Product Name\n2 - Product Price\n3 - Product Stock\n4 - Remove Product\nChoise : ");
			if(com == 1){
				String newName = SuperMarket.input("Enter New Name : ");
				curProd.name = newName;
				System.out.println("Product Name Changed Successfully");
			}else if(com == 2){
				int newPrice = SuperMarket.intInput("Enter New Price : ");
				curProd.price = newPrice;
				System.out.println("Product Price Changed Successfully");
			}else if(com == 3){
				int newStock = SuperMarket.intInput("Enter New Stock : ");
				curProd.stock = newStock;
				System.out.println("Product Stock Changed Successfully");
			}else if(com == 4){
				Product.removeProductById(curProd.id);
				System.out.println("Product Removed Successfully");
			}else{
				System.out.println("Invalid Option");
			}
			SuperMarket.enterWait();
		}else{
			System.out.println("Product Not Found !");
			SuperMarket.enterWait();
		}
	}

	public void dispAnalytics(ArrayList<Product> prods, int com, String topic){
		SuperMarket.cls();
		for(Product p:prods){
			p.display(com);
		}
		SuperMarket.enterWait();
	}

	public void dispAnalytics(ArrayList<Customer> custs, String topic){
		SuperMarket.cls();
		for(Customer c:custs){
			System.out.println("Customer Name : " + c.name + "\nCustemer mail : " + c.email + "\nTotal Purchase Price : " + c.totalPurchasePrice +"\n");
		}
		SuperMarket.enterWait();
	}

	public void viewAdvAnalytics(){
		int com = -1; boolean invalid = false;
		while(true){
			SuperMarket.cls();
		com = SuperMarket.page(invalid, "Advanced Analytics", "Main Menu", "Search", "Less Stock", "Unbought Products", "Highly Active Customers");
			invalid = false;
			if(com == 1){
				dispAnalytics(Product.search(SuperMarket.input("Key word : ")), 0, "Search Results");
			}else if(com == 2){
				dispAnalytics(Product.sortByStock(), 1, "Sorted By Stock (Low to high)");
			}else if(com == 3){
				dispAnalytics(Product.sortByPurchase(), 2, "Sorted By No of Units Bought (Low to high)");
			}else if(com == 4){
				dispAnalytics(Customer.sortByPurchase(), "Customers (Most Buy to Least Buy)");
			}else if(com == 0){
				break;
			}else{
				invalid = true;
			}
		}
	}

	public void welcome(){
		int com = -1; boolean invalid = false;
		while(true){
			SuperMarket.cls();
			com = SuperMarket.page(invalid, "Welcome , " + name, "Main Menu", "Add Customer/Admin", "Add Credit to User","Add Product", "Modify Product", "Advanced Analytics");
			invalid = false;
			if(com == 1){
				addUser();
			}else if(com == 2){
				credUser();
			}else if(com == 3){
				addProd();
			}else if(com == 4){
				modProd();
			}else if(com == 5){
				viewAdvAnalytics();
			}else if(com == 0){
				break;
			}else{
				invalid = true;
			}
		}
	}


}

class Product{
	static int prodId = 1;
	static ArrayList<Product> products = new ArrayList<Product>();

	int id;
	String name;
	String disc;
	int price;
	int stock;
	int purchase;

	// Adding products
	static{
		String[] names = {"Complan 500g", "Boost 500g", "Maida 250g", "Maida 500g", "Butter 200g", "Butter 500g", "Urad Dhal 500g", "Chilli Powder 250g"};
		int[] prices = {150, 180, 20, 40, 50, 250, 50, 56};
		int[] stocks = {15, 18, 12, 9, 8, 15, 5, 10};
		for(int i = 0; i < names.length; i++){
			products.add(new Product(names[i], prices[i], stocks[i]));
		}
	}

	public static ArrayList<Product> getProducts(){
		ArrayList<Product> availProducts = new ArrayList<Product>();
		for(Product p: products){
			if(p.stock > 0){
				availProducts.add(p);
			}
		}

		return availProducts;
	}

	public Product(String name, int price, int stock){
		this.id = prodId++;
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.purchase = 0;
	}

	public static Product getProductById(int id){
		for(int i = 0; i < products.size(); i++){
			if(products.get(i).id == id){
				return products.get(i);
			}
		}
		return null;
	}

	public void display(){
		System.out.println("Product ID : " + id + "\nProduct Name : " + name + "\nPrice : " + price + "\nStock : " + stock);
	}

	public void display(int opt){
		if(opt == 0){
			System.out.println("Product ID : " + id + "\nProduct Name : " + name + "\nPrice : " + price + "\nStock : " + stock + "\nPurchase : " + purchase + "\n");
		}else if(opt == 1){
			System.out.println("Product ID : " + id + "\nProduct Name : " + name + "\nStock : "  + stock + "\n");
		}else if(opt == 2){
			System.out.println("Product ID : " + id + "\nProduct Name : " + name + "\nPurchase : " + purchase + "\n");
		}else if(opt == 3){
			System.out.println("Product ID : " + id + "\nProduct Name : " + name + "\nPrice : " + price + "\n");
		}

	}

	public static ArrayList<Product> search(String str){
		ArrayList<Product> result = new ArrayList<Product>();
		for(int i = 0; i < products.size(); i++){
			if(products.get(i).name.toLowerCase().contains(str.toLowerCase())){
				result.add(products.get(i));
			}
		}
		return result;
	}

	public static ArrayList<Product> sortByName(){
		ArrayList<Product> res = new ArrayList<Product>(products);
		for(int i = 0; i < products.size(); i++){
			for(int j = i+1; j < products.size(); j++){
				if(res.get(i).name.compareTo(res.get(j).name) > 0){
					Product temp = res.get(i);
					res.set(i, res.get(j));
					res.set(j, temp);
				}
			}	
		}
		return res;
	}

	public static ArrayList<Product> sortByPrice(){
		ArrayList<Product> res = new ArrayList<Product>(products);
		for(int i = 0; i < products.size(); i++){
			for(int j = i+1; j < products.size(); j++){
				if(res.get(i).price > res.get(j).price){
					Product temp = res.get(i);
					res.set(i, res.get(j));
					res.set(j, temp);
				}
			}	
		}
		return res;
	}
	
	public static ArrayList<Product> sortByStock(){
		ArrayList<Product> res = new ArrayList<Product>(products);
		for(int i = 0; i < products.size(); i++){
			for(int j = i+1; j < products.size(); j++){
				if(res.get(i).stock > res.get(j).stock){
					Product temp = res.get(i);
					res.set(i, res.get(j));
					res.set(j, temp);
				}
			}	
		}
		return res;
	}

	public static ArrayList<Product> sortByPurchase(){
		ArrayList<Product> res = new ArrayList<Product>(products);
		for(int i = 0; i < products.size(); i++){
			for(int j = i+1; j < products.size(); j++){
				if(res.get(i).purchase > res.get(j).purchase){
					Product temp = res.get(i);
					res.set(i, res.get(j));
					res.set(j, temp);
				}
			}	
		}
		return res;
	}

	public static void removeProductById(int id){
		for(int i = 0; i < products.size(); i++){
			if(products.get(i).id == id){
				products.remove(i);
			}
		}
	}
}