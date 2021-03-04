/* @formatter:off
 *
 * Dave Rosenberg
 * Comp 2000 - Data Structures
 * Lab: Testing infrastructure
 * Fall, 2020
 * 
 * Usage restrictions:
 * 
 * You may use this code for exploration, experimentation, and furthering your
 * learning for this course. You may not use this code for any other
 * assignments, in my course or elsewhere, without explicit permission, in
 * advance, from myself (and the instructor of any other course).
 * 
 * Further, you may not post nor otherwise share this code with anyone other than
 * current students in my sections of this course. Violation of these usage
 * restrictions will be considered a violation of the Wentworth Institute of
 * Technology Academic Honesty Policy.
 *
 * Do not remove this notice.
 *
 * @formatter:on
 */

package edu.wit.dcsn.dmr.testing.junit ;

/**
 * Support specific Exception detection
 */
public class PlaceholderException extends Exception
    {

    /** for serialization */
    static final long serialVersionUID = 1L ;

    /**
     * default constructor with no status
     */
    public PlaceholderException()
        {
        super() ;

        }   // end no-arg constructor


    /**
     * constructor with message
     * 
     * @param message 
     *     the message text associated with this exception
     */
    PlaceholderException( final String message )
        {
        super( message ) ;

        }   // end 1-arg constructor (with message)


    /**
     * constructor with status (nonstandard)
     *
     * @param status
     *     the status given, for example, to {@code System.exit()}
     */
    PlaceholderException( final int status )
        {
        super( Integer.toString( status ) ) ;

        }   // end 1-arg constructor (nonstandard)

    }   // end class PlaceholderException