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
    % pseudocode:
    
    peaks = newP + offset;
    troughs = newT + offset;
    
    disp(peaks);
    disp(troughs);
    
    save('data\peaksRes.mat', 'peaks' );
    save('data\troughRes.mat', 'troughs');
end

