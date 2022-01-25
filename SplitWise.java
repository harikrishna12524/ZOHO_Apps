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

		User me = users.get(0);
		Group g1 = new Group("Goa Trip", me);
		for(int i =1; i < 4; i++){
			g1.addMember(users.get(i));
		}

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
		SplitWise.enterWait();
	}

	public void dispExpenses(){
		// For Expenses Created By self -- Displays all the invloved Persons
		if(addedExpenses.size() == 0){
			System.out.println("No Expenses added by you !");
		}else{
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
		}

		// For Expeses you have been tagged in -- displays only his stuff.
		if(taggedExpenses.size()==0){
			System.out.println("You have not been added in any expenses");
		}
		else{
			System.out.println("Expenses You've been Tagged in");
			for(Expense e:taggedExpenses){
				System.out.println("Title : " + e.name + "\nTotal Amount : " + e.totalAmount + "\nDate and Time : " + e.timeStamp);
				System.out.printf("You owe %s Rs.%d\n", e.spender.name, e.moneyOwed.get(this));
				if(e.expenseGroup==null){
					System.out.println("Individual Transaction");
				}else{
					System.out.println("Group : " + e.expenseGroup.name);
				}
			}
		}
		SplitWise.enterWait();
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
					Group curGrp = groups.get(groupInd - 1);
					if(curGrp.groupAdmin.equals(this)){
						curGrp.menu();
					}else{
						curGrp.menu(1);
					}
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

	public void updateWallet(){
		SplitWise.cls();
		System.out.println("Existing Balance in the Account : " + wallet);
		int amount = SplitWise.intInput("Enter Money to add");
		System.out.println("Updated Balance in the Account : " + (wallet+=amount));
		SplitWise.enterWait();
	}

	public void pendingDues(){
		SplitWise.cls();
		System.out.println("Pending Due to Pay");
		for(User u:moneyOwed.keySet()){
				System.out.println("\nMoney Owed to : " + u.name + " (" + u.contact + ") " + "\nOwed Money : " + moneyOwed.get(u));
			}
		SplitWise.enterWait();
	}

	public void settleDue(){
		while(true){
			SplitWise.cls();
			System.out.println("Pending Due to Pay");
			int i = 0;
			
			// Set<User>
			User[] owedTo = new User[moneyOwed.size()];
			for(User u:moneyOwed.keySet()){
				System.out.println("\nDue : " +(++i)+"\nMoney Owed to : " + u.name + " (" + u.contact + ") " + "\nOwed Money : " + moneyOwed.get(u));
				owedTo[i-1] = u;
			}

			System.out.println("\n0 - Exit");
			int com = SplitWise.intInput("Choise : ");

			if(com <= 0){
				break;
			}else if(com <= owedTo.length){
				User benefitiary = owedTo[com-1];
				int amountOwed = moneyOwed.get(benefitiary);
				System.out.println("Transfer " + amountOwed + " to " + benefitiary.name + " (" + benefitiary.contact + ") ?");
				String conf = SplitWise.input("Confirm Transfer (y/n) : ");
				if(conf.equals("y")){
					Transaction t = new Transaction(amountOwed, this, benefitiary);
					moneyOwed.remove(benefitiary);
					System.out.println("Transaction Complete");
				}else{
					System.out.println("Transaction Cancelled");	
				}
			}else{
				System.out.println("Invalid Choise");
			}
		}


		
	}

	public void menu(){
		int com = -1;boolean invalid = false;
		loop : while(true){
			SplitWise.cls();
			com = SplitWise.page(invalid, "Welcome to Splitwise", "Exit", "Existing Groups", "New Group", "Update Wallet Amount", "Pending Dues", "Transaction History", "Expenses", "Settle Dues");
			switch(com){
				case 1:
					dispGroup();
					break;
				case 2:
					createGroup();
					break;
				case 3:
					updateWallet();
					break;
				case 4:
					pendingDues();
					break;
				case 5:
					dispTrans();
					break;
				case 6:
					dispExpenses();
					break;
				case 7:
					settleDue();
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
		member.groups.add(this);
	}

	public void removeMember(User member){
		members.remove(member);
		member.groups.remove(this);
	}

	public boolean hasMember(User member){
		return members.contains(member);
	}

	public void addExpense(Expense expense){
		expenses.add(expense);
	}

	public void viewMembers(){
		for(int i = 0; i < members.size(); i++){
				User usr = members.get(i);
				System.out.printf(i+1 + " - %s (%s)\n", usr.name, usr.contact);
		}
	}

	public void viewExpense(){
		for(Expense e:expenses){e.disp(1);}
		SplitWise.enterWait();
	}

	public void menu(){
		int com = -1;boolean invalid = false;
		loop : while(true){
			SplitWise.cls();
			System.out.println("Group : " + name + "\nGroup Members");

			viewMembers();

			com = SplitWise.page(invalid, "Welcome to Splitwise", "Exit", "Add Member", "Remove Member","Add Expense", "View Expense");

			if(com == 1){
				String cont = SplitWise.input("Enter Member Contact : ");
				User _member = User.getUserByContact(cont);
				if(_member == null){
					System.out.println("Coundn't find User with contact " + cont);
				}else{
					addMember(_member);
					System.out.println("Member added Sucessfully !");
				}
				SplitWise.enterWait();
			}else if(com == 2){
				String cont = SplitWise.input("Enter Member Contact : ");
				User _member = User.getUserByContact(cont);
				if(_member == null){
					System.out.println("Coundn't find User with contact " + cont);
				}else if(hasMember(_member)){
					removeMember(_member);
					System.out.println("Member Removed Sucessfully !");
				}else{
					System.out.println("Member Not in Group");
				}
				SplitWise.enterWait();
			}else if(com == 3){
				Expense.newExpense(this);
			}else if(com == 4){
				viewExpense();
			}else if(com == 0){
				break;
			}else{
				invalid = true;
			}
					
			
		}
	}

	public void menu(int one){
		int com = -1;boolean invalid = false;
		loop : while(true){
			SplitWise.cls();
			System.out.println("Group : " + name + "\nGroup Members");

			for(int i = 0; i < members.size(); i++){
				User usr = members.get(i);
				System.out.printf(i+1 + " - %s (%s)\n", usr.name, usr.contact);
			}

			com = SplitWise.page(invalid, "Welcome to Splitwise", "Exit", "Add Expense", "View Expenses");

			if(com == 1){
				Expense.newExpense(this);
			}else if(com == 2){
				viewExpense();
			}else if(com == 0){
				break;
			}else{
				invalid = true;
			}
		}
	}
}

class Expense{
	String name;
	int totalAmount;
	HashMap<User, Integer> moneyOwed;
	User spender;
	Group expenseGroup;
	LocalDateTime timeStamp;

	public Expense(String name, int totalAmount, HashMap<User, Integer> moneyOwed, User spender, Group expenseGroup){
		this.name = name;
		this.totalAmount = totalAmount;
		this.moneyOwed = moneyOwed;
		// this.spender = spender;
		this.spender = spender;
		this.expenseGroup = expenseGroup;
		this.timeStamp = LocalDateTime.now();

		// Adding the Expense to people accounting for it
		// spender.addedExpenses.add(this);
		spender.addedExpenses.add(this);
		Set<User> owers = moneyOwed.keySet();
		for(User u:owers){
			
			u.taggedExpenses.add(this);

			Integer _moneyOwedNew = moneyOwed.get(u);
			if(u.moneyOwed.containsKey(spender)){
				Integer _moneyOwedPrev = u.moneyOwed.get(spender);
				u.moneyOwed.put(spender, _moneyOwedPrev + _moneyOwedNew);
			}else{
				u.moneyOwed.put(spender, _moneyOwedNew);
			}

			// _moneyOwedNew *= -1;
			// if(spender.moneyOwed.containsKey(u)){
			// 	Integer _moneyOwedPrev = spender.moneyOwed.get(u);
			// 	spender.moneyOwed.put(u, _moneyOwedPrev + _moneyOwedNew);
			// }else{
			// 	spender.moneyOwed.put(u, _moneyOwedNew);
			// }
		}

		// Adding Expense to Repective Group
		expenseGroup.addExpense(this);
	}

	public Expense(String name, int totalAmount, User Ower, int amount, User spender){
		this.name = name;
		this.totalAmount = totalAmount;
		this.moneyOwed = new HashMap<User, Integer>();
		this.spender = spender;
		this.expenseGroup = null;
		this.timeStamp = LocalDateTime.now();

		// Adding the Expense to people accounting for it
		spender.addedExpenses.add(this);
		moneyOwed.put(Ower, amount);
		Set<User> owers = moneyOwed.keySet();
		for(User u:owers){
			u.taggedExpenses.add(this);

			Integer _moneyOwedNew = moneyOwed.get(u);
			if(u.moneyOwed.containsKey(spender)){
				Integer _moneyOwedPrev = u.moneyOwed.get(spender);
				u.moneyOwed.put(spender, _moneyOwedPrev + _moneyOwedNew);
			}else{
				u.moneyOwed.put(spender, _moneyOwedNew);
			}

			_moneyOwedNew *= -1;
			if(spender.moneyOwed.containsKey(u)){
				Integer _moneyOwedPrev = spender.moneyOwed.get(u);
				spender.moneyOwed.put(u, _moneyOwedPrev + _moneyOwedNew);
			}else{
				u.moneyOwed.put(u, _moneyOwedNew);
			}
		}

	}

	public void disp(int one){
			System.out.println("Title : " + name + "\nTotal Amount : " + totalAmount + "\nExpense added by : " + spender.name + "\nDate and Time : " + timeStamp);
			Set<User> owers = moneyOwed.keySet();
			for(User usr:owers){
				System.out.printf("%s (%s) owes Rs.%d\n", usr.name, usr.contact, moneyOwed.get(usr));
			}
			if(expenseGroup==null){
				System.out.println("Individual Transaction");
			}else{
				System.out.println("Group : " + expenseGroup.name);
			}
	}

	public static int newExpense(Group group){
		SplitWise.cls();
		System.out.println("Adding an Expense to the Group");
		String name = SplitWise.input("Expense Name : ");

		int amount  = SplitWise.intInput("Total Amount : ");
		
		System.out.println("Choose the Spender");
		group.viewMembers();
		int spenderInd = SplitWise.intInput("Choise : ");
		User spender = group.members.get(spenderInd-1);
		// Add a block to block out other possibilities

		int noOfBenefiters = SplitWise.intInput("No of Expenders : ");
		System.out.print("Enter user id with space (eg. 1 5 2) : ");

		HashMap<User, Integer> moneyOwed = new HashMap<User, Integer>();
		for(int i = 0; i < noOfBenefiters; i++){
			int num = SplitWise.s.nextInt();
			User _member = group.members.get(num-1);
			moneyOwed.put(_member, 0);
		}

		boolean invalid = false;
		while(true){
			SplitWise.cls();
			System.out.println("Expense Name : " + name);
			System.out.println("Spender Name : " + spender.name);
			System.out.println("Total Amount : " + amount + "\n");
			Set<User> _expenders= moneyOwed.keySet();
			ArrayList<User> expenders = new ArrayList<User>();
			for(User u: _expenders){
				System.out.println(u.name + "(" + u.contact + ")");
				expenders.add(u);
			}

			int com = SplitWise.page(invalid,"\nChoose the spliting method", "Discard the Expense", "Split Equally", "Split By Price", "Split By Percentage");

			if(com == 1){
				int splitAmount = amount/expenders.size();
				for(User u: expenders){
					moneyOwed.put(u, splitAmount);
				}
				break;
			}else if(com == 2){
				int totalPercent = 0;
				System.out.println("Enter Share Of Each User in Price");
				for(int i = 0; i < moneyOwed.size()-1; i++){
					User u = expenders.get(i);
					int percent = SplitWise.intInput(u.name + "(" + u.contact + ") : ");
					moneyOwed.put(u, percent);
					totalPercent += percent;
				}
				User u = expenders.get(moneyOwed.size()-1);
				int finalPercent = amount - totalPercent;
				if(finalPercent < 0){
					System.out.println("Invalid Input Detected !");
					SplitWise.enterWait();
					continue;
				}
				System.out.println(u.name + "(" + u.contact + ") : " + finalPercent);
				moneyOwed.put(u, finalPercent);
				SplitWise.enterWait();
				break;
			}else if(com == 3){
				int totalPercent = 0;
				System.out.println("Enter Share Of Each User in Percentage");
				for(int i = 0; i < moneyOwed.size()-1; i++){
					User u = expenders.get(i);
					int percent = SplitWise.intInput(u.name + "(" + u.contact + ") : ");
					moneyOwed.put(u, amount*percent/100);
					totalPercent += percent;
				}
				User u = expenders.get(moneyOwed.size()-1);
				int finalPercent = 100 - totalPercent;
				if(finalPercent < 0){
					System.out.println("Invalid Input Detected !");
					SplitWise.enterWait();
					continue;
				}
				System.out.println(u.name + "(" + u.contact + ") : " + finalPercent);

				moneyOwed.put(u, amount*finalPercent/100);
				SplitWise.enterWait();
				break;
			}
			else if(com == 0){
				return 0;
			}else{
				invalid = true;
			}
			}
			SplitWise.cls();
			System.out.println("Expense Name : " + name);
			System.out.println("Spender Name : " + spender.name);
			System.out.println("Total Amount : " + amount);
			Set<User> _expenders= moneyOwed.keySet();

			for(User u: _expenders){
				System.out.println(u.name + "(" + u.contact + ") : " + moneyOwed.get(u));
			}

			String conf = SplitWise.input("Confirm Expense (y/n) : ");
			if(conf.equals("y")){
				Expense e = new Expense(name, amount, moneyOwed, spender, group);
				// group.addExpense(e);
				System.out.println("Expense Created Successfully");
				SplitWise.enterWait();
			}else{
				System.out.println("Entered Values Discarded");
				SplitWise.enterWait();
			}
		return 0;
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
		sender.wallet -= amount;
		receiver.wallet += amount;

		sender.moneyOwed.remove(receiver);
	}



}
