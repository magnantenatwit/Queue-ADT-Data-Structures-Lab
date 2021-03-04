/* @formatter:off
 *
 * Dave Rosenberg
 * Comp 2000 - Data Structures
 * Lab: Queue ADT
 * Summer, 2020
 * 
 * Usage restrictions:
 * 
 * You may use this code for exploration, experimentation, and furthering your
 * learning for this course. You may not use this code for any other
 * assignments, in my course or elsewhere, without explicit permission, in
 * advance, from myself (and the instructor of any other course).
 * 
 * Further, you may not post or otherwise share this code with anyone other than
 * current students in my sections of this course. Violation of these usage
 * restrictions will be considered a violation of the Wentworth Institute of
 * Technology Academic Honesty Policy.
 *
 * Do not remove this notice.
 *
 * @formatter:on
 */

package edu.wit.dcsn.comp2000.queue.common ;

/**
 * Enumeration of capacities to use for sizing ArrayQueues
 *
 * @author David M Rosenberg
 * @version 1.0.0 2020-02-24 Initial implementation
 */
public enum ArrayQueueCapacity
    {

    // Capacity         Display Name    Numeric Value
    // valid sizes
    /** DEFAULT_CAPACITY */
    DEFAULT (           "Default",      3 ),
    /** small capacity */
    SMALL (             "Small",        13 ),
    /** medium capacity */
    MEDIUM (            "Medium",       13 + ( 13 / 2 ) ),
    /** large capacity */
    LARGE (             "Large",        10_000 / 2 ),
    /** MAX_CAPACITY capacity */
    MAXIMUM (           "Maximum",      10_000 ),

    // invalid sizes
    /** under minimum (DEFAULT_CAPACITY) capacity */
    UNDER_MINIMUM (     "Under minimum", 3 - 1 ),
    /** zero (0) capacity */
    ZERO (              "Zero",         0 ),
    /** negative capacity */
    NEGATIVE (          "Negative",     -1_000 ),   // arbitrary negative number
    /** over MAX_CAPACITY capacity */
    OVER_MAXIMUM (      "Over maximum", 10_000 + 1 ),
    /** way over MAX_CAPACITY capacity */
    WAY_OVER_MAXIMUM (  "Way over maximum", 10_000 + 1_000 );

    
    // instance variables
    
    /** nicely formatted name for display */
    public final String displayName ;
    /** integer specification of the capacity */
    public final int capacityValue ;

    
    // constructor
    
    /**
     * @param testCapacityName
     *     nicely formatted name for display
     * @param testCapacityValue
     *     integer equivalent of the size
     */
    private ArrayQueueCapacity( final String testCapacityName,
                                    final int testCapacityValue )
        {
        this.displayName = testCapacityName ;
        this.capacityValue = testCapacityValue ;

        } // end constructor

    
    // public methods

    /**
     * Parse a text description of capacity
     *
     * @param capacityDescription
     *     a name to parse
     * @return the corresponding enum constant or FIRM if the name is unrecognized
     */
    public static ArrayQueueCapacity
                                    interpretDescription( String capacityDescription )
        {
        ArrayQueueCapacity correspondingCapacity ;

        // convert the description to all lowercase for comparison
        capacityDescription = capacityDescription.toLowerCase() ;

        // rudimentary strategy: only look at first character(s)
        switch ( capacityDescription.charAt( 0 ) )
            {
            case 'd':
                correspondingCapacity = DEFAULT ;
                break ;

            case 's':
                correspondingCapacity = SMALL ;
                break ;

            case 'm':
                switch ( capacityDescription.charAt( 1 ) )
                    {
                    case 'a':
                        correspondingCapacity = MAXIMUM ;
                        break ;
                    case 'e':
                        correspondingCapacity = MEDIUM ;
                        break ;

                    default:
                        correspondingCapacity = null ;
                        break ;

                    }
                break ;

            case 'l':
                correspondingCapacity = LARGE ;
                break ;

            case 'z':
                correspondingCapacity = ZERO ;
                break ;

            case 'n':
                correspondingCapacity = NEGATIVE ;
                break ;

            case 'u':
                correspondingCapacity = UNDER_MINIMUM ;
                break ;

            case 'o':
                correspondingCapacity = OVER_MAXIMUM ;
                break ;

            case 'w':
                correspondingCapacity = WAY_OVER_MAXIMUM ;
                break ;

            default:
                correspondingCapacity = null ;
                break ;

            }   // end switch

        return correspondingCapacity ;

        }   // end method interpretDescription()


    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
        {
        return this.displayName ;

        }   // end method toString()


    /**
     * Test driver - displays all constants for this enumeration
     *
     * @param args
     *     -unused-
     */
    public static void main( final String[] args )
        {
        // display column headers
        System.out.printf( "%-5s %-17s %-17s %-17s %-15s %-15s%n",
                           "#",
                           "Size",
                           "Name",
                           "Display Name",
                           "Size Value",
                           "Interpreted Size" ) ;

        // display each element of the enumeration
        for ( final ArrayQueueCapacity anItemSize : ArrayQueueCapacity.values() )
            {
            System.out.printf( "%-5d %-17s %-17s %-17s %,9d       %-15s%n",
                               anItemSize.ordinal(),
                               anItemSize,
                               anItemSize.name(),
                               anItemSize.displayName,
                               anItemSize.capacityValue,
                               interpretDescription( anItemSize.toString() ) ) ;
            }   // end for

        }   // end main()

    }	// end enum ArrayQueueTestCapacity