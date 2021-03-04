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

package edu.wit.dcsn.dmr.testing.junit;


import org.junit.jupiter.api.AfterAll ;
import org.junit.jupiter.api.AfterEach ;
import org.junit.jupiter.api.BeforeAll ;
import org.junit.jupiter.api.BeforeEach ;
import org.junit.jupiter.api.DisplayName ;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation ;
import org.junit.jupiter.api.TestInfo ;
import org.junit.jupiter.api.TestInstance ;
import org.junit.jupiter.api.TestInstance.Lifecycle ;
import org.junit.jupiter.api.TestMethodOrder ;

import java.io.File ;
import java.io.FileNotFoundException ;
import java.io.IOException ;
import java.io.PrintStream ;
import java.nio.file.Files ;
import java.nio.file.Path ;
import java.util.Calendar ;
import java.util.LinkedList ;


/**
 * Base class for JUnit testing
 * 
 * @author David M Rosenberg
 * @version 1.0.0 2018-05-25 initial set of tests<br>
 * @version 1.1.0 2018-06-09 revise structure to use TestInfo instead of certain
 *     hard-coded text
 * @version 1.2.0 2018-09-02 add timeouts
 * @version 1.3.0 2019-01-14 more implementation
 * @version 1.3.1 2019-01-17 cosmetic changes
 * @version 2.0.0 2019-05-12
 *     <ul>
 *     <li>restructure tests
 *     <li>disable System.exit() during testing
 *     <li>start making each subtest independent so they'll all run even if one fails
 *     </ul>
 * @version 2.1.0 2019-05-17
 *     <ul>
 *     <li>rename class
 *     <li>remove unnecessary throws clauses from @BeforeXxx and @AfterXxx
 *     <li>more fully utilize JUnit 5.4 features
 *     <li>switch tests to data-driven
 *     </ul>
 * @version 3.0.0 2019-06-27
 *     <ul>
 *     <li>complete re-write with reusable testing infrastructure
 *     <li>tests are now data-driven
 *     <li>add summary test results
 *     </ul>
 * @version 3.1.0 2019-06-28 move detailed activity to log file
 * @version 4.0.0 2019-07-04 split general purpose utilities methods into separate
 *     class
 * @version 5.0.0 2019-10-07 revise for Stack ADT
 * @version 5.1.0 2020-01-26 cleanup toward DRCo coding standard compliance
 * @version 5.2.0 2020-05-14
 *     <ul>
 *     <li>cleanup comments
 *     <li>enhance null argument handling
 *     <li>in parseArguments(): correct numeric range bounds parsing; add support for
 *     data-supplied step, repeating group count, duplicate count
 *     <li>output formatting adjustments to improve alignment and readability
 *     <li>add PlaceholderException to support specific Exception detection
 *     <li>add support for detection and display of boolean/Boolean and
 *     char/Character types
 *     </ul>
 * @version 5.3.0 2020-06-03 add startTest() pass-through for backward compatibility
 * @version 5.4.0 2020-07-22
 *     <ul>
 *     <li>add instance field retrieval and modification methods
 *     <li>add method invocation support
 *     </ul>
 * @version 5.5.0 2020-08-07
 *     <ul>
 *     <li>consolidate/simplify field access methods
 *     <li>add collection retrieval methods
 *     </ul>
 * @version 6.0.0 2020-09-13 Split into general unit testing support and
 * J   Unit-specific support - this class implements the former.
 */
