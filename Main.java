package ircbot;

/**
 * JavaBot (version 1.2)
 * 
 * MIT License, dydx (Josh Sandlin) <dydx@thenullbyte.org>
 *   
 */

import java.io.*;
import java.net.*;
import java.util.regex.*;
import java.util.Date;
import java.util.Random;

public class Main
{   
    public static void main( String[] args ) throws IOException
    {
        //connection variables
        String server = args[0]; //irc.snappeh.com
        String nick = args[1]; //JavaBot
        String address = "thenullbyte.org";
        String channel = "#"+args[2]; //#enigmagroup
        int port = 6667;
        
        //for security
        String owner = args[3];//dydx
        
        try {
            
            //our socket we're connected with
            Socket irc = new Socket( server, port );
            //out output stream
            BufferedWriter bw = new BufferedWriter( new OutputStreamWriter( irc.getOutputStream() ) );
            //our input stream
            BufferedReader br = new BufferedReader( new InputStreamReader( irc.getInputStream() ) );
            
            //create a new instance of the JavaBot
            Bot JavaBot = new Bot( bw, channel);
            
            //authenticate the JavaBot with the server
            JavaBot.login( nick, address );
            
            String currLine = null;
            while( ( currLine = br.readLine() ) != null )
            {
                
                //compile a regex to check and see if the person calling commands is the owner
                Pattern checkOwner = Pattern.compile( "^:"+owner, Pattern.CASE_INSENSITIVE );
                Matcher ownership = checkOwner.matcher( currLine );
                
                //constantly check for PING's, if the bot sees one, it replies with a PONG
                Pattern pingRegex = Pattern.compile( "^PING", Pattern.CASE_INSENSITIVE );
                Matcher ping = pingRegex.matcher( currLine );
                if( ping.find() )
                {
                    JavaBot.pong();
                }
                
                //check to see if the owner has given the !exit command
                Pattern exitRegex = Pattern.compile( "!exit", Pattern.CASE_INSENSITIVE );
                Matcher exit = exitRegex.matcher( currLine );
                if( exit.find() && ownership.find() )
                {
                    JavaBot.say( "I'm tired, bye yall" );
                    JavaBot.quit();
                }
                
                //parts one room and joins another, gives a nice little going away speech as well
                Pattern joinRegex = Pattern.compile( "!join", Pattern.CASE_INSENSITIVE );
                Matcher join = joinRegex.matcher( currLine );
                if( join.find() && ownership.find() )
                {
                    String[] token = currLine.split( " " );
                    JavaBot.say( "I'm going to go over to " + token[4] + " and see what they're up to over there. Cya." );
                    JavaBot.part();
                    channel = token[4];
                    JavaBot.join( channel );
                    JavaBot.say( "Hey guys!" );
                }
                
                //we should be polite every now and then, this introduces the bot
                Pattern sayhiRegex = Pattern.compile( "!sayhi", Pattern.CASE_INSENSITIVE );
                Matcher sayhi = sayhiRegex.matcher( currLine );
                if( sayhi.find() && ownership.find() )
                {
                    JavaBot.say( "Hi, I'm a JavaBot. I was written by dydx in Java!" );
                }
                
                //more or less a PoC, just returns the current date/time to the IRC channel
                Pattern timeRegex = Pattern.compile( "!time", Pattern.CASE_INSENSITIVE );
                Matcher time = timeRegex.matcher( currLine );
                if( time.find() )
                {
                    Date d = new Date();
                    String message = "The date is " + d ;
                    JavaBot.say( message );
                }
                
                //another little proof of concept. full text regex matching for questions.. HA
                Pattern questionRegex = Pattern.compile( "(how do i|why|what)", Pattern.CASE_INSENSITIVE );
                Matcher question = questionRegex.matcher( currLine );
                if( question.find() || currLine.endsWith( "?" ) )
                {
                    //an array of insults
                    String[] messages = {
                        "Ugh, stupid n00b. You're making my head hurt",
                        "You know, I think a rock has a higher IQ than you",,
                        "/me wants to kill you",
                        "your existance is a waste of oxygen.",
                        "I don't know what makes you so stupid, but it really works!",
                        "Keep talking, someday you'll say something intelligent.",
                        "If brains were taxed, you would certainly be owed a refund.",
                        "You must be an experiment in Artificial Stupidity.",
                        "As a failure, you are a tremendous success."
                    };
                    
                    int i = rand( messages.length, 0 );
                    System.out.println( i );
                    
                    String message = messages[i];
                    
                    JavaBot.say( message );
                }
                
                //yells at a user for no real reason
                Pattern yellRegex = Pattern.compile( "!yell", Pattern.CASE_INSENSITIVE );
                Matcher yell = yellRegex.matcher( currLine );
                if( yell.find() && ownership.find() )
                {
                    String[] token = currLine.split( " " );
                    JavaBot.say( token[4] + ", you suck ass.. you really do.." );
                }
                
                Pattern hmmmRegex = Pattern.compile( "h[m+]", Pattern.CASE_INSENSITIVE );
                Matcher hmmm = hmmmRegex.matcher( currLine );
                if( hmmm.find() )
                {
                    JavaBot.say( hmmm.group()  + " indeed..." );
                }
                
                Pattern stabRegex = Pattern.compile( "stab", Pattern.CASE_INSENSITIVE );
                Matcher stab = stabRegex.matcher( currLine );
                if( stab.find() )
                {
                    JavaBot.say( "AHH! getthat knife away from me NOW!!! AHGH" );
                }
            }
        } catch ( UnknownHostException e ) {
            System.err.println( "No such host" );
        } catch ( IOException e ) {
            System.err.println( "There was an error connecting to the host" );
        } 
    }
    
    //simple random number function
    public static int rand( int hi, int lo )
    {
        Random rn = new Random();
        int n = hi - lo + 1;
        int i  = rn.nextInt() % n;
        if( i < 0 )
            i = -i;
        return lo + i;
    }
}
