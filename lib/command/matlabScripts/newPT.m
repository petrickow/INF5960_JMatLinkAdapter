function [P,T,th,Qd] = newPT(Qraw, factor, onsetTime, endTime);
% have the respiration signal in a column vector called Q
% call by >> [P,T,th,Qd] = newPT(Q);
% code adapted from Todd & Andrews. The Identification of Peaks in Physiological
% Signals. Computers and Biomedical Research 32, 322-335 (1999).

Qd = decimate(Qraw, 5);  % downsample the signal
disp('signal decimated');


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
for i = 1:n
    if d == 1
        if Qd(a) >= Qd(i) + th
            d = 3;
        elseif Qd(i) >= Qd(b) + th
            d = 2;
        end;
        if Qd(a) < Qd(i)
            a = i;
        elseif Qd(i) < Qd(b)
            b = i;
        end;
        S = i;
    elseif d == 2  % signal rising, trough-to-peak
        if Qd(a) < Qd(i) % still rising
            S = i; 
            a = i;
        elseif Qd(a) == Qd(i)
            S = [S,i];            
        elseif Qd(a) >= Qd(i) + th
            P = [P,S]; S = i;
            b = i; d = 3;
        end;
    elseif d == 3  % signal falling, peak-to-trough
        if Qd(i) <= Qd(b)
            S = i; 
            b = i;
        elseif Qd(b) == Qd(i)
            S = [S,i];
        elseif Qd(i) >= Qd(b) + th
            T = [T,S]; S = i;
            a = i; d = 2;
        end;
    end;
end;
        
% go through P and T, removing marks for peaks/troughs before the start point
% and after the end point
goodP = [];  goodT = [];
onsetTime = onsetTime/5; endTime = endTime/5;  % to match decimation
n = length(P);
for i = 1:n
     if P(i) > onsetTime
        if P(i) < endTime
           goodP = [goodP, P(i)];   % only add in peaks bigger than onsetTime
        end;
     end;
end;
n = length(T);
for i = 1:n
     if T(i) > onsetTime
        if T(i) < endTime
          goodT = [goodT, T(i)];   % only add in troughs bigger than onsetTime
        end;
     end;
end;

P = []; T = [];   % reset arrays for return values
P = goodP; 
T = goodT;

%figure();
plot(Qd, 'm'); whitebg([.9 .9 .9]);   % set background color to gray
hold on;  % plot the signal and peaks
plot(P, Qd(P), 'ob', 'MarkerSize',10);
plot(T, Qd(T), 'ob', 'MarkerSize',10);
hold off;