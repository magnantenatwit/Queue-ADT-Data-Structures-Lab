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


import static org.junit.jupiter.api.Assertions.assertArrayEquals ;
import static org.junit.jupiter.api.Assertions.assertEquals ;
import static org.junit.jupiter.api.Assertions.fail ;

import org.junit.jupiter.api.DisplayName ;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation ;
import org.junit.jupiter.api.TestInstance ;
import org.junit.jupiter.api.TestInstance.Lifecycle ;
import org.junit.jupiter.api.TestMethodOrder ;

import static edu.wit.dcsn.dmr.testing.junit.Reflection.* ;

import java.lang.reflect.Array ;
import java.util.Arrays ;


/**
 * Base class for JUnit testing.
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
 *     <li>enhance itemToString()
 *     <li>add itemToStringWithType()
 *     </ul>
 */
@DisplayName( "Test Data" )
@TestInstance( Lifecycle.PER_CLASS )
@TestMethodOrder( OrderAnnotation.class )
public class TestData
	{
	// --------------------------------------------------
	//
	// The following utilities are primarily used by the test methods
	//
	// --------------------------------------------------
	
	
	/*
	 * constants for use with arrayToString()
	 */
	/** default maximum length (in characters) a string version of an array can 
	 * take before the string is truncated in the middle */
	public final static int DEFAULT_ARRAY_TO_STRING_LENGTH =	200 ;
	/** the default number of elements arrayToString() will include in a 
	 * truncated string */
	public final static int DEFAULT_ARRAY_TO_STRING_ELEMENTS =	50 ;
	

	/**
	 * Return a limited portion of a string representation of an array
	 * 
	 * @param anArray
	 *        the array to convert to string
	 * @return the result of
	 *         {@code arrayToString( anArray, DEFAULT_ARRAY_TO_STRING_LENGTH )}
	 */
	public static String arrayToString( Object[] anArray )
    	{
    	return arrayToString( anArray, 
    	                      DEFAULT_ARRAY_TO_STRING_LENGTH,
    	                      DEFAULT_ARRAY_TO_STRING_ELEMENTS ) ;
    	}	// end 1-arg arrayToString()
	
	
	/**
	 * Return a limited portion of a string representation of an array
	 * 
	 * @param anArray
	 *        the array to convert to string
	 * @param maximumLength
	 *        the maximum number of characters to return
	 * @param maximumElements
	 *        the maximum number of elements to return
	 * @return if the full string representation has no more than maximumLength
	 *         characters, the entire string; otherwise the first maximumElements
	 *         / 2 elements followed by " ... " then the last maximumElements / 2
	 *         elements
	 */
	public static String arrayToString( Object[] anArray,
                                           int maximumLength,
                                           int maximumElements )
    	{
    	String fullString =		arrayToFullString( anArray ) ;
    	if ( ( fullString == null ) || ( fullString.length() <= maximumLength ) )
    		{
    		return fullString ;
    		}
    	
    	int halfCount =			maximumElements / 2 ;
    	StringBuilder partsString =	new StringBuilder( "[" ) ;
    	
    	for ( int i = 0; i < halfCount; i++ )
    		{
    		if ( anArray[ i ] == null )
    		    {
    		    partsString.append( "null" ) ;
    		    }
    		else
    		    {
    		    partsString.append( anArray[ i ].toString() ) ;
    		    }
    		
		    partsString.append( ", " ) ;
    		}
    	
    	partsString.append( "..." ) ;
    	
    	for ( int i = anArray.length - halfCount; i < anArray.length; i++ )
    		{
		    partsString.append( ", " ) ;
		    
            if ( anArray[ i ] == null )
                {
                partsString.append( "null" ) ;
                }
            else
                {
                partsString.append( anArray[ i ].toString() ) ;
                }
            }
    	
    	partsString.append( "]" ) ;
    	
    	return partsString.toString() ;
    	
    	}	// end 3-arg arrayToString()
    
    
    /**
     * Create a String representation of an array similar to
     * {@code Arrays.toString()} but with items appropriately delimited (e.g. Strings
     * appear within "")
     *
     * @param anArray
     *     the items to include in the resulting String
     * @return a String representing the appropriately delimited elements of
     *     {@code anArray}
     */
	public static String arrayToFullString( Object[] anArray )
        {
        if ( anArray == null )
            {
            return null ;
            }
        
        StringBuilder fullString = new StringBuilder( "[" ) ;
        
        String delimiter = "" ;
        
        for ( Object anItem : anArray )
            {
            fullString.append( delimiter ) ;
            fullString.append( itemToString( anItem ) ) ;
            
            delimiter = ", " ;
            }
        
        fullString.append( "]" ) ;
        
        return fullString.toString() ;
        
        }   // end arrayToFullString()
    
    
    /**
     * Convert an {@code item} to its String representation, delimited appropriately
     * prefixed with the (component) type
     *
     * @param item
     *     the object to convert
     * @return if {@code item} is null, "null" without delimiters, otherwise, the
     *     appropriately delimited object's String representation
     */
    public static String itemToStringWithType( Object item )
        {

        return item == null
                    ? null
                    : ( isArray( item.getClass() )
                            ? item.getClass().getComponentType().getSimpleName()
                            : item.getClass().getSimpleName() )
                        + " " + itemToString( item ) ;
        
        }  // end itemToStringWithType()
    
    
    /**
     * Convert an {@code item} to its String representation, delimited appropriately
     *
     * @param item
     *     the object to convert
     * @return if {@code item} is null, "null" without delimiters, otherwise, the
     *     appropriately delimited object's String representation
     */
	public static String itemToString( Object item )
        {
        if ( item == null )
            {
            return "null" ;
            }
        
        if ( isArray( item.getClass() ) )
            {
            int itemCount = Array.getLength( item ) ;
            Object[] items = new Object[ itemCount ] ;
            
            for ( int i = 0; i < itemCount; i++ )
                {
                items[ i ] = Array.get( item, i ) ;
                }
            
            return arrayToString( items ) ;
            
            }
        
        final String stringDelimiter = "\"" ;
        final String charDelimiter = "'" ;
        String useDelimiter = "" ;
        
        if ( item instanceof String )
            {
            useDelimiter = stringDelimiter ;
            }
        else if ( item instanceof Character )
            {
            useDelimiter = charDelimiter ;
            }
        
        return useDelimiter + item.toString() + useDelimiter ;
        
        }   // end itemToString()
	
	
	/*
	 * constants for use with compareArrays()
	 */
	/** flag that dataset is ordered and may not be reordered for comparison */
	public final static boolean IS_ORDERED =	true ;
	/** flag that dataset is unordered and may be reordered for comparison */
	public final static boolean IS_UNORDERED =	false ;
	
	/**
	 * Determine if two arrays contain the same contents
	 * 
	 * @param expected
	 *        the array of elements as they should appear
	 * @param actual
	 *        the array of elements to be verified against expected
	 * @param ordered
	 *        if true, elements of expected and actual must appear in the same
	 *        order; if false, the contents may appear in any order
	 */
	@SuppressWarnings( "null" )			// compiler bug? - NPE not possible
    public static void compareArrays( Object[] expected,
                                      Object[] actual,
                                      boolean ordered )
    	{
		// if both array references are null, succeed
    	if ( ( expected == null ) && ( actual == null ) )		// both null
    		{
    		return ;
    		}
    	
		// if one array reference is null and the other is empty, fail
    	if ( ( ( expected == null ) && ( actual.length == 0 ) ) ||
    								// expected is null, actual is empty
			 ( ( actual == null ) && ( expected.length == 0 ) ) )
    								// actual is null, expected is empty
    		{
    		fail( "Trying to compare null to empty []" ) ;
    		}
    	
    	// if either reference is null, the other can't be null
    	else if ( ( expected == null ) || ( actual == null ) )
    		{
			fail( "bad test data detected: one dataset is null and the other is neither null nor empty" ) ;
			
			// unreachable
//			throw new IllegalStateException( "bad test data detected: one dataset is null and the other is neither null nor empty" ) ;
    		}

    	// make sure the two arrays are the same length
    	assertEquals( expected.length,
    	              actual.length ) ;
    	
		// make copies of the arrays so we don't affect the contents/order of
		// the originals
    	Object[] workingExpected =		Arrays.copyOf( expected,
    	                          		               expected.length ) ;
    	Object[] workingActual =		Arrays.copyOf( actual,
    	                        		               actual.length ) ;
    	
    	// if the order of the contents of the arrays isn't ordered, sort them
    	if ( ! ordered )
    		{
    		Arrays.sort( workingExpected ) ;
    		Arrays.sort( workingActual ) ;
    		}
    	
    	// compare the contents of the arrays
    	assertArrayEquals( workingExpected, 
    	                   workingActual ) ;
    	
    	}  // end compareArrays()
	
	
    /**
     * Count the number of occurrences of a value in an array
     * 
     * @param values
     *     the collection of values
     * @param testValue
     *     the test value
     * @return the number of time testValue occurred in values
     */
    public static int countOccurrences( Object[] values,
                                        Object testValue )
        {
        int occurrences = 0 ;
        for ( int i = 0 ; i < values.length ; i++ )
            {
            if ( values[ i ].equals( testValue ) )
                {
                occurrences++ ;
                }
            }

        return occurrences ;

        }   // end countOccurrences()
	
	
	/**
	 * Determine if {@code testValue} occurs at least once in an array
	 * 
	 * @param values
	 *        the collection of values
	 * @param testValue
	 *        the test value
	 * @return true if at least one occurrence; false if no occurrences
	 */
    public static boolean arrayContains( Object[] values,
                                         Object testValue )
    	{
    	boolean found =		false ;	// we haven't found it yet
    	
    	if ( values != null )
    	    {
        	for ( int i = 0; i < values.length; i++ )
        		{
        		if ( values[ i ] == null )
        		    {
        		    found =       testValue == null ;
        		    }
        		else
            		{
            		found =       values[ i ].equals( testValue ) ;
            		}
        		
        		if ( found )
        			{
        			break ;
        			}
        		}
    	    }
    	
    	return found ;
    	
    	}	// end arrayContains()
	
	}	// end class TestData
