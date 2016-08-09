% VERISON --> TARGET, USED BY ORIGINAL PUKA
function [ ] = writeResults( offset, newP, newT )
%WRITERESULTS Summary of this function goes here
%   This script is called after an analysis window
%   is completed. It takes the resulting valid peaks
%   and troughs with calculated pauses found in newP and newT.
%   Adds the appropriate offset to them and
%   stores them persistently in mat files that can be 
%   imported alongside the original signal and also
%   compared to the result of other implementaions for
%   calculating precision and recall metrics

%   The newP and newT is multiplied with 5 to reflect the
%   raw signal decimation in newPT.m
    
    peaks = (newP*5) + offset;
    troughs = (newT*5) + offset;
    
%   append to file. Make sure to delete existing files
    peaksFile = fopen('data\reduPeaks.txt','a');
    troughsFile = fopen('data\reduTroughs.txt', 'a');
    
    disp(pwd); 
    
    fprintf(peaksFile, '%d\n', peaks);
    fprintf(troughsFile, '%d\n', troughs);
    
    fclose(peaksFile);
    fclose(troughsFile);
    
%   This would be preferable as we need to import it into
%   matlab to plot and analyse the results.
    %save('data\peakTrough.mat', 'peaks', 'troughs', '-append');
    %save('data\.mat', 'troughs');
end