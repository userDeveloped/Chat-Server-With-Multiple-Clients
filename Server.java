import java.util.*;
import java.io.*;
import java.net.*;

public class Server
{
    public static HashSet<String> names = new HashSet<>();
    public static HashSet<PrintWriter> writers = new HashSet<>();
        public static void main(String [] args) throws Exception
    {
        ServerSocket lis = new ServerSocket(8000);
        System.out.println("Server Started");
        while(true)
        {
            Handler sam =new Handler(lis.accept());
            sam.start();
        }
    }

    public static class Handler extends Thread
    {
        private String name;
        private Socket sock;
        private Scanner in;
        private PrintWriter out;

        public Handler(Socket sock)
        {
            this.sock = sock;
        }

        public void run() 
        {
            try
            {
                in = new Scanner(sock.getInputStream());
                out = new PrintWriter(sock.getOutputStream(),true);

                out.println("Enter your name: ");
                name = in.nextLine();

                synchronized(names) // so that thread don't try to access the list at the same time and crash
                {
                    names.add(name);
                }
                out.println("Name received start chat");

                for(PrintWriter writer : writers) // tell everyone new guy has joined
                {
                    writer.println("toThere " + name + " has joined");
                }

                synchronized(writers) // so that thread don't try to access the list at the same time and crash
                {
                    writers.add(out); // keep track of the new guys printwriter
                }
            
                while(true)
                {
                    String data = in.nextLine();
                
                    for(PrintWriter writer : writers)
                    {
                        if(writer != out) // jus to make everything look good
                        {
                            writer.println("toThere " + name  +" : "+ data);
                        }
                        else if(data.equals("bye"))
                        {
                            out.println("bye");
                            writers.remove(out);
                        }
                    }  
                } 
            }
            catch(Exception e)
            {}
        }      
    } 
}