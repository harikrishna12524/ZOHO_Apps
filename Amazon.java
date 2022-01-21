import java.util.*;
public class Amazon{
    static Scanner s  = new Scanner(System.in);

    public static void cls(){
            try{
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
    }

    public static void welcome(){
        boolean input_invalid = false;
        while(true){
            cls();  
            System.out.println("----------------------------------");
            System.out.println("            Welcome to Amazon  ");
            System.out.println("Select User Type:\n1 - Customer\n2 - Vendor\n3 - Admin\n4 - Exit");
            if(input_invalid){
                System.out.println("Please Enter a valid Choise.");
            }

            System.out.print("Choise : ");
            int com = s.nextInt();s.nextLine();
            switch(com){
                case 1:
                    Customer.customer_welcome();
                    break;
                case 2:
                    Vendor.vendor_welcome();
                    break;
                case 3:
                    Admin.admin_login();
                    break;
                case 4:
                    System.exit(0);
                default:
                    input_invalid = true;
                    continue;
            }
            input_invalid = false;  
        }
    }

    public static void main(String[] args){

        welcome();
    }

}


class Customer{
    // Scanner
    static Scanner s = new Scanner(System.in);

    // User Static data
    static int user_nos = 3;
    static String[] user_names = {"HariKrishna1","Harikrishna2","Harikrishn3"};
    static String[] user_ids = {"hari1","hari2","hari3"};
    static int[] pins = {1000,1001,1002};
    static String[] address = {"7/2", "J.G", "5/2"};
    static long[] phones = {9445842664l,9597567003l,9386870323l};
    static int[] wallet_bal = {1000,1200,15000};
    static String[] orderDet = {"","",""};


    static Cart cart1 = new Cart();
    static Cart cart2 = new Cart();
    static Cart cart3 = new Cart();
    static Cart[] cartData = {cart1, cart2, cart3};

    // User object data
    String user_id ;
    int usr_index;

