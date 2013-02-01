// Convert the old Quake Live mouse sensitivity values to the new
// system based on mouse hardware CPI
//
// http://www.quakelive.com/forum/showthread.php?15458-New-Mouse-Sensitivty-and-Mouse-Accel-Features

import java.lang.Math;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;

public class MouseCalc {

  interface Constants {
    // id's upstream default variable settings
    public static final int    DEFAULT_M_CPI               = 0;
    public static final double DEFAULT_SENSITIVITY         = 5;
    public static final double DEFAULT_M_YAW               = 0.022;
    public static final double DEFAULT_M_PITCH             = 0.022;
    public static final double DEFAULT_M_FORWARD           = 0.25;
    public static final double DEFAULT_M_SIDE              = 0.25;
    public static final double DEFAULT_CL_MOUSESENSCAP     = 0;
    public static final double DEFAULT_CL_MOUSEACCEL       = 0;
    public static final double DEFAULT_CL_MOUSEACCELPOWER  = 2;
    public static final double DEFAULT_CL_MOUSEACCELOFFSET = 0;
    public static final double NEW_M_YAW                   = 0.022;
  }

  public static void main(String[] args) {

    //
    // Set default values
    //

    // My defaults
    int    m_cpi               = 800;
    double old_sensitivity     =   5.000;
    double old_m_yaw           =   0.650;
    double old_m_pitch         =  -0.100;
    double old_cl_mouseSensCap =   0.000;
    double old_cl_mouseAccel   =   0.000;
    double old_m_forward       =   0.250;
    double old_m_side          =   0.250;

    /*
    // id's defaults (plus m_cpi of 800)
    int    m_cpi               = 800;
    double old_sensitivity     = Constants.DEFAULT_SENSITIVITY;
    double old_m_yaw           = Constants.DEFAULT_M_YAW;
    double old_m_pitch         = Constants.DEFAULT_M_PITCH;
    double old_cl_mouseSensCap = Constants.DEFAULT_CL_MOUSESENSCAP;
    double old_cl_mouseAccel   = Constants.DEFAULT_CL_MOUSEACCEL;
    double old_m_forward       = Constants.DEFAULT_M_FORWARD;
    double old_m_side          = Constants.DEFAULT_M_SIDE;
    */

    //
    // Read command-line arguments
    //

    // create the command line parser
    CommandLineParser parser = new PosixParser();

    // create the Options
    Options options = new Options();

    // Boolean options
    options.addOption( "h", "help",  false, "Print help message" );
    options.addOption( "q", "quiet", false, "Minimal output"     );

    // Options with arguments
    options.addOption( OptionBuilder.withLongOpt("sensitivity")     .withDescription( "Current value of sensitivity" )     .hasArg() .withArgName("value") .create("s") );
    options.addOption( OptionBuilder.withLongOpt("m-yaw")           .withDescription( "Current value of m_yaw" )           .hasArg() .withArgName("value") .create("y") );
    options.addOption( OptionBuilder.withLongOpt("m-pitch")         .withDescription( "Current value of m_pitch" )         .hasArg() .withArgName("value") .create("p") );
    options.addOption( OptionBuilder.withLongOpt("cl-mousesenscap") .withDescription( "Current value of cl_mouseSensCap" ) .hasArg() .withArgName("value") .create("c") );
    options.addOption( OptionBuilder.withLongOpt("cl-mouseaccel")   .withDescription( "Current value of cl_mouseAccel" )   .hasArg() .withArgName("value") .create("a") );

    try {
      CommandLine line = parser.parse( options, args );

      if ( line.hasOption("sensitivity") || line.hasOption("s") ) {
        System.out.format("Using provided sensitivity     : %s\n", line.getOptionValue("sensitivity"));
        old_sensitivity = Float.parseFloat(line.getOptionValue("sensitivity"));
      } else {
        System.out.format("Using default sensitivity      : %.3f\n", old_sensitivity);
      }

      if ( line.hasOption("m-yaw") || line.hasOption("y") ) {
        System.out.format("Using provided m_yaw           : %s\n", line.getOptionValue("m-yaw"));
        old_m_yaw = Float.parseFloat(line.getOptionValue("m-yaw"));
      } else {
        System.out.format("Using default m_yaw            : %.3f\n", old_m_yaw);
      }

      if ( line.hasOption("m-pitch") || line.hasOption("p") ) {
        System.out.format("Using provided m_pitch         : %s\n", line.getOptionValue("m-pitch"));
        old_m_pitch = Float.parseFloat(line.getOptionValue("m-pitch"));
      } else {
        System.out.format("Using default m_pitch          : %.3f\n", old_m_pitch);
      }

      if ( line.hasOption("cl-mousesenscap") || line.hasOption("c") ) {
        System.out.format("Using provided cl_mouseSensCap : %s\n", line.getOptionValue("cl-mousesenscap"));
        old_cl_mouseSensCap = Float.parseFloat(line.getOptionValue("cl-mousesenscap"));
      } else {
        System.out.format("Using default cl_mouseSensCap  : %.3f\n", old_cl_mouseSensCap);
      }

      if ( line.hasOption("cl-mouseaccel") || line.hasOption("a") ) {
        System.out.format("Using provided cl_mouseAccel   : %s\n", line.getOptionValue("cl-mouseaccel"));
        old_cl_mouseAccel = Float.parseFloat(line.getOptionValue("cl-mouseaccel"));
      } else {
        System.out.format("Using default cl_mouseAccel    : %.3f\n", old_cl_mouseAccel);
      }

      if ( line.getArgs().length > 0 ) {
        m_cpi = Integer.parseInt(line.getArgs()[0]);
        System.out.format("Using provided m_cpi           : %d\n", m_cpi);
      } else {
        System.out.println("m_cpi not provided, cannot continue");
        System.out.println();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("MouseCalc", options, true);

        System.err.println();
        System.err.println("Provide your mouse hardware's CPI (aka DPI) as m_cpi, and copy the rest of the");
        System.err.println("values from your current configuration.  If you're not sure what your current");
        System.err.println("settings are, you can use \"\\set <variable>\" in the console to see what the");
        System.err.println("engine is using at the moment.");

        System.exit(1);
      }

      System.out.println();
    }
    catch(ParseException exception) {
      System.err.println(exception.getMessage());
      System.err.println();
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("MouseCalc", options, true);

      System.err.println("Provide your mouse hardware's CPI (aka DPI) as m_cpi, and copy the rest of the");
      System.err.println("values from your current configuration.  If you're not sure what your current");
      System.err.println("settings are, you can use \"\\set <variable>\" in the console to see what the");
      System.err.println("engine is using at the moment.");
    }

    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////

    // Calculate new values
    // sensitivity=`echo "scale=3; ${old_sensitivity} * ( ${old_m_yaw} * ${m_cpi} / 2.54 )" | bc`
    // cl_mouseSensCap=`echo "scale=3; ${old_cl_mouseSensCap} * ( ${old_m_yaw} * ${m_cpi} / 2.54 )" | bc`
    // cl_mouseAccel=`echo "scale=3; ${old_cl_mouseAccel} * ( ${old_m_yaw} * ( ${m_cpi} / 2.54 ) ^ 2 ) / 1000" | bc`
    // m_pitch=`echo "scale=3; ${old_m_pitch} * ( .022 / ${old_m_yaw} )" | bc`
    double new_sensitivity     = old_sensitivity * ( old_m_yaw * m_cpi / 2.54 );
    double new_m_yaw           = Constants.NEW_M_YAW;
    double new_m_pitch         = old_m_pitch * ( 0.22 / old_m_yaw );
    double new_cl_mouseSensCap = old_cl_mouseSensCap * ( old_m_yaw * m_cpi / 2.54 );
    double new_cl_mouseAccel   = old_cl_mouseAccel * ( old_m_yaw * Math.pow(( m_cpi / 2.54 ),2) ) / 1000;
    //double new_m_forward       = 0.250;
    //double new_m_side          = 0.250;

    System.err.format("Configuration   |    Old |     id |      New\n");
    System.err.format("================+========+========+==========\n");
    System.err.format("sensitivity     | %6.3f | %6.3f | %8.3f\n", old_sensitivity,     Constants.DEFAULT_SENSITIVITY,     new_sensitivity);
    System.err.format("m_yaw           | %6.3f | %6.3f | %8.3f\n", old_m_yaw,           Constants.DEFAULT_M_YAW,           new_m_yaw);
    System.err.format("m_pitch         | %6.3f | %6.3f | %8.3f\n", old_m_pitch,         Constants.DEFAULT_M_PITCH,         new_m_pitch);
    System.err.format("cl_mouseSensCap | %6.3f | %6.3f | %8.3f\n", old_cl_mouseSensCap, Constants.DEFAULT_CL_MOUSESENSCAP, new_cl_mouseSensCap);
    System.err.format("cl_mouseAccel   | %6.3f | %6.3f | %8.3f\n", old_cl_mouseAccel,   Constants.DEFAULT_CL_MOUSEACCEL,   new_cl_mouseAccel);
    System.err.format("m_cpi           | %6d | %6d | %8d\n",       m_cpi,               Constants.DEFAULT_M_CPI,           m_cpi);
    //System.err.format("\n");
    //System.err.format("m_forward       | %6.3f | %6.3f | %8.3f\n", old_m_forward,       Constants.DEFAULT_M_FORWARD,       new_m_forward);
    //System.err.format("m_side          | %6.3f | %6.3f | %8.3f\n", old_m_side,          Constants.DEFAULT_M_SIDE,          new_m_side);
    //System.err.format("\n");
    //System.out.printf("or\n");
    //System.err.format("\n");
    //System.out.printf("  mcpicalc -f autoexec.cfg\n");

    System.err.println();

    // Print out configuration chunk for autoexec.cfg
    System.out.println("// THE FOLLOWING GOES IN AUTOEXEC.CFG");
    System.out.println("//");
    System.out.println("// These are the three values you actually configure, going forward.  Change");
    System.out.println("// m_cpi to match the DPI of your mouse if it changes, and adjust");
    System.out.println("// cl_mouseAccelPower and cl_mouseAccelOffset to your taste.");
    System.out.println("// The default values of \"power=2\" and \"offset=0\" will give you the");
    System.out.println("// behaviour previously known as \"style 0 acceleration\". The power and offset");
    System.out.println("// values are not calculated...the comments below are just included as");
    System.out.println("// placeholders to show id's default settings (you should tweak to your liking).");
    System.out.format( "set m_cpi %-4d                    // Mouse hardware CPI (aka DPI) [%d]\n", m_cpi, Constants.DEFAULT_M_CPI);
    System.out.format( "//set cl_mouseAccelPower 2        // Power of acceleration [%.3f]\n", Constants.DEFAULT_CL_MOUSEACCELPOWER);
    System.out.println("//set cl_mouseAccelOffset 0       // Speed at which acceleration begins to");
    System.out.println("                                  // apply (below this, the sensitivity is");
    System.out.format( "                                  // constant) [%.3f]\n", Constants.DEFAULT_CL_MOUSEACCELOFFSET);
    System.out.println("");
    System.out.println("// The rest of these are just \"support variables\", and shouldn't need to be");
    System.out.println("// changed with the new CPI-based system (unless your personal preferences on");
    System.out.println("// how you want the mouse to respond actually change, of course)");
    System.out.format( "set sensitivity %-8.3f          // Base mouse sensitivity [%.3f]\n", new_sensitivity, Constants.DEFAULT_SENSITIVITY);
    System.out.format( "set m_yaw %-8.3f                // Horizontal mouse sensitivity modifier [%.3f]\n", Constants.NEW_M_YAW, Constants.DEFAULT_M_YAW);
    System.out.format( "set m_pitch %-8.3f              // Vertical mouse sensitivity modifier [%.3f]\n", new_m_pitch, Constants.DEFAULT_M_PITCH);
    System.out.format( "set cl_mouseAccel %-8.3f        // Acceleration multiplier [%.3f]\n", new_cl_mouseAccel, Constants.DEFAULT_CL_MOUSEACCEL);
    System.out.format( "set cl_mouseSensCap %-8.3f      // Ceiling for sensitivity adjustment [%.3f]\n", new_cl_mouseSensCap, Constants.DEFAULT_CL_MOUSESENSCAP);
    System.exit(0);

  } // End of main()

} // End of class MouseCalc
