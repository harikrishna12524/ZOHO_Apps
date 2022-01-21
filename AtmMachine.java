import java.util.*;

public class AtmMachine{

	public static void welcome(){
		Scanner s = new Scanner(System.in);
		byte com;
		

		while(true){
			// Check for clearing Screen
			// System.out.println("Amount in ATM "+ ATM.get_amount());
			// System.out.flush();
			System.out.println("\n--------------------------------------");
			System.out.println("Welcome to ATM, Coimbatore");
			System.out.println("1 - Admin");
			System.out.println("2 - User");
			System.out.println("3 - Exit");
			System.out.print(">> ");
			com = s.nextByte();
			s.nextLine();

			if(com==3){
				System.out.println("Exiting");
				System.out.println("--------------------------------------");
				break;	
			}else if(com == 1){
				System.out.println("--------------------------------------");
				admin_login();
			}else if(com == 2){
				System.out.println("--------------------------------------");
				user_login();
			}
			else if(com > 3 || com   < 1){
				System.out.println("Invalid Input. Please Select he above mentioned options");
			}
			System.out.println("--------------------------------------");
		}
	}


	public static int admin_login(){
		Scanner s = new Scanner(System.in);
		
		System.out.println("\n--------------------------------------");
		System.out.println("Welcome to Admin Login. \n Please Enter Login Credentials");
		System.out.print("User Id : ");
		String user_id = s.next();s.nextLine();
		String password;
		if(Admin.is_admin(user_id)){
			System.out.print("Password : ");
			password = s.next();s.nextLine();
			if(Admin.is_admin(user_id, password)){
				System.out.println("Entered User Credentials are Correct");
				Admin cur_adm = new Admin(user_id);
				cur_adm.admin_menu();
				return 0;
			}else{
				System.out.println("Entered Credentials are Wrong !");
				
			}
		}else{
			System.out.println("Entered User Doesn't Exist");
			
		}
		System.out.println("Press Enter to Return to Main Menu.");
		// System.out.print("--------------------------------------");
		s.nextLine();
	

		return 0;
	}


	public static int user_login(){
		Scanner s = new Scanner(System.in);
		// while(true){
			System.out.println("\n--------------------------------------");
			System.out.println("Welcome to User Login. \n Please Enter Login Credentials");
			System.out.print("User Id : ");
			String user_id = s.next();s.nextLine();
			int password;
			Boolean is_in = false;
			if(User.is_user(user_id)){
				User cur_usr = new User(user_id);
				System.out.print("Password : ");
				password = s.nextInt();s.nextLine();
				if(cur_usr.is_user(password)){
					// User is present
					is_in = true;
					System.out.println("Entered User Credentials are Correct");
					cur_usr.user_menu();
				}
			}else{
				System.out.println("Entered User Doesn't Exist");
			}

			if(!is_in){
				System.out.println("Press Enter to Go Back to Main Menu...");
				s.nextLine();
			}

			
			System.out.println("--------------------------------------");
		// }

		return 0;
	}


	public static void main(String[] args){

		
		welcome();

	}
} 

class User{
	Scanner s = new Scanner(System.in);
	static int user_nos = 3;
	static int[] usr_acc_nums = {1001,1002,1003};
	static String[] user_id_arr = {"hari1", "hari2", "hari3"};
	static int[] pin_arr = {1234,2345,4567};
	static int[] daily_limit = {0,0,0};
	static int[] bank_id = {4,3,2};
	static int[] balances = {15000, 20000, 16000};
	static int[] attempt = {0,0,0};
	static String[] statement = {"","",""};
	static int[] impf = {0,0,0};
	String user_id;
	int user_index;
	

	public User(String user_id){
		this.user_id = user_id;
		// Finding and Storing User Index
		for(int i = 0; i < user_nos; i++){
			if(user_id_arr[i].equals(user_id)){user_index = i;break;}
		}
	}

	// Authentication for user
	public static boolean is_user(String user_id){
		for(int i = 0; i < user_nos; i++){
			if(user_id_arr[i].equals(user_id)){
				return true;
			}
		}

		return false;
	}

	public boolean is_user(int pass){
		if(attempt[user_index] == 3){
			System.out.println("Maximum Attempts Exceeded");
			return false;
		}
		if(pin_arr[user_index]==pass){
			attempt[user_index] = 0;
			return true;
		}
		attempt[user_index]++;
		System.out.println("Entered Credentials are wrong");
		System.out.println("No of Attempts left : "+ (3 - attempt[user_index]));
		
		return false;
	}