    public static void cls(){
            try{
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
    }

    public static void enter_wait(){
        System.out.print("Press Enter to Exit...");
                        s.nextLine();
    }

    public static int[] append(int[] arr, int key) {
		int[] pin_temp = new int[arr.length+1];
        System.arraycopy(arr, 0 , pin_temp, 0, arr.length);
        pin_temp[arr.length] = key;
        return pin_temp;
	}

    public static Cart[] append(Cart[] arr, Cart key) {
		Cart[] pin_temp = new Cart[arr.length+1];
        System.arraycopy(arr, 0 , pin_temp, 0, arr.length);
        pin_temp[arr.length] = new Cart();
        return pin_temp;
	}

    public static String[] append(String[] arr, String key) {
		String[] pin_temp = new String[arr.length+1];
        System.arraycopy(arr, 0 , pin_temp, 0, arr.length);
        pin_temp[arr.length] = key;
        return pin_temp;
	}

    public static boolean is_in(int[] arr, int ele){
        for(int k: arr){
            if(k==ele){
                return true;
            }
        }
        return false;
    }

    public Customer(String user_id){
        this.user_id  = user_id;
        for(int i = 0; i < user_nos; i++){
            if(user_ids[i].equals(user_id)){
                usr_index = i;
                break;
            }
        }
        
    }

    public static int is_user(String user_id){
        for(int i = 0; i < user_nos; i++){
            if(user_ids[i].equals(user_id)){
                return i;
            }
        }
        return -1;
    }

    public static boolean number_exists(long num){
        for(int i = 0; i < user_nos; i++){
            if(phones[i] == num){
                return true;
            }
        }
        return false;
    }

    public boolean is_user(int pincode){
        if(pins[usr_index] == pincode){
            return true;
        }
        return false;
    }

    public static void customer_login(){
        int tries = 0;
        while(tries++ < 3){
            cls();
            System.out.println("----------------------------------");
            System.out.println("          Welcome Customer     ");
            System.out.println("Please Enter Login Credentials");
            System.out.print("User Id : ");
            String user_id = s.next();s.nextLine();
            if(is_user(user_id) != -1){
                // Creating a user Object.
                Customer cur_usr = new Customer(user_id);

                System.out.print("Password : ");
                int pin = s.nextInt();s.nextLine();

                if(cur_usr.is_user(pin)){
                    cur_usr.customer_menu();
                    break;
                }else{
                    System.out.println("Entered Password is wrong. Please Try Again.");
                }
            }else{
                System.out.println("User Not Found. Please Try Again.");
            }
        }
        if(tries == 3){
            System.out.println("Maximum Attempts Exeeded, Please Try after some time.");
            System.out.println("Press Enter to continue...");

        }
        
    }

    public static void customer_reg(){
        while(true){

            cls();

            long phno;
            String user_id_reg; 

            System.out.println("----------------------------------");
            System.out.println("         Welcome To Amazon Customer Reg    ");
            System.out.print("User Name : ");
            String user_name = s.next(); s.nextLine();
            

            // If user_name exists.
            do{
            System.out.print("User Id : ");
            user_id_reg = s.next(); s.nextLine();
            if(is_user(user_id_reg) != -1){
                System.out.println("User Name Already Exists. Please Try another.");
                }
            }while(is_user(user_id_reg) != -1);


            System.out.print("Password (Use Numbers - 4 digit) : ");
            int pin = s.nextInt(); s.nextLine();


            System.out.print("Address (Enter in Single Line) : ");
            String add = s.next(); s.nextLine();


            do{
            System.out.print("Phone Number : ");
            phno = s.nextLong(); s.nextLine();
            if(number_exists(phno)){
                System.out.println("Phone Number Already Exists. Please Try another.");
                }
            }while(number_exists(phno));

            System.out.println("Confirm Entered Data ? (y/n)");
            String conf = s.next(); s.nextLine();
            if(conf.equals("y")){
                
                // Rewriting data in Arrays
                
                String[] user_names_temp = new String[user_nos+1];
                System.arraycopy(user_names, 0 , user_names_temp, 0, user_nos);
                user_names_temp[user_nos] = user_name;
                user_names = user_names_temp;


                String[] user_id_temp = new String[user_nos+1];
                System.arraycopy(user_ids, 0 , user_id_temp, 0, user_nos);
                user_id_temp[user_nos] = user_id_reg;
                user_ids = user_id_temp;

                
                String[] address_temp = new String[user_nos+1];
                System.arraycopy(address, 0 , address_temp, 0, user_nos);
                address_temp[user_nos] = add;
                address = address_temp;

                long[] phno_temp = new long[user_nos+1];
                System.arraycopy(phones, 0 , phno_temp, 0, user_nos);
                phno_temp[user_nos] = phno;
                phones = phno_temp;

                int[] pin_temp = new int[user_nos+1];
                System.arraycopy(pins, 0 , pin_temp, 0, user_nos);
                pin_temp[user_nos] = pin;
                pins = pin_temp;

                wallet_bal = append(wallet_bal, 0);

                Cart cart = new Cart();
                cartData = append(cartData, cart);


                user_nos++;

                // for(String k: address){
                //     System.out.println(k);
                // }

                System.out.println("Congradulations, You are succesfully registered !");
                System.out.print("Press Enter to Continue..");
                s.nextLine();
                break;
            }

        }       
    }

    public static void customer_welcome(){
        int opt = 1;
        while(true){
            cls();
                // for(String k: user_names){
                //     System.out.println(k);
                // }
            System.out.println("----------------------------------");
            System.out.println("         Welcome Customer     ");
            System.out.println("Please Select the appropriate option.");
            System.out.println("1 - Already Having an Account\n2 - New to Amazon\n3 - Back");

            if(opt == -1){
                System.out.println("Please Enter a valid input!");
            }

            System.out.print("Choise : ");
            opt = s.nextInt(); s.nextLine();
            
            // Break loop for 3
            if(opt==3){break;}
            
            // options
            switch(opt){
                case 1:
                    customer_login();
                    break;
                case 2:
                    customer_reg();
                    break;
                default:
                    opt = -1;
            }
        }
    }

    public void prod_oper(String head, int[] arr){
        int[] temp_prods = {};
        int sort_by = 1, p_low = 0, p_high = 0;
        int com = 0;
        boolean reverse = false, sort_price = false, filter_price = false;
        while(true){

            cls();
            System.out.println("----------------------------------");
            System.out.println("     " + head);

            if(filter_price){
                System.out.println("Filter By : Price. Range: " + p_low + " - " + p_high + "\n");
                temp_prods = Product.filter_price(arr, p_low, p_high);
            }else{
                temp_prods = new int[arr.length];
                System.arraycopy( arr, 0, temp_prods , 0 , arr.length);
            }

            
            if(sort_price){
                System.out.println("Sorted By : Price; Order : "+ (reverse==true?"Decending":"Ascending" )+ "\n");
                Product.sort(temp_prods, sort_by, reverse);
            }


            if(temp_prods.length == 0){
                System.out.println("Sorry, No results similar to your search results\n");
            }
            for(int k = 0; k < temp_prods.length; k++){
                int i = temp_prods[k];
                System.out.println("Product ID : " + i + "\n" + Product.names[i] + "\n" + Product.discripts[i] + "\nPrice: " + Product.prices[i] + "\nAvailable: "+Product.stocks[i] + "\n");
            }


            System.out.println("Options:\n1 - Sort the products\n2 - filter the products\n3 - Add a product to cart\n4 - return to Previous Page");
            
            if(com == -1){
                System.out.println("Please Choose a valid input!");
            }

            System.out.print("Choise : ");
            com = s.nextInt(); s.nextLine();
            
            if(com == 1){
                    System.out.print("Sorting by ?\n1 - Price\n2 - Cancel\nChoise : ");
                    int sort_opt = s.nextInt(); s.nextLine();
                    if(sort_opt == 2){continue;}
                    else if(sort_opt == 1){
                        sort_price = true;
                        System.out.print("Order ?\n1 - Decending\nOther - Ascending\nChoise : ");
                        int rev_opt = s.nextInt(); s.nextLine();
                        reverse = rev_opt==1?true:false;
                    }else{
                        System.out.println("Invalid input");
                        enter_wait();
                    }
                    
                    
            }else if(com == 2){
                System.out.print("Filter by ?\n1 - Price\n2 - Cancel\nChoise : ");
                int filt_opt = s.nextInt(); s.nextLine();
                if(filt_opt == 2){continue;}
                else if(filt_opt == 1){
                    filter_price = true;
                    System.out.print("Lower Limit : ");
                    p_low = s.nextInt(); s.nextLine();
                    System.out.print("Upper Limit : ");
                    p_high = s.nextInt(); s.nextLine();
                    if(p_high <= p_low){
                        filter_price = false;
                        System.out.println("Invalid input");
                        enter_wait();
                    }
                }else{
                    System.out.println("Invalid input");
                    enter_wait();

                }

            }else if(com == 3){
                System.out.print("Adding Product to cart (-1 to cancel)\nProduct Id : ");
                int prod_id = s.nextInt(); s.nextLine();
                if(is_in(temp_prods, prod_id)){
                    System.out.print("Count : ");
                    int count = s.nextInt(); s.nextLine();
                    if(count < 1){
                        System.out.println("Invalid Count");
                    }else if(count > Product.stocks[prod_id]){
                        System.out.println("Requested Quantity Unavailable");
                        System.out.print("Press Enter to Exit...");
                        s.nextLine();
                    }
                    else{
                        cartData[usr_index].addItem(prod_id, count);
                        System.out.print("Press Enter to Exit...");
                        s.nextLine();
                    }

                }else{
                    System.out.println("Requested Product Id not Found");
                    System.out.print("Press Enter to Exit...");
                     s.nextLine();
                }

            }else if(com == 4){
                System.out.print("Press Enter to Exit...");
                s.nextLine();
                break;
            }else{
                com = -1;
            }
        
        }
    }

    public void catogs(int cat){
        String catog = "";
        switch(cat){
            case 1:
                catog = "Wearables";
                prod_oper("Categories , "+catog , Product.filter_catog(1) );
                break;
            case 2:
                catog = "Electronics";
                prod_oper("Categories , "+catog , Product.filter_catog(2) );
                break;
            case 3:
                catog = "Food Products";
                prod_oper("Categories , "+catog , Product.filter_catog(3) );
                break;
        }

    }

    public void catogs(){
            int com = 1;
        loop : while(true){
            cls();
            System.out.println("----------------------------------");
            System.out.println("         Product Categories , " );
            System.out.println("Please Select the appropriate option.");
            System.out.println("1 - Wearables\n2 - Electronics\n3 - Food Products\n4 - Exit");

            if(com == -1){
                System.out.println("Please Enter a valid input");
            }

            System.out.print("Choise : ");
            com = s.nextInt(); s.nextLine();

            switch(com){
                case 1:
                    catogs(1);
                    break;
                case 2:
                    catogs(2);
                    break;
                case 3:
                    catogs(3);
                    break;
                case 4:
                    break loop;
                default:
                    com = -1;
            }

        }
    }

    public void search_prod(){
        cls();
        System.out.println("----------------------------------");
        System.out.println("Enter the search key word or 0 - to go back");
        System.out.print("Key : ");
        String key = s.next();s.nextLine();
        if(key.equals("")){
            System.out.println("Please Enter a key word to search");
            enter_wait();
            search_prod();
        }else if(key.equals("0")){

        }else{
            prod_oper("Search Results for "+key, Product.search(key));
        }
    }

    public void cart(){
        int com = 0;
        while(true){
            cls();
            System.out.println("----------------------------------");
            System.out.println("           Cart         ");
            int[] cartContent = {};
            try{
                cartContent = cartData[usr_index].getItems();
            }catch(Exception e){
                // System.out.println("The Cart is Empty");
            }
            if(cartContent.length==0){System.out.println("The Cart is Empty");}
            for(int k = 0; k < cartContent.length; k++){
                int i = cartContent[k];
                System.out.println("Product ID : " + i + "\n" + Product.names[i] + "\n" + Product.discripts[i] + "\nPrice:" + Product.prices[i] + "\nCount: " + cartData[usr_index].counts.get(k)+"\n");
            }
            System.out.println("Options:\n1 - Check out\n2 - Remove Product\n3 - Go Back");

            if(com == -1){System.out.println("Please Enter a valid Input");}

            System.out.print("Choise : ");
            com = s.nextInt(); s.nextLine();

            if(com == 1){
                if(cartContent.length == 0){
                    System.out.println("The cart is Empty!.");
                }else{
                    if(checkOut()){
                        break;
                    };
                }
            }else if(com == 2){
                if(cartContent.length == 0){
                    System.out.println("There is nothing in cart to remove.");
                }else{
                System.out.print("Enter Product Id : ");
                int rem_prod = s.nextInt(); s.nextLine();

                cartData[usr_index].removeItem(rem_prod);}
                enter_wait();
            }else{
                break;
            }

        
        }
    }

    public boolean checkOut(){
        boolean order_complete = false;
        int com = 0;
        while(true){
            cls();
            System.out.println("----------------------------------");
            System.out.println("               Cart            ");
            int[] cartContent = cartData[usr_index].getItems();
            
            int overallPrice = 0;

            String Order_recipt = "Product                                           Count          Price\n";
            System.out.println("Product                                           Count          Price");
            for(int k = 0; k < cartContent.length; k++){
                int i = cartContent[k];
                String prod_name = Product.names[i];
                int count = cartData[usr_index].counts.get(k);

                int price = Product.prices[i]*count;

                // Printing a formatted output
                System.out.print(prod_name);
                Order_recipt = Order_recipt + prod_name;
                int space1 = 50 - prod_name.length();
                for(int spc = 0; spc < space1; spc++){System.out.print(" ");Order_recipt = Order_recipt + " ";}

                String count_str = count + "";
                Order_recipt = Order_recipt + count_str;
                System.out.print(count_str);
                int space2 = 15 - count_str.length();
                for(int spc = 0; spc < space2; spc++){System.out.print(" ");Order_recipt = Order_recipt + " ";}

                System.out.println(price);
                overallPrice += price;
                Order_recipt += price + "\n";

            }
            System.out.print("Total Price : "+overallPrice);
            Order_recipt += "Total Price : "+overallPrice;

            System.out.println("\n Select one of the options:\n1 - Proceed to Payment\n2 - Go Back to Cart");
            if(com == -1){System.out.println("Enter a Valid input");}

            System.out.print("Choise : ");
            com = s.nextInt(); s.nextLine();

            if(com == 1){
                System.out.println("\n Select one of the options:\n1 - Pay Via Wallet\n2 - Cash on Delivery\nAnyother - Return");
                System.out.print("Choise : ");
                int pay_opt = s.nextInt(); s.nextLine();
                if(pay_opt == 1){
                    if(wallet_bal[usr_index] >= overallPrice){
                        wallet_bal[usr_index] -= overallPrice;
                        System.out.println("Your order has been Successfully Placed.");
                        cartData[usr_index].clearCart();
                        orderDet[usr_index] += Order_recipt + "%";
                        order_complete = true;
                        break;
                    }else{
                        System.out.println("You dont have required balance in wallet");
                    }
                }else if(pay_opt==2){
                    System.out.println("Your order has been Successfully Placed on Cash On Delivery.");
                    cartData[usr_index].clearCart();
                    orderDet[usr_index] += Order_recipt + "%";
                    order_complete =  true;
                    break;
                }else{
                    System.out.println("Invalid Input.");
                }
            }else if(com == 2){
                break;
            }else{
                com = -1;
            }
            enter_wait();
            

        }
        return order_complete;
    }

    public void prev_order(){
        // System.out.println(orderDet[usr_index]);
        
        if(orderDet[usr_index].contains("%")){
        String[] orders = orderDet[usr_index].split("%");
        if(orders.length!=0){
            for(int i = orders.length-1; i >=0; i--){
                cls();
                System.out.println("Order Number : " + (orders.length-i));
                String k = orders[i];
                System.out.println(k + "\n");
                if(i != orders.length - 2){
                    System.out.print("Press Enter for Next Order");
                }else{
                    System.out.print("Last Order, Press Enter to return to menu...");
                }
                s.nextLine();

            }
        }
        }else{
            cls();
            System.out.println("No Orders Placed Till Now.  Start Shopping Happiness !");
            enter_wait();
        }
        
    }

    public void deposit(){
        cls();
        System.out.println("----------------------------------");
        System.out.println("         Deposite Cash , " );
        System.out.println("Please Select the appropriate option.");
        System.out.println("Wallet Balance : "+wallet_bal[usr_index]);
        System.out.println("1 - Deposite\nAnyOther - Exit\n");
        System.out.print("Choise : ");
        int com = s.nextInt(); s.nextLine();
        if(com == 1){
            System.out.print("Amount : ");
            int cash = s.nextInt(); s.nextLine();
            if(cash > 0){
                wallet_bal[usr_index] += cash; 
                System.out.println("Cash Deposited\nNew Balance : "+wallet_bal[usr_index]);
            }else{System.out.println("Invalid Cash");}
        }
        enter_wait();


    }

    public void customer_menu(){
            int com = 0;
        loop : while(true){
            cls();
            System.out.println("----------------------------------");
            System.out.println("         Welcome , " + user_names[usr_index]);
            System.out.println("Please Select the appropriate option.");
            System.out.println("1 - Search\n2 - Categories\n3 - Shopping Cart\n4 - Previous Orders\n5 - Deposit Cash in Wallet\n6 - Exit");

            if(com == -1){
                System.out.println("Please Enter a valid input");
            }
            System.out.print("Choise : ");
            com = s.nextInt();s.nextLine();
            switch(com){
                case 1:
                    search_prod();
                    break;
                case 2:
                    catogs();
                    break;
                case 3:
                    cart();
                    break;
                case 4:
                    prev_order();
                    break;
                case 5:
                    deposit();
                    break;
                case 6:
                    break loop;
                default:
                    com = -1;
            }

        }
    }


}


class Vendor{

