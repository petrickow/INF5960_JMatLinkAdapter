function [peakLabels,troughLabels] = classifyPeaks(Qd,P,T,th);
% go across entire signal, looking at narrow window around each peak

% try 1 second windows around each peak/trough, centered on found peak
% 1000 Hz signal decimated by 5, so now 200 Hz; 200 data pt window either side
peakLabels = []; % labels for type of peak: 1 = valid, 2 = invalid, 3 = question
troughLabels = []; % labels trough type: 1 = valid, 2 = invalid, 3 = question
validPeaks = [];
validTroughs = [];
thisPeakLabel = 0; thisTroughLabel = 0; i = 1;

% check if the first peak is too close to the start of the signal
if P(1) > 156
    start = 1;  % start at first - there's enough room for a window
else
    while P(i) < 156
        i = i + 1;
    end;
    start = i;  % start at first peak higher than 101
end;
% check if the last peak is too close to the end of the signal
numPeaks = max(size(P));
if (P(numPeaks) + 156) < max(size(Qd))
    stop = numPeaks;      % stop at last peak
else
    i = -1;
    while (P(numPeaks + i) + 156) > max(size(Qd))
        i = i - 1;
    end;
    stop = numPeaks + i;
end;

% first check all of the peaks
for i = start:stop
    windowB4 = Qd((P(i)-150):P(i));  % window before the peak
    windowAf = Qd(P(i):(P(i)+ 150));  % window after the peak
    
    diffWB4 = diff(windowB4); % difference between all adjacent pts in the window
    diffWAf = diff(windowAf);
    [indNegB4] = find(diffWB4 < 0);   % neg diff = curve going down
    [indPosB4] = find(diffWB4 > 0);   % pos diff = curve going up
    [indNegAf] = find(diffWAf < 0);   % neg diff = curve going down
    [indPosAf] = find(diffWAf > 0);   % pos diff = curve going up
    
    negB4 = max(size(indNegB4)); % # of neg diffs before the peak
    negAf = max(size(indNegAf)); % # of neg diffs after the peak    
    posB4 = max(size(indPosB4)); % # positive diffs before the peak
    posAf = max(size(indPosAf)); % # positive diffs after the peak    
    
    thisPeakLabel = 1;  % assume that most peaks are VALID
    
    % start or end of window higher than peak
    if windowB4(1) >= Qd(P(i)) | windowAf(150) >= Qd(P(i))  
        thisPeakLabel = 2; % label peak as invalid
        a = 'start or end of window higher than peak'
    end;
    % more diffs in wrong direction on either side of peak than correct
    % SHOULD be neg after and pos before
    if negAf < negB4 | posB4 < posAf
        thisPeakLabel = 2; % label peak as invalid
        a = 'more diffs in wrong direction on either side of peak'
    end;  
    % check amount of difference on each side of the peak
    if sum(diffWB4) < 0 | sum(diffWAf) > 0
        thisPeakLabel = 2; % label peak as invalid
        a = 'too much in wrong direction around peak'
    end;  
    peakLabels = [peakLabels, thisPeakLabel];  % add this peak label to the end of the list
end;

% add in labels for the first and last peaks, if needed
if start ~= 1
    counter = 1;
    while counter < start
        peakLabels = [1, peakLabels];  % assign first peak as indeterminant
	counter = counter + 1;
    end;
end;

if stop ~= numPeaks
    counter = 0;
    while (numPeaks - stop) > counter
        peakLabels = [peakLabels, 1];  % assign last peak as valid
	counter = counter + 1;
    end;    
end;

%%%%%%%%%%%%%%%%%%%TROUGHS%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% check if the first trough is too close to the start of the signal
i = 1;  % reset marker
if T(1) > 156
    start = 1;  % start at first - there's enough room for a window
else
    while T(i) < 156
        i = i + 1;
    end;
    start = i;  % start at first trough higher than 101
end;
% check if the last trough is too close to the end of the signal
numTroughs = max(size(T));
if (T(numTroughs) + 156) < max(size(Qd))
    stop = numTroughs;      % stop at last trough
else
    i = -1;
    while (T(numTroughs + i) + 156) > max(size(Qd))
        i = i - 1;
    end;
    stop = numTroughs + i;
end;

% check all of the troughs
for i = start:stop
    windowB4 = Qd((T(i)- 150):T(i));  % window before the trough
    windowAf = Qd(T(i):(T(i)+ 150));  % window after the trough
    
    diffWB4 = diff(windowB4); % difference between adjacent pts in the window
    diffWAf = diff(windowAf);
    [indNegB4] = find(diffWB4 < 0);   % neg diff = curve going down
    [indPosB4] = find(diffWB4 > 0);   % pos diff = curve going up
    [indNegAf] = find(diffWAf < 0);   % neg diff = curve going down
    [indPosAf] = find(diffWAf > 0);   % pos diff = curve going up
    
    negB4 = max(size(indNegB4)); % # of neg diffs before the trough
    negAf = max(size(indNegAf)); % # of neg diffs after the trough    
    posB4 = max(size(indPosB4)); % # positive diffs before the trough
    posAf = max(size(indPosAf)); % # positive diffs after the trough    
    
    thisTroughLabel = 1;  % assume most troughs valid
    
    % start or end of window lower than marked trough
    if windowB4(1) <= Qd(T(i)) | windowAf(150) <= Qd(T(i))  
        thisTroughLabel = 2; % label trough as invalid
        a = 'start or end of window lower than marked trough'
    end;
    % more diffs in wrong direction on either side of trough than correct
    % SHOULD be neg before and pos after
    if negAf > negB4 | posB4 > posAf
        thisTroughLabel = 2; % label trough as invalid
        a = 'more diffs in wrong direction on either side of trough than correct'
    end;  
    % check amount of difference on each side of the trough
    if sum(diffWB4) > 0 | sum(diffWAf) < 0
        thisTroughLabel = 2; % label trough as invalid
        a = 'too much in wrong direction around trough'
    end;  

    troughLabels = [troughLabels, thisTroughLabel];  % add label to end of list
end;
% add in labels for the first and last troughs, if needed
if start ~= 1
    counter = 1;
    while counter < start
	troughLabels = [1, troughLabels];  % assign first trough as indeterminant
	counter = counter + 1;
    end;
end;
if stop ~= numTroughs
    counter = 0;
    while (numTroughs - stop) > counter
        troughLabels = [troughLabels, 1];  % assign last trough as indeterminant
	counter = counter + 1;
    end;
end;

plot(Qd, 'm'); hold on;  
whitebg([.9 .9 .9]);   % set background color to gray
n = max(size(P));  % n has number of peaks found
for i = 1:n
    if peakLabels(i) == 1
        plot(P(i), Qd(P(i)), 'xb', 'MarkerSize', 10);
    elseif peakLabels(i) == 2
        plot(P(i), Qd(P(i)), 'xr', 'MarkerSize', 10);
    end;
    text(P(i), Qd(P(i)) + th*.5, ['p', int2str(i)], 'FontSize', 8);
end;
n = max(size(T)); % number of troughs
for i = 1:n
    if troughLabels(i) == 1
        plot(T(i), Qd(T(i)), 'xb', 'MarkerSize', 10);
    elseif troughLabels(i) == 2
        plot(T(i), Qd(T(i)), 'xr', 'MarkerSize', 10);
    end;
    text(T(i), Qd(T(i)) - th*.5, ['t', int2str(i)], 'FontSize', 8);
end;
hold off;
