function [P, T] = adjustFlatPT(Qd, allP, allT, newP, newT);

% this function recreates P and T by moving each peak and trough marker
% to the center of its pause as saved in newP and newT.
% allP is validPeaks, allT is validTroughs.  
% there are two entries in newP for each in allP

P = []; T = []; left = 0; right = 0; i = 0;
numPeaks = length(allP);  % # of peaks

for i = 1:numPeaks
    left = newP((2 * i) - 1);
    right = newP(2 * i);
    if left > allP(i) 
       s = ['error1 - ', int2str(allP(i)), ' left=', int2str(left), ' right=', int2str(right)]
    elseif right < allP(i)
       s = ['error2 - ', int2str(allP(i)), ' left=', int2str(left), ' right=', int2str(right)]
    else
       P = [P, round((left + right) / 2)];
    end;
end;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

numTroughs = length(allT);

for i = 1:numTroughs
     left = newT((2 * i) - 1);
     right = newT(2 * i);
     if left > allT(i)
        s = ['error3 - ', int2str(allT(i)), ' left=', int2str(left), ' right=', int2str(right)]
     elseif right < allT(i)
        s = ['error4 - ', int2str(allT(i)), ' left=', int2str(left), ' right=', int2str(right)]
     else
        T = [T, round((left + right) / 2)];
     end;
end;

