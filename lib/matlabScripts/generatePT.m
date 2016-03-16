function [peaks,troughs] = generatePT(P,T,peakLabels, troughLabels);
% creates peaks and troughs, arrays of the undecimated peak and trough locations
% of all valid peaks and troughs in P & T.

% P  the vector of peak indexes, on the decimated-by-5 signal
% T  the vector of trough indexes, on the decimated-by-5 signal
% peakLabels & troughLabels assigned by classifyPeaks, then modified
% manually if necessary:  1 = valid 2 = invalid 3 = questionable

peaks = [];
n = max(size(P));  % n has number of peaks found
for i = 1:n
    if peakLabels(i) == 1       % it is a good peak
        peaks = [peaks, (P(i)*5)];  % add this UNDECIMATED peak to the peaks array
    end;
end;

troughs = [];
n = max(size(T));  % n has number of troughs found
for i = 1:n
    if troughLabels(i) == 1       % it is a good trough
        troughs = [troughs, (T(i)*5)];  % add this UNDECIMATED trough to the troughs array
    end;
end;