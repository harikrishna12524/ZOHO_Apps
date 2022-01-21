import java.util.*;
import java.time.LocalDate;

public class VehicleRental{
	static Scanner s = new Scanner(System.in);
	public static void cls(){
		try{
			new ProcessBuilder("cmd","/c", "cls").inheritIO().start().waitFor();
		}catch(Exception e){

		}
	}

	public static void enterWait(){
		System.out.print("Press Enter to Continue...");
		s.nextLine();
	}

	public static int intInput(String msg){
		System.out.print(msg);
		int val = s.nextInt();s.nextLine();
		return val;
	}

	public static String input(String msg){
		System.out.print(msg);
		String val = s.nextLine();
		return val;
	}

	public static int page(boolean invalid, String head, String back, String ... options){
		System.out.println( "    " + head);
		for(int i = 0; i < options.length; i++){
			System.out.println(i+1 + " - " + options[i]);
		}
		System.out.println("0 - " + back);
		if(invalid){
			System.out.println("Invalid Option.");
		}
		int com = intInput("Choise : ");
		return com;
	}

	public static void welcome(){
		int com = -1;boolean invalid = false;
		loop : while(true){
			cls();
			com = page(invalid, "Welcome To Vehicle Rental", "Exit", "User", "Admin" );
			switch(com){
				case 1:
					Borrower.userLogin();
					break;
				case 2:
					Admin.userLogin();
					break;
				case 0:
					break loop;
				default:
					invalid = true;
			}
		}
	}

	public static void main(String[] args){
		// System.out.println("Words");
		welcome();
	}
}

class User{
	String name;
	String email;
	String password;

	public User(String name, String email, String password){
		this.name = name;
		this.email = email;
		this.password = password;
	}
}

class Borrower extends User{
	static ArrayList<Borrower> users = new ArrayList<Borrower>(); 

	int deposit;
	ArrayList<String> prevRentals = new ArrayList<String>(); 
	ArrayList<Integer> cart = new ArrayList<Integer>();
	Vehicle car;
	Vehicle bike;

	public Borrower(String name, String email, String password){
		super(name, email, password);
		this.deposit = 30000;
		this.car = null;
		this.bike = null;
	}

	static{
		Borrower b = new Borrower("Harikrishna D", "hari", "1234");
		users.add(b);
	}

	public static Borrower getUser(String username, String password){
		for(int i = 0; i < users.size(); i++){
			Borrower temp  = users.get(i);
			if(temp.email.equals(username)){
				if(temp.password.equals(password)){
					return temp;
				}
			}
		}
		return null;
	}

	public static void userLogin(){
		VehicleRental.cls();
		System.out.println("Borrower Login");
		String email = VehicleRental.input("Enter User Email : ");
		String password = VehicleRental.input("Enter Password : ");
		Borrower curBor = getUser(email, password);
		if(curBor != null){
			curBor.menu();
		}else{
			System.out.println("Enter User Credentials are Wrong");
			VehicleRental.enterWait();
		}
	}

	public void removeFromCart(int id){
		for(int i = 0; i < cart.size(); i++){
			if(cart.get(i) == id){
				cart.remove(i);
			}
		}
	}

	public int daysBetween(LocalDate d1, LocalDate d2){
		int sol = 0;
		while(!d1.equals(d2)){
			sol+= 1;
			d1.plusDays(1);
			if(sol > 30){
				return 30;
			}
		}
		return sol;
	}

	public void rental(){
		int com = -1; boolean invalid = false;
		while(true){
			VehicleRental.cls();
			System.out.println("    Vehicle Rentals");
			ArrayList<Vehicle> vehicles = Vehicle.getVehicles();
			for(Vehicle v:vehicles){
				v.display();
			}
			com = VehicleRental.page(invalid, "\nOptions", "Back" ,"Add Vehicle to cart");
			
			if(com == 1){
				int vehicleId = VehicleRental.intInput("Enter Vehicle Id : ");
				Vehicle v = Vehicle.getVehicleById(vehicleId);
				if(vehicles.contains(v)){
					if(cart.contains(v.id)){
						System.out.println("Vehicle Already in Cart");
					}else if((car != null && v.isCar) || (bike != null && !v.isCar)){
						System.out.println((v.isCar?"Car":"Bike") + " cannot be borrowed twice");
					}else{
						cart.add(v.id);
						System.out.println("Vehicle Added To Cart");
					}
					VehicleRental.enterWait();
				}
			}else if(com == 0){
				break;
			}else{

			}
			
		}
	}