    static Scanner s = new Scanner(System.in);

    // User Static data
    static int user_nos = 3;
    static String[] user_names = {"ElonMusk1","ElonMusk2","ElonMusk3"};
    static String[] user_ids = {"M101","M102","M103"};
    static int[] pins = {1000,1001,1002};
    static byte[] status = {0 , 0 , 0};
    static String[] prods = {"","",""};

    // User object data
    String user_id ;
    int usr_index;

    
    public static void cls(){
            try{
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
                catch(Exception e)
            {
                System.out.println(e);
            }
    }

    public static void enter_wait(){
        System.out.print("Press Enter to Exit...");
                        s.nextLine();
    }

    public Vendor(String user_id){
        this.user_id  = user_id;
        for(int i = 0; i < user_nos; i++){
            if(user_ids[i].equals(user_id)){
                usr_index = i;
                break;
            }
        }
        
    }

    public static int is_user(String user_id){
        for(int i = 0; i < user_nos; i++){
            if(user_ids[i].equals(user_id)){
                return i;
            }
        }
        return -1;
    }

    public boolean is_user(int pincode){
        if(pins[usr_index] == pincode){
            return true;
        }
        return false;
    }

    public void vendor_access(){
        if(Vendor.status[usr_index]==1){
            vendor_menu();
        }else if(Vendor.status[usr_index]==-1){
            System.out.println("Your Account has been Blocked. Please Contact ADMIN for Queries.");
            System.out.print("Press Enter to continue...");
            s.nextLine();
        }else{
            System.out.println("Your Account is not Approved. Please try after sometime.");
            System.out.print("Press Enter to continue...");
            s.nextLine();
        }
    }

    public static void vendor_login(){
        int tries = 0;
        while(tries++ < 3){
            cls();
            System.out.println("----------------------------------");
            System.out.println("          Welcome Vendor     ");
            System.out.println("Please Enter Login Credentials");
            System.out.print("User Id : ");
            String user_id = s.next();s.nextLine();
            if(is_user(user_id) != -1){
                // Creating a user Object.
                Vendor cur_usr = new Vendor(user_id);

                System.out.print("Password : ");
                int pin = s.nextInt();s.nextLine();

                if(cur_usr.is_user(pin)){
                    cur_usr.vendor_access();

                    break;
                }else{
                    System.out.println("Entered Password is wrong. Please Try Again.");
                }
            }else{
                System.out.println("User Not Found. Please Try Again.");
            }
            if(tries == 3){
                System.out.println("Maximum Attempts Exeeded, Please Try after some time.");
                }

            System.out.print("Press Enter to continue...");
            s.nextLine();

        }
        
    }

    public static void vendor_reg(){
        while(true){

            cls();

            long phno; 

            System.out.println("----------------------------------");
            System.out.println("         Welcome To Amazon Vendor Reg    ");
            System.out.print("User Name : ");
            String user_name = s.next(); s.nextLine();
            
            String mId = "M" + (101 + user_nos);
            System.out.println("The MerchantID Generated for you :"+mId);
            

            System.out.print("Password (Use Numbers - 4 digit) : ");
            int pin = s.nextInt(); s.nextLine();


            System.out.println("Confirm Entered Data ? (y/n)");
            String conf = s.next(); s.nextLine();
            if(conf.equals("y")){
                
                // Rewriting data in Arrays
                
                String[] user_names_temp = new String[user_nos+1];
                System.arraycopy(user_names, 0 , user_names_temp, 0, user_nos);
                user_names_temp[user_nos] = user_name;
                user_names = user_names_temp;


                String[] user_id_temp = new String[user_nos+1];
                System.arraycopy(user_ids, 0 , user_id_temp, 0, user_nos);
                user_id_temp[user_nos] = mId;
                user_ids = user_id_temp;

                int[] pin_temp = new int[user_nos+1];
                System.arraycopy(pins, 0 , pin_temp, 0, user_nos);
                pin_temp[user_nos] = pin;
                pins = pin_temp;

                byte[] status_temp = new byte[user_nos+1];
                System.arraycopy(status, 0 , status_temp, 0, user_nos);
                status_temp[user_nos] = 0;
                status = status_temp;

                user_nos++;

                // for(String k: address){
                //     System.out.println(k);
                // }

                System.out.println("Congradulations, You are succesfully registered !. Please enter_wait until you are accepted as a Verified Vendor");
                System.out.print("Press Enter to Continue..");
                s.nextLine();
                break;
            }

        }       
    }

    public static void vendor_welcome(){
        int opt = 1;
        while(true){
            cls();
            // for(String k: user_names){
            //     System.out.println(k);
            // }
            System.out.println("----------------------------------");
            System.out.println("         Welcome Vendor    ");
            System.out.println("Please Select the appropriate option.");
            System.out.println("1 - Already Having an Account\n2 - New to Amazon\n3 - Back");

            if(opt == -1){
                System.out.println("Please Enter a valid input!");
            }

            System.out.print("Choise : ");
            opt = s.nextInt(); s.nextLine();
            
            // Break loop for 3
            if(opt==3){break;}
            
            // options
            switch(opt){
                case 1:
                    vendor_login();
                    break;
                case 2:
                    vendor_reg();
                    break;
                default:
                    opt = -1;
            }
        }
    }

    public void manage_product(){
        int com = 0;
        while(true){
            cls();
            System.out.println("----------------------------------");
            System.out.println("      Manage Products    ");
            int[] prods = Product.filter_vendor(usr_index);
            if(prods.length == 0){
                System.out.println("No Products Added, Add more product and Joy");
            }
            for(int i:prods){
                System.out.println("Product ID : " + i + "\n" + Product.names[i] + "\n" + Product.discripts[i] + "\nPrice:" + Product.prices[i] + "\n");
            }
            System.out.println("Option\n1 - Remove Product\n2 - Edit Product\n3 - Exit");

            if(com == -1){
                System.out.println("Please Enter a Valid Input");
            }

            System.out.print("Choise : ");
            com = s.nextInt();s.nextLine();
            if(com == 1){
                System.out.print("Product Id:");
                int prod_id = s.nextInt(); s.nextLine();
                if(prod_id <= Product.nos){
                    if(Product.sellerIds[prod_id] == usr_index){
                        Product.removeProduct(prod_id);
                        System.out.println("The Product is Successfully removed");
                    }else{
                        System.out.println("The Product is not under your ownership to remove");
                    }
                }else{
                    System.out.println("The Product Doesn't Exists");
                }

            }else if(com == 2){
                System.out.print("Product Id:");
                int prod_id = s.nextInt(); s.nextLine();
                if(prod_id <= Product.nos){
                    if(Product.sellerIds[prod_id] == usr_index){
                        System.out.println("Option\n1 - Name\n2 - Discription\n3 - Price\n4 - Stock\n5 - Category\n6 - Cancel");
                        System.out.print("Choise : ");
                        int edt_com = s.nextInt(); s.nextLine();
                        switch(edt_com){
                            case 1:
                                System.out.println("Old Name :"+Product.names[prod_id]);
                                System.out.print("New Value : ");
                                String val1 = s.nextLine();
                                Product.names[prod_id] = val1;
                                break;
                            case 2:
                                System.out.println("Old Discription :"+Product.discripts[prod_id]);
                                System.out.print("New Discription : ");
                                String val2 = s.nextLine();
                                Product.discripts[prod_id] = val2;
                                break;
                            case 3:
                                System.out.println("Old Price :"+Product.prices[prod_id]);
                                System.out.print("New Price : ");
                                int val3 = s.nextInt(); s.nextLine();
                                Product.prices[prod_id] = val3;
                                break;
                            case 4:
                                System.out.println("Old Stock :"+Product.stocks[prod_id]);
                                System.out.print("New Stock : ");
                                int val4 = s.nextInt(); s.nextLine();
                                Product.stocks[prod_id] = val4;
                                break;
                            case 5:
                                System.out.println("Old Category :"+Product.catogs[prod_id]);
                                System.out.print("New Category : ");
                                int val5 = s.nextInt(); s.nextLine();
                                Product.catogs[prod_id] = val5;
                                break;
                            case 6:
                                break;
                            default:
                                System.out.println("Enter a valid Input");
                            }

                    }else{
                        System.out.println("The Product is not under your ownership to remove");
                    }
                }else{
                    System.out.println("The Product Doesn't Exists");
                }
                
                
            
            }else if(com == 3){
                break;
            }else{
                com = -1;
            }

        enter_wait();

        }
        
    }

    public void add_product(){
            System.out.println("----------------------------------");
            System.out.println("      Add Products    ");
            System.out.print("Product Name : ");
            String prod_name =  s.nextLine();

            System.out.print("Product Discription : ");
            String prod_discript =  s.nextLine();

            System.out.print("Category : ");
            int catog = s.nextInt(); s.nextLine();

            System.out.print("Price : ");
            int price = s.nextInt(); s.nextLine();

            System.out.print("stock : ");
            int stock = s.nextInt(); s.nextLine();


            System.out.println("Confirm Entered Data ? (y/n)");
            String conf = s.next(); s.nextLine();
            if(conf.equals("y")){
                Product.addProduct(prod_name, price, usr_index, prod_discript, stock, catog);
                System.out.println("The Product has been successfully Added");
                enter_wait();


            }else{
                System.out.println("Entered Details Discarded");
                enter_wait();
            }
    }

    public void search_competition(){
        System.out.println("Enter the search key word or 0 - to go back");
        System.out.print("Key : ");
        String key = s.next();s.nextLine();
        if(key.equals("")){
            System.out.println("Please Enter a key word to search");
            enter_wait();
            search_competition();
        }else if(key.equals("0")){

        }else{
            int[] prods = Product.search(key);

            if(prods.length == 0){
                System.out.println("No Products returned from the search");
            }System.out.println();
            for(int i:prods){
                System.out.println("Product ID : " + i + "\n" + Product.names[i] + "\n" + Product.discripts[i] + "\nPrice:" + Product.prices[i] + "\n");
            }
            enter_wait();
        }
    }

    public void vendor_menu(){
        int com = 0;
        loop : while(true){
            cls();
            System.out.println("----------------------------------");
            System.out.println("         Welcome , " + user_names[usr_index]);
            System.out.println("Please Select the appropriate option.\n1 - Add Product\n2 - Manage Products\n3 - Search Other Products\n0 - Exit");

            if(com == -1){
                System.out.println("Please Enter a valid input!");
            }

            System.out.print("Choise : ");
            com = s.nextInt();s.nextLine();
            
            switch(com){
                case 1:
                    add_product();
                    break;
                case 2:
                    manage_product();
                    break;
                case 3:
                    search_competition();
                    break;
                case 0:
                    break loop;
                default:
                    com = -1;
            }
            
        }
    }
}


class Admin{
    static Scanner s = new Scanner(System.in);