	public int getBal(){
		return balances[this.user_index];
	}

	public void setBal(int bal){
		balances[this.user_index] = bal; 
	}

	public int withDrawMoney(){
		System.out.println("\n--------------------------------------");
		System.out.println("Cash WithDrawal\n");
		System.out.println("Enter the Amout and Press Enter to Continue\n");
		System.out.print("Amount :  ");
		int amm = s.nextInt();s.nextLine();
		System.out.println();
		int amm_org = amm;
		boolean trans_poss = true;
		// int[] cashout = getCash(amm);
		int[] cashout = {0,0,0,0};
		int[] zeros = {0,0,0,0};
		int[] avail_notes = ATM.inCash();
		int bal = getBal();


		if(amm%100 != 0){
			System.out.println("Invalid Input");
			return 0;
		}

		if(amm > 40000){
			System.out.println("ATM withdrawal only support 40,000 or lower. Trasaction Failed");
			return 0;
		}

		if(amm > bal){
			System.out.println("Insufficient Balance in Account. Trasaction Failed");
			return 0;
		}

		if(amm > 40000-daily_limit[user_index]){
			System.out.println("Daily Limit for transaction Exceeded. Trasaction Failed");
			return 0;
		}

		
		if(ATM.is_empty() || ATM.get_amount() < amm){
			System.out.println("Transaction Cannot Be Completed. Insufficient fund in ATM");
			return 0;
		}

		
		if(amm%100==0){
			
			
			//  2000, 500, 200, 100
			int pos;
			if(avail_notes[0] != 0){
				pos = amm/2000;
				cashout[0] = pos<=avail_notes[0]?pos:avail_notes[0];
				amm -= 2000*cashout[0];
			}
			if(avail_notes[1] != 0){
				pos = amm/500;
				cashout[1] = pos<=avail_notes[1]?pos:avail_notes[1];
				amm -= 500*cashout[1];
			}if(avail_notes[2] != 0){
				pos = amm/200;
				cashout[2] = pos<=avail_notes[2]?pos:avail_notes[2];
				amm -= 200*cashout[2];
			}if(avail_notes[3] != 0){
				pos = amm/100;
				cashout[3] = pos<=avail_notes[3]?pos:avail_notes[3];
				amm -= 100*cashout[3];
			}

			

			int[] ref = {2000,500,200,100};
			if(amm != 0 ){
				System.out.println("Requested Denomination not available. Please try again. Transaction Failed");
				for(int i = 2; i >= 0; i--){
					if(avail_notes[i]!=0){
						System.out.println("Please Enter the Amount in Dinomination of : "+ ref[i]);
						break;
					}
				}
				
			}else{
				// System.out.println("Balance Before Transaction :"+ this.balance);
				setBal(bal - amm_org);
				ATM.deduct(cashout);

				// Adding to statement
				statement[user_index] =  "Debit: Rs."+amm_org+" withdrawn at ATM,"+ATM.name+".\n" + statement[user_index];

				daily_limit[user_index] += amm_org;

				if(bank_id[user_index] != ATM.id){
					if(impf[user_index]<4){
						impf[user_index] += 1;
					}else if(impf[user_index]==4){
						setBal(getBal() - 40);
						statement[user_index] =  "Debit: Rs."+40+" Charge for IMPF, For Transaction at ATM,"+ATM.name+".\n" + statement[user_index];
						impf[user_index] = 0;
					}
				}
				// System.out.println("Balance Before Transaction :"+ this.balance);
				for(int i = 0; i < 4; i++){
					System.out.println("Rs." + ref[i] + "Notes - " + cashout[i]);
				}
			}
		}
	

		return 0;
	}

	public void checkBalance(){
		System.out.println("\n--------------------------------------");
		System.out.println("Balance :      "+getBal()+"\n Press Enter to Continue.\n--------------------------------------");
		s.nextLine();
	}

