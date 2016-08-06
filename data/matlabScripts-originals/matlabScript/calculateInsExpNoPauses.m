function [avgTI,stdTI,avgTE,stdTE] = calculateInsExpNoPauses(peaks,troughs);

% peaks and troughs contain the UNDECIMATED time points of the marked peaks and troughs
% the average and standard deviation of the inspiration time (TI) and 
% expiration time (TE) are returned.

Insps = [];  % initialize empty array to hold the combination of P and T
Exps = [];   % and array to hold the expiration combination
% determine which array is bigger
nP = length(peaks);  nT = length(troughs);
if nP < nT 
    m = nP;
else
    m = nT;
end;

% figure out if the signal starts with a peak or a trough
if peaks(1) < troughs(1)  % peak-first signal
   for i = 1:(m - 1)
       Insps = [Insps, (peaks(i + 1) - troughs(i))];
       Exps = [Exps, (troughs(i) - peaks(i))];
   end;
else  % trough-first signal
   for i = 1:(m - 1)
      Insps = [Insps, (peaks(i) - troughs(i))];
      Exps = [Exps, (troughs(i + 1) - peaks(i))];
   end;
end;

% now Insps just holds the length (in samples) of the inspirations
%Insps = Insps/HzUsed; Exps = Exps/HzUsed;  % change units to seconds

avgTI = mean(Insps); stdTI = std(Insps);  % do the statistics
avgTE = mean(Exps); stdTE = std(Exps);  % and for expirations