    String user_id;


    public Admin(String user_id){
        this.user_id = user_id;
    }

    public static void cls(){
            try{
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
    }

    public static int[] append(int[] arr, int key) {
		int[] pin_temp = new int[arr.length+1];
        System.arraycopy(arr, 0 , pin_temp, 0, arr.length);
        pin_temp[arr.length] = key;
        return pin_temp;
	}

    public static void admin_login(){
        cls();
        System.out.println("----------------------------------");
        System.out.println("         Welcome Admin    ");
        System.out.println("Please Enter Login Credentials");
        System.out.print("User Id : ");
        String user_id = s.next();s.nextLine();
        System.out.print("Password : ");
        String pass = s.next();s.nextLine();
        if(user_id.equals("Admin") && pass.equals("0000")){
            Admin cur_adm = new Admin(user_id);
            cur_adm.admin_menu();
        }else{
            System.out.println("The Entered Credentials are wrong");
            System.out.print("Press Enter to Continue...");
            s.nextLine();
        }


    }

    public void admin_menu(){
        int com = -1;
        loop: while(true){
        cls();

        System.out.println("----------------------------------");
        System.out.println("         Welcome Admin    ");
        System.out.println("Select Appropriate Option\n1 - Add Vendor\n2 - Block Vendor\n3 - Approve Vendor\n4 - Approve Single Vendor\n5 - Exit");
        
        if(com == -1){
            System.out.println("Please Select Appropriate Option");
        }

        System.out.print("Choise : ");
        com = s.nextInt(); s.nextLine();


        switch(com){
            case 1:
                addVendor();
                break;
            case 2:
                blockVendor();
                break;
            case 3:
                approveVendor();
                break;
            case 4:
                approveSingleVendor();
                break;
            case 5:
                break loop;
            default:
                com =- 1;
        }
        }

    }

    public void blockVendor(){
        cls();
        System.out.println("----------------------------------");
        System.out.println("        Block Vendor    ");
        System.out.print("Enter Vendor ID : ");
        String id = s.next(); s.nextLine();
        int vendor_ind = Vendor.is_user(id);
        if(vendor_ind != -1){
            byte cond = Vendor.status[vendor_ind];
            if(cond == 0 || cond == 1){
                Vendor.status[vendor_ind] = -1;
                System.out.println("The Vendor with Vendor ID "+id + " is Disabled");
            }else{
                System.out.println("The Vendor is Already Disabled");
            }
        }else{
            System.out.println("The vendor Doesn't Exist!");
        }
        System.out.print("Press Enter to Continue....");
        s.nextLine();
        
    }

    public void approveSingleVendor(){
        cls();
        System.out.println("----------------------------------");
        System.out.println("        Single Vendor Approval    ");
        System.out.print("Enter Vendor ID : ");
        String id = s.next(); s.nextLine();
        int vendor_ind = Vendor.is_user(id);
        if(vendor_ind != -1){
            byte cond = Vendor.status[vendor_ind];
            if(cond == 0 || cond == -1){
                Vendor.status[vendor_ind] = 1;
                System.out.println("The Vendor with Vendor ID "+id + " is Approved");
            }else{
                System.out.println("The Vendor is Already Approved");
            }
        }else{
            System.out.println("The vendor Doesn't Exist!");
        }
        System.out.print("Press Enter to Continue....");
        s.nextLine();
        
    }

    public void approveVendor(){
        cls();
        System.out.println("----------------------------------");
        System.out.println("        Approve/Disprove Vendor    ");
        // 
        
        for(int i = 0; i < Vendor.user_nos; i++){
            if(Vendor.status[i]==0){
                String id = Vendor.user_ids[i];
                
                System.out.println("Vendor Id : "+id);
                System.out.print("1 - Accept, 2 - Reject,3 - Skip,AnyOther - Close Page\nChoise : ");
                int com = s.nextInt();s.nextLine();

                if(com == 1){
                    Vendor.status[i] = 1;
                    System.out.println("The Vendor '"+id+"' is accepted.");
                }else if(com == 2){
                    Vendor.status[i] = -1;
                    System.out.println("The Vendor '"+id+"' is rejected!.");
                }else if(com == 3){
                    System.out.println("The Vendor '"+id+"' is skipped!.");
                }else{
                    break;
                }
                System.out.print("Press Enter to Continue");
                s.nextLine();

            }
        }

    }

    public void addVendor(){
        while(true){

            cls();

            System.out.println("----------------------------------");
            System.out.println("       Add New Vendor    ");
            System.out.print("User Name : ");
            String user_name = s.next(); s.nextLine();
            
            String mId = "M" + (101 + Vendor.user_nos);
            System.out.println("The MerchantID Generated for Merchant :"+mId);
            

            System.out.print("Password (Use Numbers - 4 digit) : ");
            int pin = s.nextInt(); s.nextLine();


            System.out.println("Confirm Entered Data ? (y/n)");
            String conf = s.next(); s.nextLine();
            if(conf.equals("y")){
                
                // Rewriting data in Arrays
                
                int user_nos = Vendor.user_nos;
                String[] user_names_temp = new String[user_nos+1];
                System.arraycopy(Vendor.user_names, 0 , user_names_temp, 0, user_nos);
                user_names_temp[user_nos] = user_name;
                Vendor.user_names = user_names_temp;


                String[] user_id_temp = new String[user_nos+1];
                System.arraycopy(Vendor.user_ids, 0 , user_id_temp, 0, user_nos);
                user_id_temp[user_nos] = mId;
                Vendor.user_ids = user_id_temp;

                int[] pin_temp = new int[user_nos+1];
                System.arraycopy(Vendor.pins, 0 , pin_temp, 0, user_nos);
                pin_temp[user_nos] = pin;
                Vendor.pins = pin_temp;

                byte[] status_temp = new byte[user_nos+1];
                System.arraycopy(Vendor.status, 0 , status_temp, 0, user_nos);
                status_temp[user_nos] = 1;
                Vendor.status = status_temp;

                Vendor.user_nos++;

                System.out.println("Vendor Added Successfully");
                System.out.print("y - Add Another User, to return press Enter...");
                String com = s.next();s.nextLine();
                
                if(!com.equals("y")){
                break;
                }
            }

        }  
    }



}


class Product{
    static int nos = 7;
    static String names[] = {"GTX 1650","RTX 3090","Maida 500g","Mouse","Levin Jeans","Oreo","Zero Shrit"};
    static int prices[] = {16500, 100000, 200, 160, 500, 100, 500};
    static int sellerIds[] = {2,1,2,0,1,1,0};
    static String discripts[] = {"4 GB Desktop Graphics Card with GDDR 6 VRAM","Killer Graphics with highest Demand","Good Floor","Awesome mouse for office works","Cool Jeans","Eat your heart out","Fit shirt"};
    static int stocks[] = {10,1,12,12,1,12,11};
    static int catogs[] = {2,2,3,2,1,3,1};


