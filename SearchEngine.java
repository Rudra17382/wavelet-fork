import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

/**
 * Boiler Plate Code copied from NumberServer.java
 */

 
/**
 * Handler that handles requests, interacts with the storage
 * accesses it and manipulate it.
 */
class Handler implements URLHandler {

    //Identifier for invalid query. DO NOT SHARE. Update if leaked.
    String invalidQueryIndentifier = "5d6870408eba79cbf114";

    /**
     * Storage of the list of strings that will be accessed 
     * and manipulated by the user
     */
    ArrayList<String> listOfStrings = new ArrayList<String>();

    /**
     * Split the query in the argument url.
     * Use = as the split operator to split around
     * @param url URL to split the query of
     * @return string list of split query around =
     */
    public String[] splitQuery(URI url) {
        return url.getQuery().split("=");
    }

    /**
     * Validate the given query by checking that format "s=abc" is 
     * being followed in the query
     * @param url Input URL
     * @return validated query string or invalid identifier
     */
    public String validateQuery(URI url) {

        //Splitting the query
        String[] query = splitQuery(url);

        //Check if first element is "s" otherwise return invalid query
        if (query[0].equals("s")) {
            return query[1];
        } 
        return invalidQueryIndentifier;
    }

    /**
     * The deafult path that webpage goes back to
     * @return webpage with the current list of strings
     */
    public String defaultPath() {
        return "Current list: " + String.join(", ", listOfStrings);
    }
    
    /**
     * Search function to search and find out if the given 
     * query is in one or more of the strings in the list of strings
     * @param url Input URL
     * @return Webpage with a list of strings that contain the query
     */
    public String search(URI url) {

        // Validate the query; make an arraylist to store correct strings
        String query = validateQuery(url);
        ArrayList<String> matchingResults = new ArrayList<String>();

        // Invalid query
        if (query.equals(invalidQueryIndentifier)) {
            return "Invalid Path and/or Query";
        }

        // Checking for every string in list of strings
        for (String string : listOfStrings) {
            //Add to matchingResults arraylist if string contains query
            if (string.contains(query)) {
                matchingResults.add(string);
            }
        }

        // If there is atleast one result then return the webpage
        if (matchingResults.size() > 0) {
            return (
                "Following strings contain the query: " + 
                String.join(", ", matchingResults)
            );
        }

        // No matching strings
        return "No Strings contained the given query :/";
    }

    /**
     * Add function to add the given query to the list of strings
     * @param url Input URL
     * @return Confirmation + Current list
     */
    public String add(URI url) {

        // Validate the query
        String query = validateQuery(url);

        // Invalid Query
        if (query.equals(invalidQueryIndentifier)) {
            return "Invalid Path and/or Query!";
        }

        // Add the query to the list of strings and return confirmation
        listOfStrings.add(query);
        return "Query Added! " + defaultPath();
    }

    /**
     * Handle the given requests in the form of URLs
     * @param url given request in the form of a url
     * @return The resulting webpage to be shown
     */
    public String handleRequest(URI url) {
        // Get the path
        String path = url.getPath();

        // Call function based on given path
        if (path.equals("/")) {
            return defaultPath();
        } else if (path.contains("/search")) {
            return search(url);
        } else if (path.contains("/add")) {
            return add(url);
        } else {
            return "404 Not Found; Invalid Path!";
        }
    }
}


/**
 * Contains the Main method
 */
public class SearchEngine {

    /**
     * Main method
     * Enter port number in command line argument to run
     * a local server on the designated port
     * Visit http://localhost:<port number> to see the server
     * @param args port number is 0th arg
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        //Check if port number is specified
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        //parse into string and start a server on the port
        int port = Integer.parseInt(args[0]);
        Server.start(port, new Handler());
    }
}