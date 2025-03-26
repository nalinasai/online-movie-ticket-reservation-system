//online movie ticket reservation  
//program construction exception handling

import java.io.*;
import java.util.*;

class InvalidMovieCodeException extends Exception{
    public InvalidMovieCodeException(String message){
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

public class onlinemovie{
    private static final String filename = "Movie Reservation Dataset.csv";
    private static Map<String, List<Movie>> itemsmap = new HashMap<>();

    public static void main(String args[]){
        loadthedetails();

        Scanner scan = new Scanner(System.in);
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

        ///////


        
        
        

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