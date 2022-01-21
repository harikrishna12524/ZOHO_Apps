import java.util.*;

public class RailwayBooking{
    static Scanner  s = new Scanner(System.in);

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
        int com = 0;
        loop : while(true){
            cls();  
            System.out.println("----------------------------------");
            System.out.println("            Welcome to E - Rail Booking  ");
            System.out.println("Select User Type:\n1 - Customer\n0 - Exit");
            
            if(com == -1){
                System.out.println("Please Enter a valid Choise.");
            }

            System.out.print("Choise : ");
            com = s.nextInt();s.nextLine();

            switch(com){
                case 1:
                    Customer.customerWelcome();
                    break;
                case 0:
                    break loop;
                default:
                    com = -1;
            } 
        }
    }

    public static void main(String[] args){



        welcome();
    }
}

class Customer{
    static Scanner s = new Scanner(System.in);
    static ArrayList<Customer> customers = new ArrayList<Customer>();
    String name;
    String user_id;
    String phone;
    boolean is_male;
    int pin;
    int age;
    int _index;
    int wallet_bal;
    ArrayList<String> tickets = new ArrayList<String>();

    static{
        Customer c1 = new Customer("Harikrishna D", "hari1", "9445842664", true, 1000, 25, 0);
        Customer c2 = new Customer("Aathish R K", "aadi1", "9445842664", true, 1001, 25, 1);
        customers.add(c1);
        customers.add(c2);
    }

