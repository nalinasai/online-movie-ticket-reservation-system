//online movie ticket reservation  
//program construction exception handling

import java.io.*;
import java.util.*;

class InvalidMovieCodeException extends Exception{
    public InvalidMovieCodeException(String message){
        super(message);
    }
}

class InvalidSeatsInput extends Exception{
    public InvalidSeatsInput(String message){
        super(message);
    }
}

class Movie{
    String moviecode;
    String moviename;
    String date;
    String showtime;
    int totalseats;
    int availableseats;
    double ticketprice;
    String language;
    String genre;

    Movie(String moviecode,String moviename,String date,String showtime,int totalseats,int availableseats,double ticketprice, String language,String genre){
        this.moviecode=moviecode;
        this.moviename=moviename;
        this.date=date;
        this.showtime=showtime;
        this.totalseats=totalseats;
        this.availableseats=availableseats;
        this.ticketprice=ticketprice;
        this.language=language;
        this.genre=genre;
    }
}

class bookings{
    String customername;
    
    Movie selectedmovie;
    String customerfile = "customer.csv";

    bookings(String customername,Movie selectedmovie){
        this.customername=customername;
        
        this.selectedmovie=selectedmovie;
    }
    
    public int reservation(){
        Scanner scan = new Scanner(System.in);
        int seatsinput = 0;

        while(true){
            System.out.print("How many seats you want: ");
            seatsinput = scan.nextInt();

            try{
                if(seatsinput<this.selectedmovie.availableseats){
                    this.selectedmovie.availableseats =this.selectedmovie.availableseats - seatsinput;
                    updateavailableseats(this.selectedmovie.availableseats);
                    System.out.println("reserved sussess");
                    break;
                }
                else{
                    throw new InvalidSeatsInput("There is no available seat count you prefer. Enter again");
                }
            }
            catch(InvalidSeatsInput e){
                System.out.println(e.getMessage());
            }
        }
        return seatsinput;

        
    }

    public void updateavailableseats(int updatedaseats){
        String file = "Movie Reservation Dataset.csv";
        List<String> lines = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(file))){
                String line;
                br.readLine();
                while((line=br.readLine())!=null){
                    String[] details = line.split(",");
                    if((this.selectedmovie.moviecode.equalsIgnoreCase(details[0])) && (this.selectedmovie.date.equalsIgnoreCase(details[2])) && (this.selectedmovie.showtime.equalsIgnoreCase(details[3]))){
                        details[5] = String.valueOf(updatedaseats);
                    }
                    line = String.join(",",details);
                    lines.add(line); 
                }

            
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
                bw.write("Movie Code,Movie Name,Date,Showtime,Total Seats,Available Seats,Ticket Price,Language,Genre");
                bw.newLine();
                for(String linewrite : lines){
                    bw.write(linewrite);
                    bw.newLine();
                }
        }
        
        catch(IOException e){
            System.out.println(e.getMessage());
        }

    }


    public double customerdetail(int seatsinput){
        double totalprice = 0;
        boolean fileexist = new File(customerfile).exists();
        try(BufferedWriter bw1 = new BufferedWriter(new FileWriter(customerfile,true))){
            if(!fileexist){
                bw1.write("Customername,Movie name,show time,date,reserved seats, total price\n");
            }
            totalprice = seatsinput*this.selectedmovie.ticketprice;
            bw1.write(this.customername+","+this.selectedmovie.moviename+","+this.selectedmovie.showtime+","+this.selectedmovie.date+","+seatsinput+","+totalprice+"\n");
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }

        return totalprice;
    }


    public void printthebill(double totalprice,int seatsinput){
        String bill = "bill.txt";
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(bill))){
            writer.write("============Online Movie Ticket\n\n");
            writer.write("Customer name: "+customername+"\n");
            writer.write("Movie name: "+this.selectedmovie.moviename+"\n");
            writer.write("Date: "+this.selectedmovie.date+"\n");
            writer.write("Show time: "+this.selectedmovie.showtime+"\n\n");
            writer.write("-------------------------------------------------------\n");
            writer.write("Booked seats count: "+seatsinput+"\n");
            writer.write("Total price: "+totalprice+"\n");
            writer.write("-------------------------------------------------------\n\n");
            writer.write("bill saved in"+bill+"\n");
            writer.write("THANKYOU");

        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
        
    }



    

}

