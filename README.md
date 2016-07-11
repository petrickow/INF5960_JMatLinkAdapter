puka modernization
==================

Maven project that wrapps calls to JMatlink (used by puka to make calls to MATLAB)
with matlabcontrol-library in order to be able to run on/with 64-bit windows/MATLAB.


Prerequisites for vanilla puka:
-------------------------------

WFDB library and software installed for cygwin:
-----------------------------------------------
* convertecg.c compiled with gcc convertecg.c -lwfdb -o convertecg
* ecgpuwave-1.3.2 requires fortran compiler for cygwin.

Useful links
-----------
* [WFDB library](http://www.physionet.org/physiotools/wfdb.shtml#library)
* [WPG article](http://www.physionet.org/physiotools/wpg/wpg.pdf)
* [ECGPU Wave](https://www.physionet.org/physiotools/ecgpuwave/)
* [Puka](https://www.physionet.org/physiotools/puka/)


TODO List:
------------
PRI: Finish tests, asserting that all type convertions are done
properly

Make it so that errors in preferences does not result in the
deletion of the preferences file!

Now that we have preferences, try adding valid clips. Reformat
the samples file so that we have a valid clip.