    public Customer(String name,String user_id,String phone,boolean is_male,int  pin,int age, int _index){
        this.name = name;
        this.user_id = user_id;
        this.phone = phone;
        this.is_male = is_male;
        this.pin = pin;
        this.age = age;
        this._index = _index;
        this.wallet_bal = 1000;
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

    public static int isUser(String user_id){
        for(int i = 0; i < customers.size(); i++){
            Customer customer = customers.get(i);
            if (customer.user_id.equals(user_id)){
                return i;
            }
        }
        return -1;
    }

    public static void enter_wait(){
        System.out.print("Press Enter to Continue...");
        s.nextLine();
    }

    public static void customerWelcome(){
        int com = 0;
        loop : while(true){
            cls();  
            System.out.println("-------------------------------------------");
            System.out.println("   Welcome to E - Rail Booking - Customer  ");
            System.out.println("Select User Type:\n1 - Existing Customer\n2 - New Customer\n0 - Exit");
            
            if(com == -1){
                System.out.println("Please Enter a valid Choise.");
            }

            System.out.print("Choise : ");
            com = s.nextInt();s.nextLine();

            switch(com){
                case 1:
                    customerLogin();
                    break;
                case 2:
                    newCustomer();
                    break;
                case 0:
                    break loop;
                default:
                    com = -1;
            } 
        }
    }

    public static void customerLogin(){
        int attempt = 0;
        while(attempt++ < 3){
            cls();
            System.out.println("----------------------------------");
            System.out.println("   Welcome to E - Rail Booking - Customer Login  ");

            System.out.print("User Id : ");
            String user_id = s.next(); s.nextLine();

            System.out.print("Pin : ");
            int pin = s.nextInt(); s.nextLine();

            int ind = isUser(user_id);
            // System.out.println(user_id);

            if(ind != -1){
                if(customers.get(ind).pin == pin){
                    customers.get(ind).userMenu();
                    break;
                }
            }
            System.out.println("Entered Credentials are Wrong");
            enter_wait();

        }
        System.out.println("Maximum Attempts Exceeded! Please Try after sometime");
    }

    public static void newCustomer(){

        System.out.println("--------------------------------------------");
        System.out.println("  New Customer Registration ");

        System.out.print("Name : ");
        String name = s.nextLine();
        
        String message = "User Id: ";
        String id;
        do{
            System.out.print("User Id : ");
            id = s.next(); s.nextLine();
            message = "User Id Already Exists\nUser Id : ";
        }
        while(isUser(id)!=-1);
        
        System.out.print("Pin : ");
        int pin = s.nextInt(); s.nextLine();
        
        System.out.print("Age : ");
        int age = s.nextInt(); s.nextLine();

        System.out.print("Phone Number : ");
        String phone = s.next(); s.nextLine();


        System.out.print("Gender(M/F) : ");
        boolean is_male = false;
        String gender = s.next(); s.nextLine();
        if(gender.equals("M")){
            is_male = true;
        }

        System.out.print("Confirm (y/n) : ");
        String conf = s.next(); s.nextLine();
        if(conf.equals("y")){
            // Creating a user object
            int _index = customers.size();
            Customer cur_usr = new Customer(name, id, phone, is_male, pin, age, _index) ;
            customers.add(cur_usr);

            System.out.println("User Has been Created Successfully");
        }else{
            System.out.println("Entered Details are discarded");
        }

        enter_wait();



    }

    public void booking(){
        int com = 0;
        while (true){
            cls();
            System.out.println("-------------------------------------------");
            System.out.println("Please Select Train (0 - Exit)");
            int train_count = 1;
            for(Train t:Train.trains){
                System.out.println(train_count++ + " - " + t.number + " : " + t.name);
            }

            if(com == -1){
                System.out.println("Enter a valid option: ");
            }

            System.out.print("Enter Choise : " );
            com= s.nextInt(); s.nextLine();

            if(com > 0 && com <= train_count){
                Train.trains.get(com-1).booking(_index);
                // t.booking(_index);
                break;
            }
            else if(com == 0){
                break;
            }else{  
                com =- 1;
            }
        }
        

    }
    
    public void wallet(){
        System.out.println(wallet_bal);
        enter_wait();
    }

    public void cancelation(){
        int com = 0;
        while (true){
            cls();
            System.out.println("-------------------------------------------");
            System.out.println("Please Select Ticket to Cancel(0 - Exit)");
            int tic_count = tickets.size();
            for(int i = 1; i <= tic_count; i++){
                String[] details = tickets.get(i-1).split(":");
                int train_num = Integer.parseInt(details[0]);
                int ticket_num = Integer.parseInt(details[1]);
                System.out.println("Ticket ID :" + i);
                Train cur_train = Train.trains.get(Train.getTrainByNumber(train_num));
                cur_train.displayTicket(cur_train.getTicketByRegNo(ticket_num));
                // System.out.println(tickets.get(i-1));
            }

            if(com == -1){
                System.out.println("Enter a valid option: ");
            }

            System.out.print("Enter Choise : " );
            com= s.nextInt(); s.nextLine();

            if(com > 0 && com <= tic_count){
                String[] details = tickets.get(com-1).split(":");
                int train_num = Integer.parseInt(details[0]);
                int ticket_num = Integer.parseInt(details[1]);

                Train cur_train = Train.trains.get(Train.getTrainByNumber(train_num));
                System.out.println("Are you sure you want to cancel the ticket ? Refund : " + (cur_train.cancelReturn(cur_train.getTicketByRegNo(ticket_num))));
                System.out.print("Confirm Ticket Cancel (y/n) : " );
                String conf = s.next();s.nextLine();
                if(conf.equals("y")){
                    // Train cur_train = Train.trains.get(Train.getTrainByNumber(train_num))
                    wallet_bal += cur_train.cancelTicket(cur_train.getTicketByRegNo(ticket_num));
                    System.out.println("Ticket Cancelled !. The refund will be credited to wallet Balance");
                    tickets.remove(com-1);
                }else{
                    System.out.println("Ticket Cancellation aborted");
                }
                enter_wait();
            }

            else if(com == 0){
                break;
            }else{  
                com =- 1;
            }
        }
    }

    public void add_ticket(String ticket, int price){
        tickets.add(ticket);
        wallet_bal -= price;
    }

    public void userMenu(){
        int com = 0;
        loop : while(true){
            cls();  
            System.out.println("-------------------------------------------");
            System.out.println("   Welcome to E - Rail Booking, " + name);
            System.out.println("Select User Type:\n1 - Booking\n2 - Cancelation\n3 - Wallet\n0 - Exit");
            
            if(com == -1){
                System.out.println("Please Enter a valid Choise.");
            }

            System.out.print("Choise : ");
            com = s.nextInt();s.nextLine();

            switch(com){
                case 1:
                    booking();
                    break;
                case 2:
                    cancelation();
                    break;
                case 3:
                    wallet();
                    break;
                case 0:
                    break loop;
                default:
                    com = -1;
            } 
        }
    }


}

class Train{
    static Scanner s = new Scanner(System.in);
    static ArrayList<Train> trains = new ArrayList<Train>();

    // ArrayList<String> stops = new ArrayList<String>(); // Converted to String Array for performance
    String[] stops;
    int[][] seats;
    int number;
    String name;
    int seat_nos;
    int waiting_list;
    int[] prices; 
    ArrayList<Ticket> tickets = new ArrayList<Ticket>();
    

