import java.util.*;
import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) throws Exception
    {


        Socket sock = new Socket("127.0.0.1",8000);  //binding the  socket with the ip address and port number
        Scanner in = new Scanner (sock.getInputStream()); // returns the inputstream for this given socket
        PrintWriter out = new PrintWriter(sock.getOutputStream(),true); // returns the outputstream for this given socket (true could be removed "flush")
        Scanner inp = new Scanner(System.in);   // regular scanner from terminal

        Res dr = new Res(sock,in,out,inp);  // creating instance of class with given constructores 
        dr.start(); // start the thread

    
    }
}

class Res extends Thread
{
    Socket sock;
    Scanner in;
    PrintWriter out;
    Scanner input;

        public Res(Socket sock, Scanner in, PrintWriter out, Scanner input)
        {
            this.sock = sock;
            this.in = in;
            this.out = out;
            this.input = input;
        }

        // then goes here
        public void run()
        {
            Date date = new Date();
            System.out.print(in.nextLine());
            String name = input.nextLine();
            while(name.isBlank())
            {
                System.out.print("Enter Correct Name: ");
                name = input.nextLine();
            }
            out.println(name);
            
            String ack = in.nextLine();
            System.out.println(ack);

            System.out.print("Chat ");
            
            Send s = new Send(out,input);
            s.start();
            
            while(in.hasNextLine())
            {
                
                String data = in.nextLine();

                if(data.startsWith("toThere"))
                {
                    Date datee = new Date();
                    System.out.print(datee.toString().substring(11, 20)); // print time
                    System.out.println(data.substring(8)); // print the message
                    System.out.print("Chat ");
                }
                else if(data.startsWith("bye"))
                {
                    System.out.println("You have been removed from chat");
                    break;
                }
            }
            try
            {
                sock.close();
                in.close();;
                out.close();;
                input.close();
            }
            catch(IOException e)
            {

            }
        }
    
}

// begins here
class Send extends Thread
{
    Scanner inp;
    PrintWriter out;
    public Send(PrintWriter out, Scanner inp)
    {
        this.out = out;
        this.inp = inp;
    }

    public void run()
    {
        while(true)
        {
            String mes = inp.nextLine();
            out.println(mes);
            System.out.print("Chat " );
            if(mes.equals("bye"))
            {
                break;
            }
        }
    }
}