    public Product(int id){

    }

    public static int[] append(int[] arr, int key) {
		int[] pin_temp = new int[arr.length+1];
        System.arraycopy(arr, 0 , pin_temp, 0, arr.length);
        pin_temp[arr.length] = key;
        return pin_temp;
	}

    public static String[] append(String[] arr, String key) {
		String[] pin_temp = new String[arr.length+1];
        System.arraycopy(arr, 0 , pin_temp, 0, arr.length);
        pin_temp[arr.length] = key;
        return pin_temp;
	}

    public static int[] remove(int[] arr, int ind) {
		int[] pin_temp = new int[arr.length-1];
        System.arraycopy(arr, 0 , pin_temp, 0, ind);
        System.arraycopy(arr, ind+1, pin_temp, ind, arr.length-ind-1);
        return pin_temp;
	}

    public static String[] remove(String[] arr, int ind) {
		String[] pin_temp = new String[arr.length-1];
        System.arraycopy(arr, 0 , pin_temp, 0, ind);
        System.arraycopy(arr, ind+1, pin_temp, ind, arr.length-ind-1);
        return pin_temp;
	}

    public static void addProduct(String name, int price, int sellerId, String discript, int stock, int catog){
        names = append(names, name);
        prices = append(prices, price);
        sellerIds = append(sellerIds, sellerId);
        discripts = append(discripts, discript);
        stocks = append(stocks, stock);
        catogs = append(catogs, catog);
        nos++;

    }

