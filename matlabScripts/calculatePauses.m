function [avgPI,stdPI,avgPE,stdPE] = calculatePauses(newP,newT);

% newP contains the markers for the start and stop of each pause around 
% each good peak - DECIMATED.  newP(1) is the start of the pause for 
% peak #1, newP(2) is the stop for the pause around peak #1, etc.
% newT is the same for the troughs.

% the average and standard deviation of the post-inspiratory pauses (PI) and 
% post-expiratory pauses (PE) are returned.

allDiffs = diff(newP);  % finds the diffs between ALL adjacent points in newP
Pdiffs = [];  % initialize empty array to hold the true differences
n = max(size(allDiffs));  
for i = 1:n
   if mod(i,2) > 0   % save all ODD indexed values (1,3,5, ...) into Pdiffs
       Pdiffs = [Pdiffs, (allDiffs(i) * 5)];  % multiply by 5 for undecimated size
   end;
end;
Pdiffs = abs(Pdiffs);  % so all values are positive

allTDiffs = diff(newT);  % finds the diffs between ALL adjacent points in newT
Tdiffs = [];  % initialize empty array to hold the true differences
n = max(size(allTDiffs));  
for i = 1:n
   if mod(i,2) > 0   % save all ODD indexed values (1,3,5, ...) into Pdiffs
       Tdiffs = [Tdiffs, (allTDiffs(i) * 5)]; % multiply by 5 for undecimated size
   end;
end;
Tdiffs = abs(Tdiffs);  % so all values are positive

avgPI = mean(Pdiffs); avgPE = mean(Tdiffs);  % averages
stdPI = std(Pdiffs); stdPE = std(Tdiffs);  % standard deviations