@DisplayName( "JUnit Testing Base" )
@TestInstance( Lifecycle.PER_CLASS )
@TestMethodOrder( OrderAnnotation.class )
public class JUnitTestingBase extends TestingBase
	{

	/**
	 * @param testInfo
	 *        the current test environment
	 */
	@BeforeAll
	protected void setUpBeforeClass( TestInfo testInfo )
		{
//		enableDebugging() ;	// DEBUG
		
		// by default, send detailed log entries to the standard output
		detailedLogStream =				System.out ;

		// create the detailed log - name is TestClass-yyyy-mm-dd-hhmmss.log
		Calendar now =					Calendar.getInstance() ;
        String timestamp =				String.format( "%TF-%<TH%<TM%<TS",
                          				               now
                          				               ) ;

        Path testLogsPath =		new File( "./test-logs" ).toPath().toAbsolutePath().normalize() ;
		
        String outputFilename =	String.format( "%s%c%s-%s.log",
                               	               testLogsPath,
                               	               File.separatorChar,
                               	               this.getClass().getSimpleName(),
                               	               timestamp ) ;
		
		try
			{
			testLogsPath =
				Files.createDirectories( testLogsPath ) ;
			
			detailedLogStream =			new PrintStream( outputFilename ) ;
			writeConsole( "Detailed log in: %s%n%n",
			              outputFilename ) ;
			}
		catch ( FileNotFoundException e )
			{
			writeSyserr( "Unable to create log file: %s%n\t%s%n\tusing System.out%n",
			             e.getMessage(),
			             outputFilename );
			}
		catch ( IOException e )
			{
			writeSyserr( "Unable to create log folder: %s%n\t%s%n\tusing System.out%n",
			             e.getMessage(),
			             testLogsPath );
			}

		
		// display start of testing (class)
		writeConsole( "     Starting tests of class %s%n",
		              testInfo.getDisplayName() ) ;
		
		// initialize testing-wide counters
		this.totalTestsAttempted =				0 ;
		this.totalTestsSucceeded =				0 ;
		
		this.summaryTestResults =				new LinkedList<>() ;
		
		this.currentTestGroup =					0 ;
		this.currentTestGroupName =				"" ;

		// assume single tests (not repeating nor parameterized)
		this.lastTestInGroupIsRunning =			true ;
		
		// there is no current test - indicate didn't pass
		this.currentTestPassed =					false ;

		// there are no stub values seen yet
		this.stubBehaviorSeenCount =				0 ;
		
		/*
		 * prevent System.exit() from terminating tests
		 */
		// save the current security manager
		this.savedSecurityManager =				System.getSecurityManager() ;
		
		// enable ours
        System.setSecurityManager( new NoExitSecurityManager() ) ;
        
		}	// end setUpBeforeClass()


	/**
	 * @param testInfo
	 *        the current test environment
	 */
	@AfterAll
	protected void tearDownAfterClass( TestInfo testInfo )
		{
		// display summary results
		if ( this.totalTestsAttempted > 0 )
			{
			writeConsole( "%n     Summary Test Results%n%n" ) ;
			
			for ( String testResult : this.summaryTestResults )
				{
				writeConsole( "%s%n",
				              testResult ) ;
				}
			
			writeConsole( "%n     Successfully completed %,3d of %,3d tests (%3d%%) attempted for class %s%n",
			              this.totalTestsSucceeded,
			              this.totalTestsAttempted,
			              ( this.totalTestsSucceeded * 100 ) / this.totalTestsAttempted,
			              testInfo.getDisplayName() ) ;
			}
		else
			{
			writeConsole( "%n     No tests attempted for class %s%n",
			              testInfo.getDisplayName() ) ;
			}
		
		
		// close the detailed log
		detailedLogStream.close() ;
		

		/*
		 * re-enable System.exit()
		 */
		// restore the saved security manager
		System.setSecurityManager( this.savedSecurityManager ) ;
        
		}	// end tearDownAfterClass()


	/**
	 * @param testInfo
	 *        the current test environment
	 */
	@BeforeEach
	protected void setUpBeforeEachTest( TestInfo testInfo )
		{
		String baseName =				testInfo.getDisplayName() ;
		int colonColonIndex =			baseName.indexOf( "::" ) ;
		if ( colonColonIndex != -1 )
			{
			baseName =		baseName.substring( 0, colonColonIndex ).trim() ;
			}
		
		this.stubBehaviorTag =				"" ;	// assume not a stub behavior
		
		if ( ! this.currentTestGroupName.equals( baseName ) )
			{
			// count this test group
    		this.currentTestGroup++ ;
    		this.currentTestGroupName =		baseName ;
    		
    		// reset current test counters
    		this.currentTestsAttempted =		0 ;
    		this.currentTestsSucceeded =		0 ;

    		// assume single test (not repeating nor parameterized)
    		this.lastTestInGroupIsRunning =	true ;

    		// there are no stub values seen yet
    		this.stubBehaviorSeenCount =				0 ;
    		
    		// display start of testing (method or category/group of operations)
			writeConsole( "%n[%,2d] Starting tests of %s%n%n",
			              this.currentTestGroup,
			              this.currentTestGroupName ) ;
			}
		
		// reset test passed flag
		this.currentTestPassed =				false ;
		
		}	// end setUpBeforeEachTest()


	/**
	 * @param testInfo the current test environment
	 */
	@AfterEach
	protected void tearDownAfterEachTest( TestInfo testInfo )
		{
		if ( this.currentTestPassed )
			{
			testPassed() ;
			}
		else
			{
			testFailed() ;
			}
		
		if ( this.lastTestInGroupIsRunning )
			{
    		// display stats for this test group

    		// filter for stubbed return values
    		if ( ( this.stubBehaviorSeenCount > 0 ) &&
                 ( this.currentTestsSucceeded == this.stubBehaviorSeenCount ) )
    			{
    			// only saw correct responses which matched the stub values
    			// consider this a total failure rather than a (misleading)
    			// correct percentage
				writeConsole( "[%,2d] The only tests which passed matched stub behaviors - ignoring them (%,2d)%n",
				              this.currentTestGroup,
				              this.stubBehaviorSeenCount ) ;
    			
    			this.currentTestsSucceeded =		0 ;	// clear the success count
    			}
		
    		String testSummary =
                   String.format( "[%,2d] Successfully completed %,3d of %,3d tests (%3d%%) of %s",
                                  this.currentTestGroup,
                                  this.currentTestsSucceeded,
                                  this.currentTestsAttempted,
                                  ( this.currentTestsAttempted == 0
                                       ? 0
                                       : ( this.currentTestsSucceeded *
                                           100 ) /
                                         this.currentTestsAttempted ),
                                  this.currentTestGroupName ) ;
    		this.summaryTestResults.add( testSummary ) ;
    		writeConsole( "%s%n%n----------%n", 
    		              testSummary ) ;
    		
    		// accumulate this test group's results
    		this.totalTestsAttempted +=		this.currentTestsAttempted ;
    		this.totalTestsSucceeded +=		this.currentTestsSucceeded ;
    		
    		// reset current test counters
    		this.currentTestsAttempted =		0 ;
    		this.currentTestsSucceeded =		0 ;
			}
		
		}	// end tearDownAfterEachTest()
	
	}	// end class JUnitTestingBase