    public static void removeProduct(int ind){
        names = remove(names, ind);
        prices = remove(prices, ind);
        sellerIds =remove(sellerIds, ind);
        discripts = remove(discripts, ind);
        stocks = remove(stocks, ind);
        catogs = remove(catogs, ind);
        nos--;

    }

    public static boolean isListable(int index){
        if(Vendor.status[sellerIds[index]] == 1 && stocks[index] != 0){
            return true;
        }
        return false;
    }

    public static int[] search(String key){
        int[] feasibles = {};

        for(int i = nos-1; i >=0 ; i--){
            if(names[i].toUpperCase().contains(key.toUpperCase()) && isListable(i)){
                feasibles = append(feasibles, i);
            }
        }

        for(int i = nos-1; i >=0 ; i--){
            if(discripts[i].toUpperCase().contains(key.toUpperCase()) && isListable(i)){
                feasibles = append(feasibles, i);
            }
        }

        return feasibles;

    }

    // Sorting Algorithm
    public static void sort(int[] prods, int sort_par, boolean reverse){
        int[] sort_arr = prices;

        if(reverse){
            for(int i = 0; i < prods.length; i++){
                for(int k = i+1; k < prods.length; k++){
                    if(sort_arr[prods[i]] < sort_arr[prods[k]]){
                        int temp = prods[i];
                        prods[i] = prods[k];
                        prods[k] = temp;
                    }
                }    
            }
        }else{
            for(int i = 0; i < prods.length; i++){
                for(int k = i+1; k < prods.length; k++){
                    if(sort_arr[prods[i]] > sort_arr[prods[k]]){
                        int temp = prods[i];
                        prods[i] = prods[k];
                        prods[k] = temp;
                    }
                }    
            }
        }
        

    }

