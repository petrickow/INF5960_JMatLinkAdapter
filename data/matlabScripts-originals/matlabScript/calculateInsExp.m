function [avgTI,stdTI,avgTE,stdTE] = calculateInsExp(newP,newT);

% newP contains the markers for the start and stop of each pause around 
% each good peak - DECIMATED.  newP(1) is the start of the pause for peak #1, newP(2)
% is the stop for the pause around peak #1, etc.
% newT is the same for the troughs.

% the average and standard deviation of the inspiration time (TI) and 
% expiration time (TE) are returned.

Insps = [];  % initialize empty array to hold the combination of P and T
Exps = [];   % and array to hold the expiration combination

% determine which array is bigger
nP = max(size(newP)); nT = max(size(newT));
if nP < nT 
    m = nP;
else
    m = nT;
end;

% figure out if the signal starts with a peak or a trough
if newP(1) < newT(1)  % peak-first signal
   for i = 1:((m/2) - 1)
      Insps = [Insps, (newP((2*i) + 1) - newT(2*i)) * 5];
      Exps = [Exps, (newT((2*i) - 1) - newP(2*i)) * 5];
   end;
else  % trough-first signal
   for i = 1:((m/2) - 1)
      Insps = [Insps, (newP((2*i)-1) - newT((2*i))) * 5];
      Exps = [Exps, (newT((2*i)+1) - newP(2*i)) * 5];
   end;
end;

% now Insps just holds the length (in samples) of the inspirations
avgTI = mean(Insps); stdTI = std(Insps);  % do the statistics
avgTE = mean(Exps); stdTE = std(Exps);  % and for expirations