	public void deposit(){
		System.out.println("\n--------------------------------------");
			System.out.println("Please Add the Cash to be deposited!");
			System.out.print("No of 2000 Rs. Notes : ");
			int in2000 = s.nextInt();
			s.nextLine();

			System.out.print("No of 500 Rs. Notes : ");
			int in500 = s.nextInt();
			s.nextLine();

			System.out.print("No of 200 Rs. Notes : ");
			int in200 = s.nextInt();
			s.nextLine();

			System.out.print("No of 100 Rs. Notes : ");
			int in100 = s.nextInt();
			s.nextLine();

			ATM.add_cash(in100, in200, in500, in2000);
			int cred_amm = (in100*100 + in200*200 + in500*500 + in2000*2000);
			balances[user_index] += cred_amm;
			statement[user_index] = "Credit: Rs."+cred_amm+" withdrawn at ATM,"+ATM.name+".\n"  + statement[user_index];
			
			// System.out.println("Problem 2: "+bank_id[user_index] != ATM.id);
			if(bank_id[user_index] != ATM.id){
				if(impf[user_index]<4){
					impf[user_index] += 1;
				}else if(impf[user_index]==4){
					setBal(getBal() - 40);
					statement[user_index] =  "Debit: Rs."+40+" Charge for IMPF,For Transaction at ATM,"+ATM.name+".\n" + statement[user_index];
					impf[user_index] = 0;
				}
			}

			System.out.println("Amount Has been credited to the Account, Dude.");
			System.out.println("Press Enter to continue....");

			s.nextLine();

	}	

	public void statement(){
		System.out.println("\n--------------------------------------");
		System.out.println("Last 5 Transactions");
		String[] statements = statement[user_index].split("\n"); 
		int limit = 5<statements.length?5:statements.length;
		for(int i = 0; i < limit; i++){
			System.out.println((i+1)+" . "+statements[i]);
		}

		System.out.println("Press Enter to Continue...");
		s.nextLine();
	}

	public void change_pin(){
		System.out.println("\n--------------------------------------");
		System.out.println("Pin Change");
		System.out.print("Enter Current Pin : ");
		int present_pin = s.nextInt();s.nextLine();
		if(present_pin != pin_arr[user_index]){
			System.out.println("The Entered Password is wrong");
		}else{
			System.out.print("Enter New Pin   : ");
			int new_pin1 = s.nextInt();s.nextLine();
			System.out.print("Confirm New Pin : ");
			int new_pin2 = s.nextInt();s.nextLine();

			if(new_pin1 != new_pin2){
				System.out.println("The Entered Password didn't Match.");
			}else{
				pin_arr[user_index] = new_pin1;
				System.out.println("Password Changed Succesfully!");
			}

		}
		System.out.println("Press Enter to Continue...");
		s.nextLine();
	}

	public void tranfer(){
		System.out.println("\n--------------------------------------");
		System.out.println("Transfer Cash");
		System.out.print("Benefitiary Account Number : ");
		int ben_acc = s.nextInt();s.nextLine();
		int ben_ind = -1;
		for(int i = 0; i < user_nos; i++){
			if(usr_acc_nums[i] == ben_acc){
				ben_ind = i;
				break;
			}
		}

		if(ben_ind != -1){
			System.out.print("Ammount to transfer : ");
			int amm = s.nextInt();s.nextLine();
			if(amm <= balances[user_index]){
				if(bank_id[user_index] != ATM.id || bank_id[ben_ind] != ATM.id){
					if(impf[user_index]<4){
						impf[user_index] += 1;
					}else if(impf[user_index]==4){
						setBal(getBal() - 40);
						statement[user_index] =  "Debit: Rs."+40+" Charge for IMPF, For Transaction at ATM,"+ATM.name+".\n" + statement[user_index];
						impf[user_index] = 0;
					}
				}
				// Tranfer Fund
				balances[user_index] -= amm;
				balances[ben_ind] += amm;

				// Notifying
				System.out.println("Fund Transfered, Transcation Successful");

				// Adding to statement
				statement[ben_ind] = "Credit: Rs."+amm+" sent from "+user_id_arr[user_index]+" at ATM,"+ATM.name+".\n"  + statement[ben_ind];
				statement[user_index] = "Debit: Rs."+amm+" Transfered to "+user_id_arr[ben_ind]+" at ATM,"+ATM.name+".\n"  + statement[user_index];

			}else{
				System.out.println("Insufficient Balance in Account. Transaction Failed.");
			}
		}else{
			System.out.println("Benefitiary Not Found");
		}

	}

