import java.util.Scanner;

public class MyClient {
    

    public static  int x;
    public static  int y;
    static Scanner scanner = new Scanner(System.in);




    public static void main(String args[]) {
                    
            
      

        try{
         x  = scanner.nextInt();
            y = scanner.nextInt();
            Adder stub=(Adder)Naming.lookup("rmi://localhost:5000/sonoo");   
System.out.println(stub.add(x,y));

}catch(Exception e){System.out.println(e);}
}

}
