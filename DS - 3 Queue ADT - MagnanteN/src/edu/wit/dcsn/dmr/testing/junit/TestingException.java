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
 * Runtime exception to wrap lower-level exceptions during testing - uses Exception
 * chaining
 *
 * @author Dave Rosenberg
 * @version 1.0.0 2020-09-13 Initial implementation
 */
public class TestingException extends RuntimeException
    {
    /**
     * Support serialization
     */
    private static final long serialVersionUID = 1L ;


    // constructors

    /**
     * @param cause
     *     the 'wrapped' exception
     */
    public TestingException( Throwable cause )
        {
        super( cause ) ;

        }   // end 'simple wrapper' constructor without message


    /**
     * @param message descriptive message related to the {@code cause}
     * @param cause the 'wrapped' exception
     */
    public TestingException( String message, Throwable cause )
        {
        super( message, cause ) ;

        }   // end 'wrapper' constructor with descriptive message

    }   // end class TestingException