	public void cart(){
		int com = -1; boolean invalid = false;
		while(true){
			VehicleRental.cls();
			System.out.println("    Vehicle Rentals");

			for(int v:cart){
				Vehicle.getVehicleById(v).display();
			}

			com = VehicleRental.page(invalid, "\nOptions", "Back" ,"Remove Vehicle", "Rent Vehicle");
			
			if(com == 1){
				int vehicleId = VehicleRental.intInput("Enter Vehicle Id : ");
				if(cart.contains(vehicleId)){
					removeFromCart(vehicleId);
				}else{
					System.out.println("Vehicle Unavailable in cart");
					VehicleRental.enterWait();
				}
			}else if(com == 2){
				int vehicleId = VehicleRental.intInput("Enter Vehicle Id : ");
				if(cart.contains(vehicleId)){
					Vehicle v = Vehicle.getVehicleById(vehicleId);
					if(!((car == null && v.isCar) || (bike == null && !v.isCar))){
						System.out.println("Cant Borrow a "+(v.isCar?"Car":"Bike") + "again ! Requested type already borrowed !");
					}else if( !v.isAvailable() ){
						System.out.println("Vehicle Unavailable");
					}else if(!((deposit >= 10000 && v.isCar) || (deposit >=3000 && !v.isCar))){
						System.out.println("Required Deposite Unavailable");
					}else{
						System.out.println((v.isCar?"Car":"Bike")+ "'s one days rental is " + v.rental);
						String conf = VehicleRental.input("Confirm (y/n) : ");
						if(conf.equals("y")){
							if(v.isCar){
								car = v;
							}else{
								bike = v;
							}
							v.borrower = email;
							v.borrowDate = LocalDate.now();
							removeFromCart(vehicleId);
							String prev = "Vehicle Booked : " + v.name + "\nBorrowed date : " + v.borrowDate + "\n";
							prevRentals.add(prev);
							System.out.println("Vehicle Borrowed Successfully, Return Before " + v.borrowDate.plusDays(1));
						}
					}
					VehicleRental.enterWait();
				}else{
					System.out.println("Invalid Input");
				}
			}else if(com == 0){
				break;
			}else{

			}
		}
	}

