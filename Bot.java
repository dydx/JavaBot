package ircbot;

import java.io.*;

/**
 * The skeleton of the IRC Bot. 
 * 
 * @author dydx
 */
public class Bot
{
    BufferedWriter bw;
    String channel;
    
    /**
     * Bot class constructor, set the defaults for the output stream and the channel
     * @param writer
     * @param chan
     * @param own
     */
    Bot( BufferedWriter writer, String chan )
    {
        bw = writer;
        channel = chan;
    }
    
    /**
     * This is the main part of the bot, it replies to PING's
     * @throws java.io.IOException
     */
    public void pong() throws IOException
    {
        bw.write( "PONG " + channel + "\n" );
        bw.flush();
    }
    
    /**
     * General purpose method for inputting into the channel at hand
     * @param message
     * @throws java.io.IOException
     */
    public void say( String message ) throws IOException
    {
        bw.write( "PRIVMSG " + channel + " :" + message + "\n" );
        bw.flush();
    }
    
    /**
     * Joins a specified IRC channel and sets this.channel accordingly
     * @param channel
     * @throws java.io.IOException
     */
    public void join( String chan ) throws IOException
    {
        bw.write( "JOIN " + chan + "\n" );
        bw.flush();
        
        //we're doing this so that there's no confusion
        //as to which channel the bot is in
        channel = chan;
    }
    
    /**
     * Leave the curent channel
     * @throws java.io.IOException
     */
    public void part() throws IOException
    {
        bw.write( "PART " + channel + "\n" );
        bw.flush();
    }
    
    /**
     * Quit the current IRC session
     * @param reason
     * @throws java.io.IOException
     */
    public void quit() throws IOException
    {
        bw.write( "QUIT " + channel + "\n" );
        bw.flush();
    }
    
    /**
     * Class method used to authenticate the Bot with the IRC server
     * @param nick
     * @param address
     * @throws java.io.IOException
     */
    public void login( String nick, String address ) throws IOException
    {
        bw.write( "NICK " + nick + "\n" );
        bw.write( "USER " + nick + " " + address + ": Java Bot\n" );
        bw.flush();
        
        bw.write( "JOIN " + channel + "\n" );
        bw.flush();
    }
    
    /**
     * General method for yelling at users, might not need to use it after all
     * @param message
     * @throws java.io.IOException
     */
    public void yell( String message ) throws IOException
    {
        say( message );
    }
    
    /**
     * Will eventually be used to search for crap on Wikipedia
     * @throws java.io.IOException
     */
    public void wiki() throws IOException
    {
        say( "Sorry folks, but that feature isn't working just yet." );
    }
}
