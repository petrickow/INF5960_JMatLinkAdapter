puka modernization
==================

Maven project that wrapps calls to JMatlink (used by puka to make calls to MATLAB)
with matlabcontrol-library in order to be able to run on/with 64-bit windows/MATLAB.

This project has only translated the methods used by puka.


Prerequisites for vanilla puka:
-------------------------------

WFDB library and software installed for cygwin (WIP):
-----------------------------------------------
* For cygwin packages required, see
* convertecg.c compiled with gcc convertecg.c -lwfdb -o convertecg
* ecgpuwave-1.3.2 requires fortran compiler for cygwin.

* Simplest solution is to run the ''frmMain'' as Java application from Eclipse
* Alternately package as jar as launch
 

Useful links
-----------
* [WFDB library](http://www.physionet.org/physiotools/wfdb.shtml#library)
* [WPG article](http://www.physionet.org/physiotools/wpg/wpg.pdf)
* [ECGPU Wave](https://www.physionet.org/physiotools/ecgpuwave/)
* [Puka](https://www.physionet.org/physiotools/puka/)


License
-----------
GNU General Public License
