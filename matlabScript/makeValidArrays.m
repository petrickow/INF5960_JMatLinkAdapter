function [validPeaks, validTroughs] = makeValidArrays(P,T,peakLabels, troughLabels);

% remake valid peak and trough vectors
validPeaks = [];
n = max(size(P));  % total number of peaks
for i = 1:n
    if peakLabels(i) == 1
        validPeaks = [validPeaks, P(i)];  % add peak to vector
    end;
end;

validTroughs = [];
n = max(size(T));  % total number of troughs
for i = 1:n
    if troughLabels(i) == 1
        validTroughs = [validTroughs, T(i)];  % add trough to vector
    end;
end;