public class onlinemovie{
    private static final String filename = "Movie Reservation Dataset.csv";
    private static Map<String, List<Movie>> itemsmap = new HashMap<>();

    public static void main(String args[]){
        loadthedetails();

        Scanner scan = new Scanner(System.in);

        System.out.print("Enter the customer name: ");
        String customername = scan.nextLine();

        String code;
        List<Movie> Movies;

        while(true){
            System.out.print("Enter the movie code: ");
            code = scan.nextLine();

            try{
                if(itemsmap.containsKey(code)){
                    Movies = itemsmap.get(code);
                    break;
                }
                else{
                    throw new InvalidMovieCodeException("Invalid Movie Code. Enter the new valid code!!");
                }
            }
            catch(InvalidMovieCodeException e){
                System.out.println(e.getMessage());
            }
            
        }

        System.out.println("Available show times for  "+ Movies.get(0).moviename +":" );
        for(int i=0; i<Movies.size();i++){
            System.out.println("Movie_Name: "+Movies.get(i).moviename+", Available Date: "+Movies.get(i).date);
        }

        List<Movie> availabledatemovie = new ArrayList<>();

        System.out.println();
        while (true){
            System.out.print("Enter the date (yyyy-mm-dd): ");
            String date_input = scan.nextLine();

            boolean founddate = false;
            for(int j=0; j<Movies.size(); j++){
                if(Movies.get(j).date.equalsIgnoreCase(date_input)){
                    System.out.println("Movie_Name: "+Movies.get(j).moviename+", Available showtime: "+Movies.get(j).showtime);
                    availabledatemovie.add(Movies.get(j));
                    founddate = true;
                }
                else{
                    continue;
                }
            }

            if(!founddate){
                System.out.println("There is no show in this date!! Enter invalid date. ");
            }
            else{
                break;
            }
        }

        
        System.out.println();

        Movie selectedmovie = null;

        while(true){
            System.out.print("Enter the show time: ");
            String showtime_input = scan.nextLine();

            boolean foundshowtime = false;

            for(int k=0; k<availabledatemovie.size(); k++){
                if(availabledatemovie.get(k).showtime.equalsIgnoreCase(showtime_input)){
                    selectedmovie = availabledatemovie.get(k);
                    foundshowtime = true;
                    break;
                }
                else{
                    continue;
                }
            }
            if(foundshowtime){
                System.out.println("Movie_Name: "+selectedmovie.moviename+", Available showtime: "+selectedmovie.showtime+", available seats: "+selectedmovie.availableseats+", ticket price: "+selectedmovie.ticketprice);
                break;
            }
            else{
                System.out.println("Enter valid showtime that before mentioned:");
                continue;
            }
        }

        bookings book = new bookings(customername,selectedmovie);
        int seatsinput = book.reservation();
        double totalprice = book.customerdetail(seatsinput);
        book.printthebill(totalprice,seatsinput);


    }


    private static void loadthedetails(){
        try{
            BufferedReader br = new BufferedReader(new FileReader(filename));
            br.readLine();
            String line;
            while ((line=br.readLine())!=null){
                String[] details = line.split(",");
                
                String moviecode = details[0];
                String moviename = details[1];
                String date = details[2];
                String showtime = details[3];
                int totalseats = Integer.parseInt(details[4]);
                int availableseats = Integer.parseInt(details[5]);
                double ticketprice = Double.parseDouble(details[6]);
                String language = details[7];
                String genre = details[8];

                Movie moviedetail = new Movie(moviecode,moviename,date,showtime,totalseats,availableseats,ticketprice,language,genre);
                itemsmap.computeIfAbsent(moviecode, k -> new ArrayList<>()).add(moviedetail);

            }
        }
        catch(IOException e){
            System.out.println("Error loading!!"+e.getMessage());
        }
        

    }
}