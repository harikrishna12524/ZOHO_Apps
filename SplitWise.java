import java.util.*;
import java.time.LocalDateTime;

public class SplitWise{
	static Scanner s = new Scanner(System.in);

	// Helper Methods
	public static void cls(){
		try{
			new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		}catch(Exception e){

		}
	}

	public static String input(String words){
		System.out.print(words);
		return s.nextLine();
	}

	public static int intInput(String words){
		System.out.print(words);
		int num = s.nextInt(); s.nextLine();
		return num;
	}

	public static void enterWait(){
		System.out.print("Press Enter to Continue ...");
		s.nextLine();
	}

	public static int page(boolean invalid, String heading, String back, String ... options){
		System.out.println(heading);
		
		for(int i = 0; i < options.length; i++){
			System.out.println(i+1 + " - " + options[i]);
		}
		System.out.println("0 - " + back);

		if(invalid){System.out.println("Invalid Input");}

		return intInput("Choise : ");
	}

	public static void userLogin(){
		cls();
		System.out.println("Enter Login Credentials");
		String userName = input("Email : ");
		String password = input("Password : ");
		User curUser = User.authUser(userName, password);
		if(curUser == null){
			System.out.println("Invalid Login Credentials");
			enterWait();
		}else{
			curUser.menu();
		}
	}

	public static void welcome(){
		int com = -1; boolean invalid = false;
		loop : while(true){
			cls();
			com = page(invalid, "Welcome to Splitwise", "Exit", "User Login");
			switch(com){
				case 1:
					userLogin();
					break;
				case 0:
					break loop;
				default:
					invalid = true;
			}
		}
	}
	
	public static void main(String[] args){
		welcome();
	}
}

class User{
	static ArrayList<User> users = new ArrayList<User>();

	static{
		users.add(new User("Harikrishna D", "hari", "1234", "9597653107"));
		users.add(new User("Jeevan S", "jeevan", "1234", "9445842664"));
		users.add(new User("Jagan S", "jagan", "1234", "9385403843"));
		users.add(new User("Nivedha D", "niva", "1234", "9486363730"));
	}

	String name;
	String email;
	String password;
	String contact;
	int wallet;
	HashMap<User, Integer> moneyOwed = new HashMap<User, Integer>();
	ArrayList<Expense> addedExpenses = new ArrayList<Expense>();
	ArrayList<Expense> taggedExpenses = new ArrayList<Expense>();
	ArrayList<Group> groups = new ArrayList<Group>();
	ArrayList<Transaction> transactions = new ArrayList<Transaction>();

	public static User getUserByContact(String contact){
		for(int i = 0; i < users.size(); i++){
			if(users.get(i).contact.equals(contact)){
				return users.get(i);
			}
		}
		return null;
	}

	public static User authUser(String email, String password){
		for(User usr:users){
			if(usr.email.equals(email) && usr.password.equals(password)){
				return usr;
			}
		}
		return null;
	}

	public boolean equals(User u){
		return this.email==u.email;
	}

	public User(String name, String email, String password, String contact){
		this.name = name;
		this.email = email;
		this.password = password;
		this.contact = contact;
	}

	public void createGroup(String GroupName){
		Group newGroup = new Group(GroupName, this);
	}

	public void dispTrans(){
		for(Transaction t:transactions){
			System.out.println("\nTransaction ID : " + t.id + "\nTransaction Type : " + (t.sender.equals(this)?"Sent":"Received") + "\nTransaction Amount : " + t.amount + "\nTransaction Date Time : " + t.timeStamp);
		}
	}

	public void dispExpenses(){
		// For Expenses Created By self -- Displays all the invloved Persons
		System.out.println("Expenses Created By Yourself");
		for(Expense e:addedExpenses){
			System.out.println("Title : " + e.name + "\nTotal Amount : " + e.totalAmount + "\nDate and Time : " + e.timeStamp);
			Set<User> owers = e.moneyOwed.keySet();
			for(User usr:owers){
				System.out.printf("%s (%s) owes Rs.%d\n", usr.name, usr.contact, e.moneyOwed.get(usr));
			}
			if(e.expenseGroup==null){
				System.out.println("Individual Transaction");
			}else{
				System.out.println("Group : " + e.expenseGroup.name);
			}
		}

		// For Expeses you have been tagged in -- displays only his stuff.
		System.out.println("Expenses Created By Yourself");
		for(Expense e:addedExpenses){
			System.out.println("Title : " + e.name + "\nTotal Amount : " + e.totalAmount + "\nDate and Time : " + e.timeStamp);
			System.out.printf("You owe %s Rs.%d\n", e.adder.name, e.moneyOwed.get(this));
			if(e.expenseGroup==null){
				System.out.println("Individual Transaction");
			}else{
				System.out.println("Group : " + e.expenseGroup.name);
			}
		}
	}

	public void dispGroup(){
		SplitWise.cls();
		System.out.println("Group Current Present");
		for(int i = 0; i < groups.size(); i++){
			System.out.println(i+1 + " - " + groups.get(i).name);
		}
		int com = -1;boolean invalid = false;
		loop : while(true){
			com = SplitWise.page(invalid, "Options", "Go Back", "Enter Group");
			switch(com){
				case 1:
					int groupInd = SplitWise.intInput("Enter Group Number : ");
					groups.get(groupInd - 1).menu();
					break loop;
				case 0:
					break loop;
				default:
					invalid = true;
			}
		}
	}