	public int user_menu(){
		Scanner s = new Scanner(System.in);
		while(true){
			System.out.println("\n--------------------------------------");
			System.out.println(impf[user_index]);
			System.out.println("Welcome to User Menu. \nOptions:\n1 - Cash WithDrawal\n2 - Check Balance\n3 - Deposit\n4 - Statement\n5 - Change Pin\n6 - Transfer\n7 - Exit");
			System.out.println("More Options Under Construction.");
			System.out.print("Choise : ");
			int com = s.nextInt();
			if(com == 1){
				withDrawMoney();
				// Common Withdrawal cont
				System.out.println();
				System.out.println("Press Enter to Return to Menu");
				System.out.println("\n--------------------------------------");
				s.nextLine();
				s.nextLine();

			}else if(com == 2){
				checkBalance();
			}else if(com == 3){
				deposit();
			}else if(com == 4){
				statement();
			}else if(com == 5){
				change_pin();
			}else if(com == 6){
				tranfer();
			}else if(com == 7){
				break;
			}else{
				System.out.println("Invalid Input !");
			}
			
		}

		return 0;
	}





}


class Admin{
	// String name;
	String user_id;
	Scanner s = new Scanner(System.in);
	
	public Admin(String user_id){
		this.user_id = user_id;
	}

	public static boolean is_admin(String name){
		if(name.equals("Admin")){return true;}
		return false;
	}

	public static boolean is_admin(String name, String password){
		if(name.equals("Admin") && password.equals("0000")){return true;}
		return false;
	}

	public void addMoney(){

			System.out.println("\n--------------------------------------");
			System.out.println("Locker Open Please Add the Cash !");
			System.out.print("No of 2000 Rs. Notes : ");
			int in2000 = s.nextInt();
			s.nextLine();

			System.out.print("No of 500 Rs. Notes : ");
			int in500 = s.nextInt();
			s.nextLine();

			System.out.print("No of 200 Rs. Notes : ");
			int in200 = s.nextInt();
			s.nextLine();

			System.out.print("No of 100 Rs. Notes : ");
			int in100 = s.nextInt();
			s.nextLine();

			ATM.add_cash(in100, in200, in500, in2000);

			System.out.println("Cash has been Succesfully added");
			System.out.println("Press Enter to continue....");

			s.nextLine();

	}

	public void checkContent(){
		int[] cashDet = ATM.inCash();
		System.out.println("Cash Details in ATM!");
		System.out.println("Notes     Nos");
		System.out.println("2000      "+cashDet[0]);
		System.out.println("500       "+cashDet[1]);
		System.out.println("200       "+cashDet[2]);
		System.out.println("100       "+cashDet[3]);

		System.out.println("Press Enter to Continue");
		s.nextLine();
	}

	public int admin_menu(){
		Scanner s = new Scanner(System.in);
		while(true){
			System.out.println("\n--------------------------------------");
			System.out.println("Welcome to Admin Menu. \nOptions:\n1 - Add Money\n2 - Check Content\n3 - Exit");
			System.out.print("Choise : ");
			int com = s.nextInt();
			if(com == 1){
				addMoney();
			}else if(com == 2){
				checkContent();
			}else if(com == 3){
				break;
			}else{
				System.out.println("Invalid Input !");
			}
			
		}

		return 0;
	}
}



class ATM{
	// The Amount is temporarly stored in here
	private static int amount = 0;
	private static int n2000 = 0;
	private static int n500 = 0;
	private static int n200 = 0;
	private static int n100 = 0;
	static String name = "Coimbatore";
	static int id = 1;


	public static int[] add_cash(int in100, int in200,int in500,int in2000 ){
		int[] rtn = {0,0,0,0};
		if(n100 + in100 > 1000){
			rtn[0] = n100 + in100 - 1000;
			n100 = 1000;
		}else{
			n100 += in100;
		}

		// Need Reprogramming
		n2000 += in2000;
		n200 += in200;
		n500 += in500;

		return rtn;
	}

	public static int[] inCash(){
		int[] cashInBank = {n2000, n500, n200, n100};
		return cashInBank;
	}

	public static boolean is_empty(){
		boolean empty = true;
		int amm[] = inCash();
		for(int k:amm){
			if(k!=0){
				empty = false;
				break;
			}
		}

		return empty;
	}

	public static void deduct(int[] cash){
		n2000 -= cash[0];
		n500 -= cash[1];
		n200 -= cash[2];
		n100 -= cash[3];
	}

	public static int get_amount(){
		return (n2000*2000 + n500*500 + n200*200 + n100*100);
	}
	
}