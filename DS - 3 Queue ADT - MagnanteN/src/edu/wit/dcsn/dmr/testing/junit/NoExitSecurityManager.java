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

import java.security.Permission ;

import static edu.wit.dcsn.dmr.testing.junit.JUnitTestingBase.writeSyserr ;

/**
 * Support {@code System.exit()} interception
 * 
 * @author Dave Rosenberg
 * @version 1.0.0 2020-08-08 Initial implementation
 */
public class NoExitSecurityManager extends SecurityManager
    {

    /**
     * No-arg constructor
     */
    public NoExitSecurityManager()
        {
        super() ;

        }   // end no-arg constructor


    @Override
    public void checkPermission( final Permission perm )
        {
        // noop
        }


    @Override
    public void checkPermission( final Permission perm,
                                 final Object context )
        {
        // noop
        }


    @Override
    public void checkExit( final int status )
        {
        super.checkExit( status ) ;

        writeSyserr( "%nSystem.exit( %,d ) intercepted%n%n", status ) ;

        throw new ExitException( status ) ;

        }   // end checkExit()

    }   // end class NoExitSecurityManager