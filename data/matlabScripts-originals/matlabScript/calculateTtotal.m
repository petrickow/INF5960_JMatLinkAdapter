function [avgTtot,stdTtot] = calculateTtotalNoPauses(troughs);
% called by: [avgTtot,stdTtot] = calculateTtotalNoPauses(troughs);

% troughs contains the points marked as the troughs for each breath
% returns the average and standard deviation of the total breath time,
% the time from the start of one inspiration to the next.

diffPts = abs(diff(troughs));  % find difference between all adjacent points in newT
%diffPts = diffPts/HzUsed;
avgTtot = mean(diffPts); stdTtot = std(diffPts);  % do the statistics