	public void createGroup(){
		SplitWise.cls();
		Group g = new Group(SplitWise.input("Enter Group Name : "),this);
		System.out.println("Group has been created");
		SplitWise.enterWait();
	}

	public void menu(){
		int com = -1;boolean invalid = false;
		loop : while(true){
			SplitWise.cls();
			com = SplitWise.page(invalid, "Welcome to Splitwise", "Exit", "Existing Groups", "New Group", "Update Wallet Amount", "Pending Dues", "Transaction History", "Expenses");
			switch(com){
				case 1:
					dispGroup();
					break;
				case 2:
					createGroup();
					break;
				case 0:
					break loop;
				default:
					invalid = true;
			}
		}
	}



}

class Group{
	String name;
	User groupAdmin;
	ArrayList<User> members = new ArrayList<User>();
	ArrayList<Expense> expenses = new ArrayList<Expense>();

	public Group(String name,User groupAdmin){
		this.name = name;
		this.groupAdmin = groupAdmin;

		groupAdmin.groups.add(this);
		members.add(groupAdmin);
	}

	public void addMember(User member){
		members.add(member);
	}

	public void removeMember(User member){
		members.remove(member);
	}

	public boolean hasMember(User member){
		return members.contains(member);
	}

	public void addExpense(Expense expense){
		expenses.add(expense);
	}

	public void menu(){
		int com = -1;boolean invalid = false;
		loop : while(true){
			SplitWise.cls();
			System.out.println("Group : " + name + "\nGroup Members");

			for(int i = 0; i < members.size(); i++){
				User usr = members.get(i);
				System.out.printf(i+1 + " - %s (%s)\n", usr.name, usr.contact);
			}

			com = SplitWise.page(invalid, "Welcome to Splitwise", "Exit", "Add Member" , "Update Wallet Amount", "Pending Dues", "Transaction History", "Expenses");
			switch(com){
				case 1:
					
					break;
				case 0:
					break loop;
				default:
					invalid = true;
			}
		}
	}


	

}

class Expense{
	String name;
	int totalAmount;
	HashMap<User, Integer> moneyOwed;
	User adder;
	Group expenseGroup;
	LocalDateTime timeStamp;

	public Expense(String name, int totalAmount, HashMap<User, Integer> moneyOwed, User adder, Group expenseGroup){
		this.name = name;
		this.totalAmount = totalAmount;
		this.moneyOwed = moneyOwed;
		this.adder = adder;
		this.expenseGroup = expenseGroup;
		this.timeStamp = LocalDateTime.now();

		// Adding the Expense to people accounting for it
		adder.addedExpenses.add(this);
		Set<User> owers = moneyOwed.keySet();
		for(User u:owers){
			u.taggedExpenses.add(this);

			Integer _moneyOwedNew = moneyOwed.get(u);
			if(u.moneyOwed.containsKey(adder)){
				Integer _moneyOwedPrev = u.moneyOwed.get(adder);
				u.moneyOwed.put(adder, _moneyOwedPrev + _moneyOwedNew);
			}else{
				u.moneyOwed.put(adder, _moneyOwedNew);
			}

			_moneyOwedNew *= -1;
			if(adder.moneyOwed.containsKey(u)){
				Integer _moneyOwedPrev = adder.moneyOwed.get(u);
				adder.moneyOwed.put(u, _moneyOwedPrev + _moneyOwedNew);
			}else{
				u.moneyOwed.put(u, _moneyOwedNew);
			}
		}

		// Adding Expense to Repective Group
		expenseGroup.addExpense(this);
	}

	public Expense(String name, int totalAmount, User Ower, int amount, User adder){
		this.name = name;
		this.totalAmount = totalAmount;
		this.moneyOwed = new HashMap<User, Integer>();
		this.adder = adder;
		this.expenseGroup = null;
		this.timeStamp = LocalDateTime.now();

		// Adding the Expense to people accounting for it
		adder.addedExpenses.add(this);
		moneyOwed.put(Ower, amount);
		Set<User> owers = moneyOwed.keySet();
		for(User u:owers){
			u.taggedExpenses.add(this);

			Integer _moneyOwedNew = moneyOwed.get(u);
			if(u.moneyOwed.containsKey(adder)){
				Integer _moneyOwedPrev = u.moneyOwed.get(adder);
				u.moneyOwed.put(adder, _moneyOwedPrev + _moneyOwedNew);
			}else{
				u.moneyOwed.put(adder, _moneyOwedNew);
			}

			_moneyOwedNew *= -1;
			if(adder.moneyOwed.containsKey(u)){
				Integer _moneyOwedPrev = adder.moneyOwed.get(u);
				adder.moneyOwed.put(u, _moneyOwedPrev + _moneyOwedNew);
			}else{
				u.moneyOwed.put(u, _moneyOwedNew);
			}
		}

	}

}

class Transaction{
	static int transId = 1;

	int id;
	int amount;
	User sender;
	User receiver;
	LocalDateTime timeStamp;

	public Transaction(int amount, User sender, User receiver){
		this.id = transId++;
		this.amount = amount;
		this.sender = sender;
		this.receiver = receiver;
		this.timeStamp = LocalDateTime.now();

		sender.transactions.add(this);
		receiver.transactions.add(this);
	}

}