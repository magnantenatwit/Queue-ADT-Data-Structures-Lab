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

import org.junit.jupiter.api.TestInfo ;

import java.io.PrintStream ;
import java.time.Duration ;
import java.util.ArrayList ;
import java.util.List ;

import static edu.wit.dcsn.dmr.testing.junit.TestData.* ;


/**
 * Base class for unit testing
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
 *     JUnit-specific support - this class implements the former.
 */
public class TestingBase
	{
	
	/** default display flag to indicate stub behavior */
	protected final static String DEFAULT_STUB_BEHAVIOR_INDICATOR =	" s" ;
	
	
	/**
	 * Handle stub behavior using default indicator
	 * 
	 * @param isStubBehavior
	 *        flag to indicate that the result of testing this dataset matches
	 */
	protected void determineStubBehavior( boolean isStubBehavior )
    	{
		determineStubBehavior( isStubBehavior,
		                       DEFAULT_STUB_BEHAVIOR_INDICATOR ) ;
    	
    	}	// end determineStubBehavior()
	
	
	/**
	 * Handle stub behavior indicator
	 * 
	 * @param isStubBehavior
	 *        flag to indicate that the result of testing this dataset matches
	 * @param stubBehaviorIndicator
	 *        text to flag that the current test data matches the expected
	 *        behavior from stubbed methods
	 */
	protected void determineStubBehavior( boolean isStubBehavior,
	                                      String stubBehaviorIndicator )
    	{
		// if the expected behavior of this test will match the stub behavior,
		// count it
    	if ( isStubBehavior )
    		{
    		this.stubBehaviorSeenCount++ ;
    		
    		// and set the tag
    		this.stubBehaviorTag =			stubBehaviorIndicator ;
    		}
    	
    	}	// end determineStubBehavior()
	
	
	/**
	 * Utility to parse a string of arguments into an array of corresponding
	 * entries - for parameterized tests
	 * 
	 * @param arguments
	 *        the string to parse
	 * @return an array containing Longs or Strings or a zero-length array of
	 *         Objects representing the entries in arguments or null if
	 *         arguments is null
	 */
	protected static Object[] parseArguments( String arguments )
    	{
    	// convert the arguments string to an array of its component entries
       	List<Object> parsedArguments =		null ;
       	
       	// parse the parameter if it's not null
       	if ( arguments != null )
       		{
       		arguments = arguments.trim() ;
       		
       		if ( arguments.equals( "null" ) )
       		    {
       		    return null ;
       		    }
       		
       		parsedArguments =				new ArrayList<>() ;
       		
       		String[] collectionContentsStrings ;
       		
       		if ( arguments.length() == 0 )
       			{
       			collectionContentsStrings =	new String[ 0 ] ;
       			}
       		else
       			{
	       		collectionContentsStrings =	arguments.split( "[|]" ) ;
       			}
		
   			// trim the strings
			for ( int i = 0; i < collectionContentsStrings.length; i++ )
				{
				collectionContentsStrings[ i ] =
										collectionContentsStrings[ i ].trim() ;
				
				if ( collectionContentsStrings[ i ].length() == 0 )
				    {
				    collectionContentsStrings[ i ] = "" ;
				    }
				}

			
   			// convert the elements to an appropriate type
   			for ( int i = 0; i < collectionContentsStrings.length; i++ )
   				{
   				// check for a 0-length string
   				if ( collectionContentsStrings[ i ].length() == 0 )
   					{
   					parsedArguments.add( "" ) ;
   					}
   				// check for an explicit null (case sensitive)
   				else if ( collectionContentsStrings[ i ].equals( "null" ) )
   					{
   					parsedArguments.add( null ) ;
   					}
       			// try to convert to integers (long actually)
   				else if ( Character.isDigit( collectionContentsStrings[ i ].charAt( 0 ) ) ||
   						  ( ( collectionContentsStrings[ i ].length() >= 2 ) &&
   							( collectionContentsStrings[ i ].charAt( 0 ) == '-' ) &&
   							Character.isDigit( collectionContentsStrings[ i ].charAt( 1 ) ) )
   							) 
       				{
               		parsedArguments.add( Long.parseLong( collectionContentsStrings[ i ] ) ) ;
       				}
   				// see if we want a range of numbers
	   			else if ( collectionContentsStrings[ i ].charAt( 0 ) == '[' )
       				{	// add elements leftBound..rightBound by step
					String[] parts =	collectionContentsStrings[ i ]
											.substring( 1,
	                                                    collectionContentsStrings[ i ]
                                            				.length() - 1 )
	                                        .split( "[:]" ) ;

           			int leftBound =     Integer.parseInt( parts[ 0 ] ) ;

           			int rightBound =	Integer.parseInt( parts[ 1 ] ) ;

           			int step =			parts.length > 2
		                                    ? parts[ 2 ].length() == 0
		                                        ? 1
	                                            : Integer.parseInt( parts[ 2 ] )
	                                        : leftBound <= rightBound
	                                            ? 1
                                                : -1 ;

                    int duplicates =    parts.length > 3
                                            ? parts[ 3 ].length() == 0
                                                ? 1
                                                : Integer.parseInt( parts[ 3 ] )
                                            : 1 ;

                    int groups =        parts.length > 4
                                            ? parts[ 4 ].length() == 0
                                                ? 1
                                                : Integer.parseInt( parts[ 4 ] )
                                            : 1 ;

                    // populate the list
                    for ( int groupI = 1; groupI <= groups; groupI++ )
                        {
        				for ( long rangeI = leftBound ;
        				      step > 0
        		                ? rangeI <= rightBound
        		                : rangeI >= rightBound ;
        				      rangeI += step )
               				{
               				for ( int duplicateI = 1; duplicateI <= duplicates; duplicateI++ )
               				    {
               				    parsedArguments.add( rangeI ) ;
               				    }
               				}
                        }
       				}
                // see if we want an individual character
                else if ( ( collectionContentsStrings[ i ].length() == 3 ) &&
                          ( collectionContentsStrings[ i ].charAt( 0 ) == '\'' ) &&
                          ( collectionContentsStrings[ i ].charAt( 2 ) == '\'' ) )
                    {
                    parsedArguments.add( collectionContentsStrings[ i ].charAt( 1 ) ) ;
                    }
                // see if we want a boolean
                else if ( ( collectionContentsStrings[ i ].equals( "true" ) ) ||
                          ( collectionContentsStrings[ i ].equals( "false" ) ) )
                    {
                    parsedArguments.add( Boolean.parseBoolean( collectionContentsStrings[ i ] ) ) ;
                    }
   				// everything else we leave as a string
	   			else
	   				{
	   				parsedArguments.add( collectionContentsStrings[ i ] ) ;
	   				}
       			}	// end for parse each element
       		
       		}	// end arguments isn't null
       		
		// assertion: parsedArguments is either null or points to an array of
		// Longs, Strings, Characters, Booleans, nulls - may be a zero-length array
       	
    	return parsedArguments == null
				? null
				: parsedArguments.toArray() ;
    	
    	}	// end parseArguments
	
	
	/**
     * Utility to pre-process test parameters - pass-through to method that takes
     * argument labels - for backward compatibility.
     * 
     * @param testInfo
     *        info about the test
     * @param isLastTest
     *        flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *        flag to indicate that the result of testing this dataset matches
     *        the stubbed behavior
     * @param collectionContentsArguments
     *        contents of one or more collections to populate
     * @return the parsed collectionContentsArguments in order of appearance in the
     *         argument list
     */
    protected Object[][] startTest( TestInfo testInfo,
                                    boolean isLastTest,
                                    boolean isStubBehavior,
                                    String... collectionContentsArguments )
        {
        return startTest( testInfo, isLastTest, isStubBehavior, null, collectionContentsArguments ) ;
        }   // end startTest() pass-through
	
	
	/**
	 * Utility to pre-process test parameters
	 * 
	 * @param testInfo
	 *        info about the test
	 * @param isLastTest
	 *        flag to indicate that this is the last dataset for this test
	 * @param isStubBehavior
	 *        flag to indicate that the result of testing this dataset matches
	 *        the stubbed behavior
	 * @param argumentLabels
	 *        descriptive text for each of the collectionContentArguments elements
	 * @param collectionContentsArguments
	 *        contents of one or more collections to populate
	 * @return the parsed collectionContentsArguments in order of appearance in the
	 *         argument list
	 */
	protected Object[][] startTest( TestInfo testInfo,
	                                boolean isLastTest,
	                                boolean isStubBehavior,
	                                String[] argumentLabels,
	                                String... collectionContentsArguments )
    	{
    	this.lastTestInGroupIsRunning =	isLastTest ;
    	
		// if the expected behavior of this test will match the stub behavior,
		// count it
    	determineStubBehavior( isStubBehavior ) ;
    	
    	// create placeholder argument labels if none provided
    	if ( argumentLabels == null )
    	    {
    	    argumentLabels = new String[ collectionContentsArguments.length ] ;
    	    
    	    argumentLabels[ 0 ] = "with" ;
    	    for ( int i = 1; i < argumentLabels.length; i++ )
    	        {
    	        argumentLabels[ i ] = "and" ;
    	        }
    	    }
    	
    	// determine format for aligned argument labels
    	int longestLabelLength = 0 ;
    	for ( int i = 0; i < argumentLabels.length; i++ )
    	    {
    	    if ( argumentLabels[ i ].length() > longestLabelLength )
    	        {
    	        longestLabelLength = argumentLabels[ i ].length() ;
    	        }
    	    }
    	final String labelFormat = "\t\t%-" + longestLabelLength + "s: %s%n" ;
    	
       	// count this test
       	this.currentTestsAttempted++ ;

       	Object[][] populatedCollections =
       						new Object[ collectionContentsArguments.length ][] ;
       	
    	// convert the arguments representing the collection contents to an array
       	for ( int i = 0; i < populatedCollections.length; i++ )
           	{
           	populatedCollections[ i ] =
           					parseArguments( collectionContentsArguments[ i ] ) ;
           	}
       	

       	// display message describing this test
		writeLog( "[%,d, %,d%s] Testing: %s%n\tconfiguration:%n",
		          this.currentTestGroup,
		          this.currentTestsAttempted,
		          this.stubBehaviorTag,
		          this.currentTestGroupName ) ;
       	
       	for( int i = 0; i < populatedCollections.length; i++ )
       		{
			writeLog( labelFormat,
			          argumentLabels[ i ],
			          arrayToString( populatedCollections[ i ] ) ) ;
       		}
       	
		return populatedCollections ;
    	
    	}	// end startTest() with 0 or more collections contents
	
	
	/**
	 * Utility method to perform steps to conclude an unsuccessful test
	 */
	protected void testFailed()
    	{
    	// display message indicating unsuccessful completion
		writeLog( "[%,d, %,d%s] Test failed%n%n",
		          this.currentTestGroup,
		          this.currentTestsAttempted,
		          this.stubBehaviorTag ) ;
		
    	}	// end testFailed()
	
	
	/**
	 * Utility method to perform steps to conclude a successful test
	 */
	protected void testPassed()
    	{
    	// count this test success
    	this.currentTestsSucceeded++ ;
    	
    	// display message indicating successful completion
		writeLog( "[%,d, %,d%s] Test passed%n%n",
		          this.currentTestGroup,
		          this.currentTestsAttempted,
		          this.stubBehaviorTag ) ;
		
    	}	// end testPassed()

	
	// --------------------------------------------------
	//
	// The next section contains testing infrastructure declarations 
	// and code
	//
	// --------------------------------------------------
	
	/*
	 * State instance variables and support methods follow:
	 */

	/*
	 * test constants, counters and labels
	 */
    /** default timeout: 2 second */
	protected static final long TEST_TIME_LIMIT_DEFAULT_SECONDS =	2 ;
	/** default timeout: 2 seconds */
	protected static final Duration TEST_TIME_LIMIT_DEFAULT =
						Duration.ofSeconds( TEST_TIME_LIMIT_DEFAULT_SECONDS ) ;
	
	/** effectively disable timeout for debugging */
	protected static final long TEST_TIME_LIMIT_DEBUG_SECONDS =
												Integer.MAX_VALUE ;
	/** effectively disable timeout for debugging */
	protected static final Duration TEST_TIME_LIMIT_DEBUG =
						Duration.ofSeconds( TEST_TIME_LIMIT_DEBUG_SECONDS ) ;
	
	/** current timeout duration */
	protected static Duration testTimeLimit =		TEST_TIME_LIMIT_DEFAULT ;
	
	// overall totals
	
	/** total number of tests attempted */
	protected int totalTestsAttempted ;
	/** total number of tests that completed successfully */
	protected int totalTestsSucceeded ;
	
	/** accumulates test results for summary display once all tests finish */
	protected List<String> summaryTestResults ;
	
	// current test group (method)
	
	/** current test group (method) counter */
	protected int currentTestGroup ;
	/** current test group (method) name */
	protected String currentTestGroupName ;

	/** flag that the last test in a test group is executing */
	protected boolean lastTestInGroupIsRunning ;
	
	/** number of tests attempted in current test group (method) */
	protected int currentTestsAttempted ;
	/** number of tests that completed successfully in current test group 
	 	(method) */
	protected int currentTestsSucceeded ;
	
	/** flag that the currently executing test completed successfully */
	protected boolean currentTestPassed ;
	
	/** counter for the number of tests within a test group that match the
	 	expected stubbed method behavior */
	protected int stubBehaviorSeenCount ;
	/** text appended to individual test id for tests that match the expected 
	 	stubbed method behavior */
	protected String stubBehaviorTag ;
	
	
	/** for test 'full' logging */
	protected static PrintStream detailedLogStream ;

	
	/** saves the active security manager when testing starts */ 
	protected SecurityManager savedSecurityManager ;
	
	
	/**
	 * Disable debugging mode by enabling test timeouts
	 * 
	 * @return true if debugging was previously enabled, false otherwise
	 */
	public static boolean disableDebugging()
		{
		return setDebug( false ) ;
		
		}	// end disableDebugging()


	/**
	 * Enable debugging mode by suppressing test timeouts
	 * 
	 * @return true if debugging was previously enabled, false otherwise
	 */
	public static boolean enableDebugging()
		{
		return setDebug( true ) ;
		
		}	// end enableDebugging()


	/**
	 * Enable or disable debugging by adjusting test timeouts.
	 * 
	 * @param wantToDebug
	 *        true disables test timeouts; false (default) enables timeouts.
	 * @return true if debugging was previously enabled, false otherwise
	 */
	protected static boolean setDebug( boolean wantToDebug )
		{
		boolean wasDebugging =	testTimeLimit == TEST_TIME_LIMIT_DEBUG ;

		testTimeLimit =				wantToDebug
    		                            ? TEST_TIME_LIMIT_DEBUG
    		                            : TEST_TIME_LIMIT_DEFAULT ;

		if ( wantToDebug )
			{
			writeSyserr( "\n\n----------\n\n%s: %s\n\n----------\n\n",
			             "WARNING",
			             "Debugging mode enabled\n\tMust submit with debugging mode disabled!" ) ;
			}

		return wasDebugging ;

		}	// end setDebug()
	
	
	/**
	 * Display a log message to the console and detailed log file
	 * 
	 * @param format
	 *        to {@code printf()}
	 * @param parameters
	 *        to {@code printf()}
	 */
	public static void writeConsole( String format, Object... parameters )
    	{
    	System.out.printf( format, parameters ) ;
    	
    	if ( detailedLogStream != System.out )
        	{
        	writeLog( format, parameters ) ;
        	}
    	
    	}	// end writeConsole()
	
	
	/**
	 * Display a log message to the detailed log file
	 * 
	 * @param format
	 *        to {@code printf()}
	 * @param parameters
	 *        to {@code printf()}
	 */
	public static void writeLog( String format, Object... parameters )
    	{
    	detailedLogStream.printf( format, parameters ) ;
    	
    	}	// end writeLog()
	
	
	/**
	 * Print a formatted message to System.err in its proper sequence wrt
	 * System.out - limited effectiveness when running multiple threads
	 * 
	 * @param format
	 *        to {@code printf()}
	 * @param parameters
	 *        to {@code printf()}
	 */
	public static void writeSyserr( String format, Object... parameters )
    	{
    	System.out.flush() ;
    	System.err.printf( format, parameters ) ;
    	System.err.flush() ;
    	
    	}	// end writeSyserr()
	
	}	// end class TestingBase
