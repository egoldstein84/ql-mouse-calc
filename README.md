ql-mouse-calc
==========

Calculate new Quake Live mouse sensitivity values using the new m\_cpi variable

Background
----------

You can get background on this new configuration feature, and why you might
want to use it, from [SyncError's post explaining the whole thing] [1].
Basically, the advantage of the new system is that it allows players to
directly compare sensitivity settings, regardless of mouse hardware sampling
resolution (CPI, or commonly called DPI).  Also, it should make adjusting your
configuration for a new mouse dead simple, since you just adjust m\_cpi to
match the new hardware and everything else just follows along.

This script will take your current Quake Live mouse sensitivity settings, along
with your mouse hardware CPI, and generate a configuration block with the new
settings, suitable for pasting into a Quake Live configuration file (like
autoexec.cfg).

If you're interested in taking a deeper dive, check out this
[excellent page on hardcore mouse tuning] [2].

Building
--------

javac -classpath '.:commons-cli-1.2.jar' MouseCalc.java

Running
-------

java -classpath '.:commons-cli-1.2.jar' MouseCalc --help

Documentation
-------------

    MouseCalc [-h] [-a <cl_mouseAccel>] [-c <cl_mouseSensCap>] [-p <m_pitch>] [-s <sensitivity>] [-y <m_yaw>] <m_cpi>

      -h,--help                                Print help message

      -a,--cl-mouseaccel <cl_mouseAccel>       Current value of cl_mouseAccel
      -c,--cl-mousesenscap <cl_mouseSensCap>   Current value of cl_mouseSensCap
      -p,--m-pitch <m_pitch>                   Current value of m_pitch
      -s,--sensitivity <sensitivity>           Current value of sensitivity
      -y,--m-yaw <m_yaw>                       Current value of m_yaw

Provide your mouse hardware's CPI (aka DPI) as m\_cpi, and copy the rest of the
values from your current configuration.  If you're not sure what your current
settings are, you can use "\set \<variable\>" in the console to see what the
engine is using at the moment.

TODO
----

* Make an applet version
* Combine everything into one file, so it's just a single executable
* Read in an existing Quake Live autoexec.cfg and pull the values from there




[1]: http://www.quakelive.com/forum/showthread.php?15458 "quakelive.com"
[2]: http://www.funender.com/quake/mouse                 "funender.com"