    // Filtering Algorithms
    public static int[] filter_catog(int[] arr, int cat){
        int[] temp = {};
        for(int k:arr){
            if(catogs[k]==cat && isListable(k)){
                temp = append(temp, k);
            }
        }

        return temp;
    }

    public static int[] filter_catog(int cat){
        int[] temp = {};
        for(int k = 0; k < nos; k++){
            if(catogs[k]==cat && isListable(k)){
                temp = append(temp, k);
            }
        }

        return temp;
    }

    public static int[] filter_price(int[] arr, int low, int high){
        int[] temp = {};
        for(int k:arr){
            if(prices[k] >= low  && prices[k] <= high && isListable(k)){
                temp = append(temp, k);
            }
        }
        return temp;
    }

    public static int[] filter_price(int low, int high){
        int[] temp = {};
        for(int k = 0; k < nos; k++){
            if(prices[k] >= low  && prices[k] <= high && isListable(k)){
                temp = append(temp, k);
            }
        }
        return temp;
    }

    public static int[] filter_vendor(int[] arr ,int vendor_id){
        int[] temp = {};
        for(int k: arr){
            if(sellerIds[k]==vendor_id){
                temp = append(temp, k);
            }
        }

        return temp;
    }

    public static int[] filter_vendor(int vendor_id){
        int[] temp = {};
        for(int k = 0; k < nos; k++){
            if(sellerIds[k]==vendor_id){
                temp = append(temp, k);
            }
        }

        return temp;
    }



}


class Cart{

    ArrayList<Integer> prod_ids = new ArrayList<Integer>(); 
    ArrayList<Integer> counts = new ArrayList<Integer>();

    public Cart(){

    }

    public void addItem(int prod_id, int count){
        Product.stocks[prod_id] -= count;
        if(prod_ids.contains(prod_id)){
            int ind = prod_ids.indexOf(prod_id);
            counts.set(ind, counts.get(ind) + count);
        }else{
        prod_ids.add(prod_id);
        counts.add(count);
        }
    }

    public int[] getItems(){
        int[] temp = new int[prod_ids.size()];
        for(int i = 0; i < prod_ids.size(); i++){
            temp[i] = prod_ids.get(i);
        }
        return temp;
    }

    public void clearCart(){
        prod_ids.clear();
        counts.clear();
    }

    public void removeItem(int prod_id){
        if(prod_ids.contains(prod_id)){
            int ind = prod_ids.indexOf(prod_id);
            prod_ids.remove(ind);
            Product.stocks[prod_id] += counts.get(ind);
            counts.remove(ind);
            System.out.println("Product Removed");
        }else{
            System.out.println("The Entered Product is not Found");
        }

    }

    
}