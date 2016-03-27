function [P,T,th,Qd] = newPT(Qraw, factor, onsetTime, endTime);
% have the respiration signal in a column vector called Q
% call by >> [P,T,th,Qd] = newPT(Q);
% code adapted from Todd & Andrews. The Identification of Peaks in Physiological
% Signals. Computers and Biomedical Research 32, 322-335 (1999).
%disp(numel(Qraw));
Qd = decimate(Qraw, 5);  % downsample the signal
%disp(numel(Qd));

d = 1;      % variable to show if signal unknown (1), trough-to-peak (2),
            % or peak-to-trough (3)
a = 1;      % index of maximal element since last trough
b = 1;      % index of minimal element since last peak
S = [];     % indices of maximal elements since last trough if signal rising (d = 2)
            % or minimal elements since last peak if signal falling (d = 3)
P = [];     % peak elements
T = [];     % trough elements
% factor is the number to use when making the threshold; default is 0.1

th = abs(prctile(Qd,75) - prctile(Qd,25)) * factor;
% th = abs(max(Qd) - min(Qd)) * .5;  % threshold

[n] = max(size(Qd));  % get size of the array so can do the for loop
for ind = 1:n
    if d == 1
        if Qd(a) >= Qd(ind) + th
            d = 3;
        elseif Qd(ind) >= Qd(b) + th
            d = 2;
        end;
        if Qd(a) < Qd(ind)
            a = ind;
        elseif Qd(ind) < Qd(b)
            b = ind;
        end;
        S = ind;
    elseif d == 2  % signal rising, trough-to-peak
        if Qd(a) < Qd(ind) % still rising
            S = ind; 
            a = ind;
        elseif Qd(a) == Qd(ind)
            S = [S,ind];            
        elseif Qd(a) >= Qd(ind) + th
            P = [P,S]; S = ind;
            b = ind; d = 3;
        end;
    elseif d == 3  % signal falling, peak-to-trough
        if Qd(ind) <= Qd(b)
            S = ind; 
            b = ind;
        elseif Qd(b) == Qd(ind)
           S = [S,ind];
        elseif Qd(ind) >= Qd(b) + th
            T = [T,S]; S = ind;
            a = ind; d = 2;
        end;
    end;
end;
        
% go through P and T, removing marks for peaks/troughs before the start point
% and after the end point
goodP = [];  goodT = [];
onsetTime = onsetTime/5; endTime = endTime/5;  % to match decimation
n = length(P);
for ind = 1:n
    if P(ind) > onsetTime
        if P(ind) < endTime
            goodP = [goodP, P(ind)];   % only add in peaks bigger than onsetTime
        end;
	end;
end;
n = length(T);
for ind = 1:n
     if T(ind) > onsetTime
        if T(ind) < endTime
          goodT = [goodT, T(ind)];   % only add in troughs bigger than onsetTime
        end;
    end;
end;

P = []; T = []; P = goodP; T = goodT;  % reset arrays for return values


plot(Qd, 'm'); whitebg([.9 .9 .9]);   % set background color to gray
hold on;  % plot the signal and peaks
plot(P, Qd(P), 'ob', 'MarkerSize',10);
plot(T, Qd(T), 'ob', 'MarkerSize',10);
hold off;