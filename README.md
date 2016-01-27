Prerequisites:

WFDB library and software installed for cygwin.



* convertecg.c compiled with gcc convertecg.c -lwfdb -o convertecg
* ecgpuwave-1.3.2 requires fortran compiler for cygwin.


http://www.physionet.org/physiotools/wfdb.shtml#library
http://www.physionet.org/physiotools/wpg/wpg.pdf

https://www.physionet.org/physiotools/ecgpuwave/

https://www.physionet.org/physiotools/puka/

TODO List:
PRI: Finish tests, asserting that all type convertions are done
properly

Make it so that errors in preferences does not result in the
deletion of the preferences file!

Now that we have preferences, try adding valid clips. Reformat
the samples file so that we have a valid clip.


