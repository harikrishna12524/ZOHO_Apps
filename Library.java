import java.util.*;
import java.time.LocalDate;

public class Library{
    static Scanner s = new Scanner(System.in);

    public static void cls(){
        try{
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public static void enterWait(){
        System.out.print("Press Enter to Continue");
        s.nextLine();
    }

    public static int page(boolean invalid, String heading, String out, String ... options){
        System.out.println("********************************************************");
        System.out.println("             " + heading);

        for(int i = 1; i <= options.length; i++){
            System.out.println(i+" - "+options[i-1]);
        }

        System.out.println("0 - " + out);

        if(invalid){
            System.out.println("Invalid Input !");
        }

        System.out.print("Choise : ");
        int com = s.nextInt(); s.nextLine();
        return com;
    }

    public static void welcome(){
        int com = -1; boolean invalid = false;
        while(com != 0){
            cls();
            com = page(invalid, "Welcome to Harikrishna & Books", "Exit", "Library Member", "Library Admin");
            switch(com){
                case 1:
                    Borrower.userWelcome();
                    break;
                case 2:
                    Admin.adminWelcome();
                    break;
                default:
                    invalid = true;
            }
        }
    }

    public static void  main(String[] args){
        welcome();
    }

}

class Borrower{
    static Scanner s = new Scanner(System.in);

    static ArrayList<Borrower> borrowers= new ArrayList<Borrower>();

    String name;
    String emailId;
    ArrayList<BorrowedBook> books = new ArrayList<BorrowedBook>();
    ArrayList<BorrowedBook> Borrowedbooks = new ArrayList<BorrowedBook>();
    String password;
    int deposit;
    ArrayList<String> penalties = new ArrayList<String>();
    ArrayList<String> borrowHistory = new ArrayList<String>();

    // Borrowed Book Class
    class BorrowedBook{
        String name;
        int number;
        LocalDate borrowDate;
        public BorrowedBook(String name, int number, LocalDate borrowDate){
            this.name = name;
            this.number  = number;
            this.borrowDate = borrowDate;
        }
        public BorrowedBook(String name, int number){
            this.name = name;
            this.number = number;
        }
    }

    static{
        Borrower bor1 = new Borrower("Harikrishna D", "harikrishna12524@gmail.com", "12345678", 1500);
        borrowers.add(bor1);
        bor1 = new Borrower("Jagadeesh S", "j@g.c", "1000", 1500);
        borrowers.add(bor1);
    }

    public Borrower(String name, String emailId, String password, int deposit){
        this.name = name;
        this.emailId = emailId;
        this.password = password;
        this.deposit = deposit;
    }

    public static void withBook(){
        Library.cls();
        ArrayList<Borrower> borrowed = new ArrayList<Borrower>();
        for(int i = 0 ; i  < borrowers.size(); i++){
            if(borrowers.get(i).Borrowedbooks.size() > 0){
                Borrower cur_guy = borrowers.get(i);
                System.out.println("\nBorrower Name : " + cur_guy.name + "\nBorrower mail : " + cur_guy.emailId + "\nBorrowerd Books");
                for(int j = 0; j < cur_guy.Borrowedbooks.size(); j++){
                    BorrowedBook curBook = cur_guy.Borrowedbooks.get(j);
                    System.out.println("Book Name : " + curBook.name + " Return Date : " + curBook.borrowDate.plusDays(15));
                }
            }
        }
        Library.enterWait();
    }

    public static void withBook(int isbn){
        Library.cls();
        ArrayList<Borrower> borrowed = new ArrayList<Borrower>();
        for(int i = 0 ; i  < borrowers.size(); i++){
            if(borrowers.get(i).Borrowedbooks.size() > 0){
                Borrower cur_guy = borrowers.get(i);
                BorrowedBook curBook;
                
                for(int j = 0; j < cur_guy.Borrowedbooks.size(); j++){
                    curBook = cur_guy.Borrowedbooks.get(j);
                    if(curBook.number == isbn){
                        System.out.println("\nBorrower Name : " + cur_guy.name + "\nBorrower mail : " + cur_guy.emailId + "\nBorrowerd Books");
                        System.out.println("Book Name : " + curBook.name + " Return Date : " + curBook.borrowDate.plusDays(15));
                        break;
                    }   
                }
            }
        }
        Library.enterWait();
    }

    public static int is_user(String userName){
        for(int i = 0; i < borrowers.size(); i++){
            if(borrowers.get(i).emailId.equals(userName)){
                return i;
            }
        }
        return -1;
    }

    public int due(LocalDate borrowDate, LocalDate returnDate, int bookPrice){
        int days = 0;
        while(!borrowDate.equals(returnDate)){
            borrowDate = borrowDate.plusDays(1);
            days++;
        }

        if(days <= 15){
            return 0;
        }else{
            int due_amm = 0;
            days = days-15;
            int fac = 1;
            for(int i = 0; i < days/10; i++){
                fac *= 2;
                due_amm += fac*10;
            }
            due_amm += (fac*2)*(days%10);

            if(bookPrice*0.8 < due_amm ){
                return bookPrice*80/100;
            }else{
                return due_amm;
            }
        }

    }

    public static boolean is_email(String email){
        if(!email.contains("@")){
            return false;
        }
        String[] s_email = email.split("@");
        if(!s_email[1].contains(".")){
            return false;
        }
        return true;
    }

    public static void userWelcome(){
        int com = -1; boolean invalid = false;
        while(com != 0){
            Library.cls();
            com =   Library.page(invalid, "Member Login", "Exit", "Existing Member", "New Memeber");
            switch(com){
                case 1:
                    userLogin();
                    break;
                case 2:
                    userReg();
                    break;
                default:
                    invalid = true;
            }
        }
    }

    public static void userLogin(){
        Library.cls();
        System.out.println("User Login");
        System.out.print("Email ID : ");
        String emailId = s.next(); s.nextLine();
        System.out.print("Password : ");
        String password = s.next(); s.nextLine();
        int ind = is_user(emailId);
        if(ind != -1){
            if(borrowers.get(ind).password.equals(password)){
                borrowers.get(ind).userMenu();
            }
        }else{
            System.out.println("Entered Login credentials are wrong");
            Library.enterWait();
        }
    }

    public static void userReg(){
        Library.cls();
        System.out.println("User Registration");

        System.out.print("Name : ");
        String name = s.nextLine();

        String emailId;
        
        do{
        System.out.print("Email ID : ");
        emailId = s.next(); s.nextLine();
        if(!is_email(emailId)){System.out.println("Enter Valid Email Id");continue;}
        if(is_user(emailId) != -1){System.out.println("Email Already in Use");}
        }while(is_user(emailId) != -1 || !is_email(emailId));

        System.out.print("Password : ");
        String password = s.next(); s.nextLine();
        int ind = is_user(emailId);

        int ammount;
        do{
        System.out.print("Deposit Ammount : ");
        ammount = s.nextInt(); s.nextLine();
        if(ammount < 1500){System.out.println("Minimum Deposit is 1500");}
        }while(ammount < 1500);

        System.out.print("Confirm (y/n) : ");String conf = s.next();s.nextLine();

        if(conf.equals("y")){
            Borrower bor1 = new Borrower(name, emailId, password, ammount);
            borrowers.add(bor1);
            System.out.println("You Have been Successfully Registered");
        }else{
            System.out.println("Entered Details Discarded.");
            }
        Library.enterWait();
    }

    public boolean hasBook(int number){
        for(int i = 0; i < books.size(); i++){
            if(books.get(i).number == number){
                return true;
            }
        }
        for(int i = 0; i < Borrowedbooks.size(); i++){
            if(Borrowedbooks.get(i).number == number){
                return true;
            }
        }
        return false;
    }

    public void borrowBook(){
        System.out.print("Enter Book ISBN : ");
        int isbn = s.nextInt(); s.nextLine();
        int ind = Book.getBookByISBN(isbn);
        if(ind != -1){
            if(hasBook(isbn)){
                System.out.println("Cannot Borrow Same Book Twice.");
            }else if(deposit < 500){
                System.out.println("Cannot Borrow Book with deposite less than 500");
            }else{
                BorrowedBook bb = new BorrowedBook(Book.books.get(ind).name, isbn);
                books.add(bb);
                System.out.println("Book Added to Cart");
            }
            Library.enterWait();
        }else{
            System.out.println("Incorrect ISBN");
        }
    }

    public void borrowCart(){
        int com = -1;
        while(com != 0){
            Library.cls();
            System.out.print("       Borrow Cart \n");
            if(books.size() > 3-Borrowedbooks.size()){
                System.out.println("Cannot Borrow more than 3 Books at a time ! Remove Books to Check cart out");
                }

            for(int i = 0; i < books.size(); i++){
                System.out.println("Book Number : " + (i+1) + "\nBook name : " + books.get(i).name + "\nISBN Number : " + books.get(i).number+"\n");
            }

            System.out.println("Options\n1 - Remove a book\n2 - Check Cart out\n0 - back");
            com = s.nextInt(); s.nextLine();

            if(com == 1){
                System.out.print("Book Number (toRemove): ");
                int book_num = s.nextInt(); s.nextLine();
                if(book_num > books.size() || book_num < 1){
                    System.out.println("Invalid input");
                    Library.enterWait();
                }else{
                    books.remove(book_num-1);
                }
            }else if(com == 2){
                if(books.size() > 3-Borrowedbooks.size()){
                    System.out.println("Cannot Borrow more than 3 Books at a time ! Remove Books to Check cart out");
                    Library.enterWait();
                }else{
                    // System.out.print("Enter Borrowing Date (DD/MM/YYYY): ");
                    // String date = s.next(); s.nextLine();
                    // String[] date_arr = date.split("/");
                    // int year = Integer.parseInt(date_arr[2]);
                    // int month = Integer.parseInt(date_arr[1]);
                    // int day = Integer.parseInt(date_arr[0]);
                    // LocalDate bordat = LocalDate.of(year, month, day);
                    LocalDate bordat = LocalDate.now();
                    System.out.println("The Borrowed Books should be returned on or before " + bordat.plusDays(15));
                    
                    for(int j = 0; j < books.size(); j++){
                        borrowHistory.add("Book name : " + Book.books.get(Book.getBookByISBN(books.get(j).number)).name + " Borrowed At : " + bordat);
                        BorrowedBook bb = new BorrowedBook(Book.books.get(Book.getBookByISBN(books.get(j).number)).name, books.get(j).number, bordat);
                        Borrowedbooks.add(bb);
                        Book.books.get(Book.getBookByISBN(books.get(j).number)).count--;
                        Book.books.get(Book.getBookByISBN(books.get(j).number)).timesBorrowed++;
                    }
                    books.clear();
                    

                    Library.enterWait();
                    break;
                }
            }
        }
    }

    public void viewBooks(){
        int com = -1; boolean invalid = false;
        while(com != 0){
            Library.cls();
            System.out.println("       Books");
            for(int i = 1; i <= Book.books.size(); i++){
                Book.books.get(i-1).display();
            }
            System.out.println("\nOption.\n1 - Borrow book\n2 - Book Cart\n0 - Return");
            System.out.print("Choise : ");
            com = s.nextInt(); s.nextLine();
            switch(com){
                case 1:
                    borrowBook();
                    break;
                case 2:
                    borrowCart();
                    break;
            }
        }
    }

    public void borrowedBooks(){
        Library.cls();
        System.out.println("     Borrowed Books       ");
        for(int i = 0; i < Borrowedbooks.size(); i++){
            System.out.println("No : " + (i+1) + "\nBook Name : " + Borrowedbooks.get(i).name + "\nBorrowing Date : " + Borrowedbooks.get(i).borrowDate  +"\n");
        }
        
    }

    public void returnBook(){
        int com = -1;
        while(com != 0){
            borrowedBooks();
            System.out.println("\nOptions :\n1 - Return Book\n2 - Revive Book\n3 - Report Book Lost\n4 - Report Lost Membership card\n0 - Exit");
            com = s.nextInt(); s.nextLine();

            if(com == 1){
                System.out.print("Enter the Book Number : ");
                int bookNum = s.nextInt(); s.nextLine();
                if(bookNum > Borrowedbooks.size() || bookNum < 1){
                    System.out.println("Invalid Input");
                    Library.enterWait();
                }else{
                    System.out.print("Enter Returning Date (DD/MM/YYYY): ");
                    String date = s.next(); s.nextLine();
                    String[] date_arr = date.split("/");
                    int year = Integer.parseInt(date_arr[2]);
                    int month = Integer.parseInt(date_arr[1]);
                    int day = Integer.parseInt(date_arr[0]);
                    LocalDate bordat = LocalDate.of(year, month, day);
                    int bookPrice = Book.books.get(Book.getBookByISBN(Borrowedbooks.get(bookNum-1).number)).price;
                    int due_amm = due(Borrowedbooks.get(bookNum-1).borrowDate, bordat, bookPrice);
                    System.out.println("Fine Due to Late Return of Book is Rs." + due_amm);
                    System.out.print("Pay By cash ? (y/n) : ");
                    String conf = s.next();s.nextLine();
                    if(conf.equals("y")){
                        System.out.println("Fine has been succesfully cleared. To avoid fines return or revive the book in given time.");
                    }else{
                        deposit -= due_amm;
                        System.out.println("Price Has Been Detucted From the Deposit.");
                        penalties.add("Late Submission of Book : " + Borrowedbooks.get(bookNum-1).name + ". Rs. " + due_amm);
                    }
                    Book.books.get(Book.getBookByISBN(Borrowedbooks.get(bookNum-1).number)).count++;
                    Borrowedbooks.remove(bookNum - 1);
                    Library.enterWait();
                }
            }else if(com == 2){
                System.out.print("Enter the Book Number : ");
                int bookNum = s.nextInt(); s.nextLine();
                if(bookNum > Borrowedbooks.size() || bookNum < 1){
                    System.out.println("Invalid Input");
                    Library.enterWait();
                }else{
                    LocalDate bordat = LocalDate.now();
                    System.out.println("The Borrowed Books should be returned on or before " + bordat.plusDays(15));
                    Borrowedbooks.get(bookNum - 1).borrowDate = bordat;
                }
            }else if(com == 3){
                System.out.print("Enter the Book Number : ");
                int bookNum = s.nextInt(); s.nextLine();
                if(bookNum > Borrowedbooks.size() || bookNum < 1){
                    System.out.println("Invalid Input");
                    Library.enterWait();
                }else{
                    System.out.println("Penalty for losing book will be deducted from your deposit");
                    deposit -= (Book.books.get(Book.getBookByISBN(Borrowedbooks.get(bookNum-1).number)).price)/2;
                    penalties.add("Penalty for loosing the book " + Borrowedbooks.get(bookNum-1).name + ". Rs. " + (Book.books.get(Book.getBookByISBN(Borrowedbooks.get(bookNum-1).number)).price)/2);
                    Borrowedbooks.remove(bookNum - 1);
                    
                }
            }else if(com == 4){
                    System.out.println("Penalty Rs.10 for losing book will be deducted from your deposit");
                    penalties.add("Penalty for loosing membership card. Rs. 10");
                    deposit -= 10;
                    Library.enterWait();
            }
        }
    }

    public void deposit(){
        int com = -1; boolean invalid = false;
        
        while(com != 0){
            Library.cls();
            System.out.println("Deposit Remaining in your account : " + deposit);
            com =   Library.page(invalid, "Deposit", "Member Menu", "Deposit Cash" );
            if(com == 1){
                System.out.print("Cash To Deposit : ");
                int cash = s.nextInt();s.nextLine();
                if(cash > 0){
                    deposit += cash;
                    System.out.println("Cash has been deposited in you account");
                }else{
                    System.out.println("Invalid cash.");
                }
                Library.enterWait();
            }else{
                invalid = true;   
            }
        }
    }

    public void viewPenalties(){
        Library.cls();
        System.out.println("Your Past Penalties are as follows");
        for(int i = 0; i < penalties.size(); i++){
            System.out.println(i+1 + " . " + penalties.get(i));
        }
        Library.enterWait();
    }

    public void viewBorrowHistry(){
        Library.cls();
        System.out.println("Your Borrow History");
        for(int i = 0; i < borrowHistory.size(); i++){
            System.out.println(i+1 + " . " + borrowHistory.get(i));
        }
        Library.enterWait();
    }

    public void userMenu(){
        int com = -1; boolean invalid = false;
        while(com != 0){
            Library.cls();
            com =   Library.page(invalid, "Welcome "+ name , "Main Menu", "Borrow Book", "Books", "Return Book", "Deposit", "View Penalities", "Previous Borrows");
            switch(com){
                case 1:
                    viewBooks();
                    break;
                case 2:
                    borrowedBooks();
                    Library.enterWait();
                    break;
                case 3:
                    returnBook();
                    break;
                case 4:
                    deposit();
                    break;
                case 5:
                    viewPenalties();
                    break;
                case 6:
                    viewBorrowHistry();
                    break;
                default:
                    invalid = true;
            }
        }
    }
}

class Admin{
    static Scanner s = new Scanner(System.in);

    static ArrayList<Admin> admins= new ArrayList<Admin>();
    String name;
    String emailId;
    String password;

    static{
        Admin Adm = new Admin("Harikrishna D", "harikrishna12524@gmail.com", "12345678");
        admins.add(Adm);
        Adm = new Admin("Jagan S", "j@g.c", "1000");
        admins.add(Adm);
    }

    public Admin(String name, String emailId, String password){
        this.name = name;
        this.emailId = emailId;
        this.password = password;
    }

    public static int is_user(String userName){
        for(int i = 0; i < admins.size(); i++){
            if(admins.get(i).emailId.equals(userName)){
                return i;
            }
        }
        return -1;
    }

    public static boolean is_email(String email){
        if(!email.contains("@")){
            return false;
        }
        String[] s_email = email.split("@");
        if(!s_email[1].contains(".")){
            return false;
        }
        return true;
    }

    public static void adminWelcome(){
        int com = -1; boolean invalid = false;
        while(com != 0){
            Library.cls();
            com =   Library.page(invalid, "Admin Login", "Exit", "Login");
            switch(com){
                case 1:
                    adminLogin();
                    break;
                default:
                    invalid = true;
            }
        }
    }

    public static void adminLogin(){
        Library.cls();
        System.out.println("User Login");
        System.out.print("Email ID : ");
        String emailId = s.next(); s.nextLine();
        System.out.print("Password : ");
        String password = s.next(); s.nextLine();
        int ind = is_user(emailId);
        if(ind != -1){
            if(admins.get(ind).password.equals(password)){
                admins.get(ind).adminMenu();
            }
        }else{
            System.out.println("Entered Login credentials are wrong");
            Library.enterWait();
        }
    }

    public void adminReg(){
        Library.cls();
        System.out.println("User Registration");

        System.out.print("Name : ");
        String name = s.nextLine();

        String emailId;
        
        do{
        System.out.print("Email ID : ");
        emailId = s.next(); s.nextLine();
        if(!is_email(emailId)){System.out.println("Enter Valid Email Id");continue;}
        if(is_user(emailId) != -1){System.out.println("Email Already in Use");}
        }while(is_user(emailId) != -1 || !is_email(emailId));

        System.out.print("Password : ");
        String password = s.next(); s.nextLine();
        int ind = is_user(emailId);


        System.out.print("Confirm (y/n) : ");String conf = s.next();s.nextLine();

        if(conf.equals("y")){
            Admin adm = new Admin(name, emailId, password);
            admins.add(adm);
            System.out.println("You Have been Successfully Registered");
        }else{
            System.out.println("Entered Details Discarded.");
            }
        Library.enterWait();
    }

    public void addBook(){
        System.out.println("Add a Book");
        System.out.print("Book Name : ");
        String name = s.nextLine();

        System.out.print("Book Number : ");
        int number = s.nextInt(); s.nextLine();

        System.out.print("Count : ");
        int count = s.nextInt(); s.nextLine();

        System.out.print("Price : ");
        int price = s.nextInt(); s.nextLine();

        System.out.print("Adding Date (DD/MM/YYYY): ");
        String date = s.next(); s.nextLine();

        System.out.print("Confirm (y/n) : ");
        String conf = s.next(); s.nextLine();

        if(conf.equals("y")){

            String[] date_arr = date.split("/");
            int year = Integer.parseInt(date_arr[2]);
            int month = Integer.parseInt(date_arr[1]);
            int day = Integer.parseInt(date_arr[0]);

            LocalDate dateOrg = LocalDate.of(year, month, day);
            Book.addBook(name, count, number, price, dateOrg);
            System.out.println("Book has been sucessfully added");
        
        }
        else{
            System.out.println("Entered Details Discarded");

        }

        Library.enterWait();

    }

    public void manageBook(){
        int com = -1; boolean invalid = false;
        while(com != 0){
            Library.cls();
            System.out.println("Manage Existsing books");
            for(int i = 1; i <= Book.books.size(); i++){
                System.out.println("\nNo : " + i);
                Book.books.get(i-1).display(1);
            }

            com =   Library.page(invalid, "Options" , "Main Menu", "Remove Book", "Edit Book");


            if(com == 1){
                System.out.print("Enter Book ISBN : ");
                int isbn = s.nextInt(); s.nextLine();
                int bookIndex = Book.getBookByISBN(isbn); 
                if(bookIndex != -1){
                    Book.books.remove(bookIndex);
                    System.out.println("Book has been removed Successfully");
                }else{
                    System.out.println("Entered ISBN Cannot be found");
                }
                Library.enterWait();
            }else if(com == 2){
                System.out.print("Enter Book ISBN : ");
                int isbn = s.nextInt(); s.nextLine();
                int bookIndex = Book.getBookByISBN(isbn); 
                if(bookIndex != -1){
                    System.out.print("Enter what to change\n1 - Book ISBN\n2 - Book Count\n0 - cancel\nChoise : ");
                    int option = s.nextInt(); s.nextLine();
                    if(option == 1){
                        System.out.print("Enter New ISBN : ");
                        int new_isbn = s.nextInt(); s.nextLine();
                        Book.books.get(bookIndex).number = new_isbn;
                        System.out.println("Value Changed Successfully");
                    }else if(option == 2){
                        System.out.print("Enter Additional books count : ");
                        int new_count = s.nextInt(); s.nextLine();
                        Book.books.get(bookIndex).count += new_count;
                        System.out.println("Value Changed Successfully");
                    }
                }else{
                    System.out.println("Entered ISBN Cannot be found");
                }
            }

            
        
        
        }
    }

    public void showBooks(ArrayList<Book> dispBooks){
        Library.cls();
        System.out.println("Resulted Search");
        for(int i = 0; i < dispBooks.size(); i++){
            System.out.println(i + 1 );
            dispBooks.get(i).display(1);
        }
        Library.enterWait();
    }

    public void bookDetails(){
        int com = -1; boolean invalid = false;
        while(com != 0){
            Library.cls();
            com =   Library.page(invalid, "Search Book Details" , "Admin Menu", "Book By Quantity", "Unborrowed Books", "Highly Borrowed Books", "Students with Outstanding books", "Book with ISBN");
            if(com == 1){
                System.out.print("Quatity Limit : ");
                int Quant = s.nextInt(); s.nextLine();
                showBooks(Book.sortByQuantity(Quant));
            }else if(com == 2){
                showBooks(Book.unborrowed());
            }else if(com == 3){
                System.out.print("Minimum Borrow Lim : ");
                int limit = s.nextInt(); s.nextLine();
                showBooks(Book.sortByBorrow(limit));
            }else if(com == 4){
                Borrower.withBook();
            }else if(com == 5){
                System.out.print("Book ISBN Number : ");
                int isbn = s.nextInt(); s.nextLine();
                Borrower.withBook(isbn);
            }

        }
    }

    public void adminMenu(){
        int com = -1; boolean invalid = false;
        while(com != 0){
            Library.cls();
            com =   Library.page(invalid, "Welcome "+ name , "Main Menu", "Add Member", "Add Admin", "Add Book", "Manage Books", "Advanced Book Analysis");
            switch(com){
                case 1:
                    Borrower.userReg();
                    break;
                case 2:
                    adminReg();
                    break;
                case 3:
                    addBook();
                    break;
                case 4:
                    manageBook();
                    break;
                case 5:
                    bookDetails();
                    break;
                default:
                    invalid = true;
            }
        }
    }
}

class Book{
    static ArrayList<Book> books = new ArrayList<Book>();
    String name;
    int count;
    int number;
    int timesBorrowed;
    LocalDate purchaseDate;
    int price;
    ArrayList<String> borrowers = new ArrayList<String>();

    static{
        LocalDate dt = LocalDate.of(2010, 12, 5);
        addBook("Deja Vu", 10, 1515, 1500, dt);
        addBook("Guns and Roses", 5, 1335, 1500, dt);
        addBook("Python Programming with Java", 2, 1650, 1500, dt);
        addBook("Life is a Joke", 1, 1778, 1500, dt);
        addBook("Why ? Just ask yourself.", 10, 4511, 1500, dt);
        addBook("Jump off a bridge!", 2, 7998, 1500, dt);
    }

    public Book(String name, int count, int number ,int price , LocalDate purchaseDate){
        this.name = name;
        this.count = count;
        this.number = number;
        this.price = price;
        this.purchaseDate = purchaseDate;
        this.timesBorrowed = 0;
    }

    public static int getBookByISBN(int number){
        for(int i = 0; i < books.size(); i++){
            if(books.get(i).number == number){
                return i;
            }
        }
        return -1;
    }

    public static void addBook(String name, int count, int number, int price, LocalDate purchaseDate){
        Book book = new Book(name, count, number, price, purchaseDate);
        books.add(book);
    }

    public void display(){
        if(count!=0){
        System.out.println("\nBook Name : " + name + "\nISBN Number : " + number);}
    }

    public void display(int one){
        System.out.println("Book Name : " + name + "\nISBN Number : " + number + "\nCount : " + count + "\nNo of Borrows : " + timesBorrowed + "\nBuying date : " + purchaseDate + "\n");
    }

    public static ArrayList<Book> sortByQuantity(int Quant){
        ArrayList<Book> temp_books = new ArrayList<Book>();
        for(int i = 0; i < books.size(); i++){
            if(books.get(i).count < Quant){
                temp_books.add(books.get(i));
            }
        }

        for(int i = 0; i < temp_books.size(); i++){
            for(int j = i; j < temp_books.size(); j++){
                if(temp_books.get(j).count < temp_books.get(i).count ){
                    Book sus = temp_books.get(i);
                    temp_books.set(i, temp_books.get(j));
                    temp_books.set(j, sus);
                }
            }
        }
        return temp_books;
    }

    public static ArrayList<Book> unborrowed(){
        ArrayList<Book> temp_books = new ArrayList<Book>();
        for(int i = 0; i < books.size(); i++){
            if(books.get(i).timesBorrowed == 0){
                temp_books.add(books.get(i));
            }
        }

        return temp_books;
    }

    public static ArrayList<Book> sortByBorrow(int limit){
        ArrayList<Book> temp_books = new ArrayList<Book>();
        for(int i = 0; i < books.size(); i++){
            if(books.get(i).timesBorrowed >= limit){
                temp_books.add(books.get(i));
            }
        }

        for(int i = 0; i < temp_books.size(); i++){
            for(int j = i; j < temp_books.size(); j++){
                if(temp_books.get(j).timesBorrowed > temp_books.get(i).timesBorrowed ){
                    Book sus = temp_books.get(i);
                    temp_books.set(i, temp_books.get(j));
                    temp_books.set(j, sus);
                }
            }
        }
        return temp_books;
    }
}