	public void borrowed(){
		int com = -1; boolean invalid = false;
		while(true){
			VehicleRental.cls();
			System.out.println("Return Car/Bike\n");
			if(car != null){
				car.display();
				System.out.println("Borrowed On : " + car.borrowDate + "\n");
			}else{
				System.out.println("No car borrowed");
			}

			if(bike != null){
				bike.display();
				System.out.println("Borrowed On : " + bike.borrowDate);
			}else{
				System.out.println("No Bike borrowed");
			}



			com = VehicleRental.page(invalid, "\nOptions", "Back" ,"Return Vehicle", "Report Vehicle Lost", "Extend Vehicle Time");
			if(com == 1){
				int vehicleId = VehicleRental.intInput("Enter Vehicle ID : ");
				if(car.id == vehicleId || bike.id == vehicleId){
					Vehicle v = car.id==vehicleId?car:bike;
					int charge = v.rental;
					LocalDate date = LocalDate.parse(VehicleRental.input("Enter Return Date (YYYY-MM-DD): "));
					if(v.borrowDate.equals(date)){
						int dist = VehicleRental.intInput("Distance Travelled : ");
						String damage = VehicleRental.input("Enter Damage Significance : ");
						if(dist > 500){
							charge += (int)0.15*v.rental;
							System.out.println("Vehicle Travelled More Than Optimal Value Extra 15% will be detucted");
						}
						if(damage.equals("NONE")){
							System.out.println("No Charges Deducted");
						}else{
							if(damage.equals("LOW")){
								charge += v.fine * 25/100;
							}else if(damage.equals("MID")){
								charge += v.fine * 50/100;
							}else if(damage.equals("HIGH")){
								charge += v.fine * 75/100;
							}
						}
						VehicleRental.enterWait();
						while(true){
							VehicleRental.cls();
							int opt = VehicleRental.intInput("Total Charge is Rs." + charge + "\n1 - Cash" + "\n2 - Deposit" +"\nChoise : "  );
							if(opt == 2){
								if(deposit >= charge){
									deposit -= charge;
									System.out.println("Thankyou for your business");
									if(v.isCar){
										car = null;
									}else{
										bike  = null;
									}
									v.borrower = null; v.borrowDate = null;
									break;
								}else{
									System.out.println("Insufficient Balance");
								}
							}else if(opt == 1){
									System.out.println("Thankyou for your business");
									if(v.isCar){
										car = null;
									}else{
										bike  = null;
									}
									v.borrower = null; v.borrowDate = null;
									break;
							}else{
									System.out.println("Invalid Input");	
							}
							VehicleRental.enterWait();
						}
					}else{
						System.out.println("Returned Vehicle Late");
						System.out.println("Deposit will not be given back.");
						deposit = 0;
					}
				}else{
					System.out.println("Entered Vehicle not borrowed");
				}
				VehicleRental.enterWait();
			}else if(com == 2){
				int vehicleId = VehicleRental.intInput("Enter Vehicle ID : ");
				Vehicle v;
				if(car.id == vehicleId || bike.id == vehicleId){
					v = car.id==vehicleId?car:bike;
					
				while(true){
					VehicleRental.cls();
					int opt = VehicleRental.intInput("Total Charge For Loosing the Vehicle is Rs." + v.fine + "\n1 - Cash" + "\n2 - Deposit" +"\nChoise : "  );
					if(opt == 2){
						if(deposit >= v.fine){
							deposit -= v.fine;
							System.out.println("Fine Deducted !");
							if(v.isCar){
								car = null;
							}else{
								bike  = null;
							}
							Vehicle.removeVehicle(v);
							break;
						}else{
							System.out.println("Insufficient Balance");
						}
					}else if(opt == 1){
							System.out.println("Fine Payed !");
							if(v.isCar){
								car = null;
							}else{
								bike  = null;
							}
							Vehicle.removeVehicle(v);
							break;
					}else{
							System.out.println("Invalid Input");	
					}
					VehicleRental.enterWait();
						}}else{
							System.out.println("Invalid Input");
						}
			}else if(com == 3){
				int vehicleId = VehicleRental.intInput("Enter Vehicle ID : ");
				Vehicle v;
				if(car.id == vehicleId || bike.id == vehicleId){
					v = car.id==vehicleId?car:bike;
					if(v.extend < 3){
						v.extend++;
						v.borrowDate =  v.borrowDate.plusDays(1);
						if(deposit > 10000){
							deposit -= v.rental;
							System.out.println("Extended Date Return Vehicle On : " + v.borrowDate);
						}else{
							System.out.println("Cannot Extend Insufficient Deposit");
						}
					}else{
						System.out.println("Extended Maximum Limit! Please return the vehicle");
					}
				}else{
					System.out.println("Invalid Input");
				}
				VehicleRental.enterWait();
			}else if(com == 0){
				break;
			}else{
				invalid = true;
			}
		}
	}

	public void previousRental(){
		for(int i = 0; i < prevRentals.size(); i++){
			VehicleRental.cls();
			System.out.println(prevRentals.get(i));
			VehicleRental.enterWait();
		}
	}

	public void menu(){
		int com = -1;boolean invalid = false;
		loop : while(true){
			VehicleRental.cls();
			com = VehicleRental.page(invalid, "Welcome ," + name, "MainMenu", "View Vehicles","Cart" ,"Post Borrowal Transactions", "Previous Rentals");
			switch(com){
				case 1:
					rental();
					break;
				case 2:
					cart();
					break;
				case 3:
					borrowed();
					break;
				case 4:
					previousRental();
					break;
				case 0:
					break loop;
				default:
					invalid = true;
			}
		}
	}	
}

class Admin extends User{
	static ArrayList<Admin> users = new ArrayList<Admin>();

	public Admin(String name, String email, String password){
		super(name, email, password);
	}
	
	static{
		Admin b = new Admin("Harikrishna D", "hari", "1234");
		users.add(b);
	}
	
	public static Admin getUser(String username, String password){
		for(int i = 0; i < users.size(); i++){
			Admin temp  = users.get(i);
			if(temp.email.equals(username)){
				if(temp.password.equals(password)){
					return temp;
				}
			}
		}
		return null;
	}

	public static void userLogin(){
		VehicleRental.cls();
		System.out.println("Borrower Login");
		String email = VehicleRental.input("Enter User Email : ");
		String password = VehicleRental.input("Enter Password : ");
		Admin curAdm = getUser(email, password);
		if(curAdm != null){
			curAdm.menu();
		}else{
			System.out.println("Enter User Credentials are Wrong");
			VehicleRental.enterWait();
		}
	}