    static{
        /*ArrayList<String> _stops = new ArrayList<String>();
        _stops.add("CBE");
        _stops.add("TUP");
        _stops.add("ERD");
        _stops.add("SAL");
        _stops.add("KPD");
        _stops.add("MAS");*/

        String[] _stops = {"CBE", "TUP", "ERD", "SAL", "KPD", "MAS"};
        int[] _prices = {50,60,80,150,110};
        Train t1 = new Train("Coimbatore-Chennai Express", 665110, 10, _prices, _stops);
        trains.add(t1);
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

    public static void enter_wait(){
        System.out.print("Press Enter to Continue...");
        s.nextLine();
    }

    public static int getTrainByNumber(int train_num){
        for(int i = 0; i < trains.size(); i++){
            if(trains.get(i).number == train_num){
                return i;
            }
        }
        return -1;
    }

    public int getTicketByRegNo(int ticketRegNo){
        for(int i = 0; i < tickets.size(); i++){
            if (tickets.get(i).regNo == ticketRegNo){
                return i;
            }
            
        }
        return -1;
    }

    public Train(String name, int number, int seat_nos,int[] prices, String[] stops){
        this.name = name;
        this.number = number;
        this.seat_nos = seat_nos;
        this.stops = stops;
        this.prices = prices;
        this.seats = new int[seat_nos][stops.length-1];
        this.waiting_list = 0;
        empty();
    }

    class Ticket{
        static int Ticket_Id = 0;

        int _index;
        int price;
        int regNo;
        int board;
        int leave;
        int seat_nos;
        int passengerId;
        boolean confirmed;
        ArrayList<Integer> seat_nums = new ArrayList<Integer>();

        public Ticket(int _index, int price, int board, int leave, int passengerId, ArrayList<Integer> seat_nums){
            this._index = _index;
            this.price = price;
            this.regNo = Ticket_Id++;
            this.board = board;
            this.leave = leave;
            this.passengerId = passengerId;
            this.seat_nums = seat_nums;
            this.seat_nos = seat_nums.size();
            this.confirmed = true;
        }

        public Ticket(int _index, int price, int board, int leave, int passengerId,int seat_nos){
            this._index = _index;
            this.price = price;
            this.regNo = Ticket_Id++;
            this.board = board;
            this.leave = leave;
            this.passengerId = passengerId;
            this.seat_nos = seat_nos;
            this.confirmed = false;

        }


    }

    public void empty(){

        for(int i = 0; i < seats.length; i++){
            for(int j = 0; j < seats[0].length; j++){
                seats[i][j] = 0;
            }
        }

    }

    public void disp_seats(){
        for(int i = 0; i < seats.length; i++){
            for(int j = 0; j < seats[0].length; j++){
                System.out.print(seats[i][j] + " ");
            }
            System.out.println();
        }
    }

    public int gen_price(int start, int stop){
        int price = 0;
        for(int i = start; i < stop; i++){
            price += prices[i];
        }
        return price;

    }

    public boolean seat_avail(int start, int end, int nos){
        int avail = 0;
        // System.out.println("Start : " + start + " Stop : "+ end);
        for(int i = 0; i < seats.length; i++){
            boolean is_free = true;
            for(int j = start; j < end; j++){
                if(seats[i][j] == 1){
                    is_free = false;
                }
            }
            if(is_free){
                avail++;
            }
            if(avail==nos){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Integer> seat_book(int start, int end, int nos){
        ArrayList<Integer> booked_seats = new ArrayList<Integer>();
        for(int i = 0; i < seats.length; i++){
            
            boolean is_free = true;
            for(int j = start; j < end; j++){
                if(seats[i][j] == 1){
                    is_free = false;
                }
            }

            if(is_free){
                for(int j = start; j < end; j++){
                    seats[i][j] = 1;
                }
                booked_seats.add(i);

                if(--nos == 0){
                break;
                }
            }
               
        }
        return booked_seats; 
        
    }
    
    public void booking(int user_id){
        int com = 0, start = -1, leave = 1;
        boolean stop = false;
        while (true){
            cls();
            disp_seats();
            System.out.println("-------------------------------------------");
            System.out.println("Please " + (stop==false?"Boarding":"Leaving") + " Stop (0 - Exit)");
            
            for(int i = start+2; i <= stops.length-leave; i++){
                System.out.println(i + " - " + stops[i-1]);
            }

            if(com == -1){System.out.println("Please Enter Valid Input !");}

            if(!stop){
                
                System.out.print("Boarding Point : ");
                com = s.nextInt(); s.nextLine();

                if(com==0){
                    break;
                }else if(com > 0 && com < stops.length){
                    stop = true;
                    start = com-1;
                    leave = 0;
                }else if(com == stops.length){
                    System.out.println("Don't Enter The Last Stop");
                    enter_wait();
                }else{
                    com = -1;
                }
            }
            
            else{
                
                System.out.println("Boarding Point : "+ stops[start]);
                System.out.print("Leaving Point : ");
                com = s.nextInt(); s.nextLine();
                if(com==0){
                    break;
                }else if(com > start && com <= stops.length){
                    leave = com-1;
                    System.out.println("Boarding Point : " + stops[start] + " - Leaving Point : " + stops[leave]);

                    int count;
                    while(true){
                    System.out.print("No of Passengers : ");
                    count = s.nextInt(); s.nextLine();
                    if(count < 1){System.out.println("Invalid No of Passengers !");continue;}
                    else if(count > seat_nos){System.out.println("Required Seats not available in this train");continue;}
                    break;
                    }

                    if(seat_avail(start, leave, count)){

                        int ticket_price = gen_price(start, leave)*count;

                        System.out.println("The Ticket(s) Price is : "+ ticket_price);
                        System.out.print("Confirm Purchase (y/n) : " );
                        String conf = s.next(); s.nextLine();

                        if(conf.equals("y")){
                            ArrayList<Integer> booked_seats = seat_book(start, leave, count);
                            int _index = tickets.size();
                            Ticket tic = new Ticket(_index, ticket_price, start, leave, user_id, booked_seats);
                            tickets.add(tic);
                            Customer.customers.get(user_id).add_ticket(number+":"+tic.regNo,ticket_price);
                            System.out.println("Ticket Booked!");

                        }else{
                            System.out.println("Details Discarded ! ");
                        }

                    }else{
                        if(waiting_list == 10){
                            System.out.println("Requied seat Unavailable and Waiting list is full.");
                        }else{
                            System.out.println("Required Seat Unavailable! Add to Waiting list");
                            int ticket_price = gen_price(start, leave);
                            System.out.println("The Ticket(s) Price is : "+ (ticket_price*count));
                            System.out.print("Confirm Purchase (y/n) : " );
                            String conf = s.next(); s.nextLine();

                            if(conf.equals("y")){
                                int _index = tickets.size();
                                Ticket tic = new Ticket(_index, ticket_price, start, leave, user_id, count);
                                tickets.add(tic);
                                Customer.customers.get(user_id).add_ticket(number+":"+tic.regNo,ticket_price);
                                System.out.println("Added To Waiting List!");
                            }else{
                                System.out.println("Details Discarded !");
                            }
                        }
                    }

                    enter_wait();
                    break;
                }else{
                    com = -1;
                }

            }
        }
    }

    public void displayTicket(int ticket_id){
        Ticket ticket = tickets.get(ticket_id);
        System.out.println("Ticket Number : "+ticket.regNo +"\nBoarding : " + stops[ticket.board] + "\nUnboarding : " + stops[ticket.leave] + "\nTicket Status : " + (ticket.confirmed?"Confirmed":"Waiting") +"\nNumber of Passengers : "+ticket.seat_nos+ "\nPrice : " + ticket.price);
        if(ticket.confirmed){
            System.out.print("Seat Number : ");
            for(int i:ticket.seat_nums){
                System.out.print(i+" ");
            }
            System.out.println("\n");
        }
    }

    public int cancelTicket(int ticket_ind){
        Ticket ticket = tickets.get(ticket_ind);
        int price = 0;
        if(ticket.confirmed){
            for(int i:ticket.seat_nums){
                for(int j = ticket.board; j < ticket.leave; j++){
                    seats[i][j] = 0;
                }
            }
            price = ticket.price/2;
            tickets.remove(ticket_ind);
            evalWaitingList();
        }else{
            price = ticket.price;
            tickets.remove(ticket_ind);
        }
        return price;
    }

    public int cancelReturn(int ticket_ind){
        Ticket ticket = tickets.get(ticket_ind);
        int price = 0;
        if(ticket.confirmed){
            price = ticket.price/2;
        }else{
            price = ticket.price;
        }
        return price;
    }

    public void evalWaitingList(){
        // System.out.println("Tickets are reevaluated");
        for(Ticket tic:tickets){
            // System.out.println("Ticket Eval :"+tic.regNo+" Status : "+tic.confirmed);
            if(!tic.confirmed){

                if(seat_avail(tic.board, tic.leave, tic.seat_nos)){
                    // System.out.println("Why ??");
                    tic.seat_nums = seat_book(tic.board, tic.leave, tic.seat_nos);
                    tic.confirmed = true;

                }
            }
        }
    }


    


}