	public void addVehicle(){
		System.out.println("Add Vehicle");
		String name = VehicleRental.input("Vehicle Name : ");
		String number = VehicleRental.input("Vehicle Number Plate");
		boolean isCar = VehicleRental.input("Vehicle is Car (y/n) : ").equals("y")?true:false;
		int rental = VehicleRental.intInput("Vehicle Rental : ");
		int fine = VehicleRental.intInput("Vehicle Fine : ");
		if(VehicleRental.input("Confirm (y/n) : ").equals("y")){
			Vehicle.addVehicle(name, number, isCar, rental, fine);
			System.out.println("Vehicle Added");
		}else{
			System.out.println("Vehicle Discarded");
		}
	}

	public void modVehicle(){
		System.out.print("Modify Vehicle");
		
	}

	public void menu(){
		int com = -1;boolean invalid = false;
		loop : while(true){
			VehicleRental.cls();
			com = VehicleRental.page(invalid, "Welcome ," + name, "MainMenu", "Add Vehicles","Modify Vehicle" ,"Post Borrowal Transactions", "Previous Rentals");
			switch(com){
				case 1:
					addVehicle();
					break;
				case 2:
					// modVehicle();
					break;
				case 3:
					// searchVehicle();
					break;
				case 4:
					// credUser();
					break;
				case 0:
					break loop;
				default:
					invalid = true;
			}
		}
	}	
}

class Vehicle{
	static int vehId = 1;
	static ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>(); 

	String name;
	String numberPlate;
	int distanceTravelled;
	boolean isCar;
	int rental;
	int id;
	String borrower;
	LocalDate borrowDate;
	int fine;
	int extend;

	public Vehicle(String name, String numberPlate, boolean isCar, int rental, int fine){
		this.name = name;
		this.numberPlate = numberPlate;
		this.isCar = isCar;
		this.distanceTravelled = 0;
		this.id = vehId++;
		this.borrower = null;
		this.borrowDate = null;
		this.rental = rental;
		this.fine = fine;
		this.extend = 0;
	}

	static{
		Vehicle v = new Vehicle("Hyndai i10", "TN 45 BH 0389", true, 3000, 5000);
		vehicles.add(v);
		v = new Vehicle("Honda Shine", "TN 75 AS 8541", false, 1200, 3000);
		vehicles.add(v);
		v = new Vehicle("Lanborgini", "TN 36 AK 1313", true, 10000, 100000);
		vehicles.add(v);
		v = new Vehicle("Hyndai i20", "TN 45 BH 0389", true, 3200, 5000);
		vehicles.add(v);
	}

	public void display(){
		System.out.println("\nVehicle ID : " + id + "\nVehicle Name : " + name + "\nVehicle Number : " + numberPlate + "\nVehicle Type : " + (isCar?"Car":"Bike") + "\nRental PerDay : " + rental + "\n");
	}

	public void display(int one){
		System.out.println("\nVehicle ID : " + id + "\nVehicle Name : " + name + "\nVehicle Number : " + numberPlate + "\nVehicle Type : " + (isCar?"Car":"Bike") + "\nVehicle Travelled Since Service : " + distanceTravelled + "\n");
		if(borrower!= null ){
			System.out.println("Borrower ID : " + borrower + "\nBorrowed Date : " + borrowDate);
		}
	}


	public static Vehicle getVehicleById(int id){
		for(Vehicle v:vehicles){
			if(v.id == id){return v;}
		}
		return null;
	}

	public static ArrayList<Vehicle> getVehicles(){
		ArrayList<Vehicle> res = new ArrayList<Vehicle>();
		for(Vehicle v:vehicles){
			if(v.borrower == null && (v.isCar && v.distanceTravelled < 3000) || (!v.isCar && v.distanceTravelled < 1500) ){
				res.add(v);
			}
		}
		return res;
	}
	
	public static void removeVehicle(Vehicle v){
		for(int i = 0; i < vehicles.size(); i++){
			if(vehicles.get(i).id == v.id){
				vehicles.remove(i);
			}
		}
	}

	public static void addVehicle(String name, String numberPlate, boolean isCar, int rental, int fine){
		vehicles.add(new Vehicle(name, numberPlate, isCar, rental, fine));
	}

	public boolean isAvailable(){
		if(borrower==null && distanceTravelled < 500){
			return true;
		}
		return false;
	}

}