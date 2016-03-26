function [newP] = markPeakPauses(Qd, validPeaks, validTroughs, th);
% check for same-height pts around each peak and trough found in newPT

% Qd the decimated respiration signal
% validPeaks  the vector of peak indexes, on the decimated-by-5 (200Hz) signal  
% validTroughs  the vector of trough indexes, on the decimated-by-5 signal
% th the threshold used by newPT

m = 0;  % counter for pts near each peak or trough
newP = [];   % vector of pause starts & stops
thisPeak = 0; nextTrough = 0; prevTrough = 0;

numPeaks = length(validPeaks);  % # of valid peaks after classifyPeaks.m
numTroughs = length(validTroughs);

for ind = 1:numPeaks
    thisPeak = validPeaks(ind);  % going to find the pause size for this peak
    if validPeaks(1) < validTroughs(1)    % peak-first signal
        if ind <= numTroughs
            nextTrough = validTroughs(ind);  % peak-first, so next trough has same index
        else
            nextTrough = validTroughs(numTroughs);  % if no next trough assign the last one
        end;
        if ind > 1 & ind < numTroughs
            prevTrough = validTroughs(ind - 1);  % and previous trough has previous index
        else
            prevTrough = validTroughs(1);  % assign to first trough
        end;
    else % through first
        if ind < numTroughs
            nextTrough = validTroughs(ind + 1);  % trough-first, so next trough has index + 1 number
        else
            nextTrough = validTroughs(numTroughs);
        end;
        if ind <= numTroughs
            prevTrough = validTroughs(ind);  % and previous trough has same index
        else
            nextTrough = validTroughs(numTroughs);
        end;
    end;

    %%%%%%%%%%%%%% find pause to the left of the peak %%%%%%%%%%%%%%
    sth = abs((Qd(prevTrough) - Qd(thisPeak))  * .30);  % small threshold = 15% of slope height
    mth = abs((Qd(prevTrough) - Qd(thisPeak))  * .08);  % medium threshold
    ssth = abs(Qd(prevTrough) - Qd(thisPeak))  * .0005;
    m = -1; littles = 0; negatives = 0; stop = 0;  
    
    % loop until end of window is sth LOWER than the peak
    while abs(Qd(thisPeak) - Qd(thisPeak + m)) < sth
	    if (thisPeak + m) == 2
		    m = m + 1;
		    break;
	    else
	       m = m - 1;
	    end;
    end;
    % now loop back until a stopping condition is met - that is the end of the peak 
    while stop == 0 
        disp('thisPeak and m');
        disp(thisPeak);
        disp(m);
        if abs(Qd(thisPeak + m - 1) - Qd(thisPeak + m)) < ssth % WHY -2?
            littles = littles + 1;  % last point has zero or not-too-steep slope
        else
            littles = 0;  % other - medium or steep slope
        end;
        if (Qd(thisPeak + m) - Qd(thisPeak + m - 1) < 0)
            negatives = negatives + 1;   % this point lower than last
        else
            negatives = 0;
        end;
                
        if m == -1 
            disp('done');
            m = 0; stop = 1;  % back to trough point - stop
            s = ['m == 1 - Lp', int2str(ind)]
        elseif Qd(thisPeak + m) >= Qd(thisPeak)
            stop = 1;  % height is same as peak point - stop
            s = ['back to start level - Lp', int2str(ind)]    
        elseif littles > 30 & ((Qd(thisPeak) - Qd(thisPeak + m)) < mth)
            stop = 1;  % slope not changing much anymore
            m = m - 15; % move back to start of the littles
            s = ['littles > 30 - Lp', int2str(ind)]  
        elseif negatives > 10 & ((Qd(thisPeak) - Qd(thisPeak + m)) < mth)
            stop = 1;  % went downhill for 10 points
            m = m - 10;
            s = ['negatives > 10 - Lp', int2str(ind)]  
        else
            m = m + 1;  % move one point closer to the trough
        end;
    end;
    newP = [newP, (thisPeak + m)];  % add in new point
    
    %%%%%%%%%%%%%% find pause to the right of the peak %%%%%%%%%%%%%%
    sth = abs((Qd(nextTrough) - Qd(thisPeak))  * .30);  % small threshold = 15% of slope height
    mth = abs((Qd(nextTrough) - Qd(thisPeak))  * .08);  % medium threshold
    ssth = abs(Qd(nextTrough) - Qd(thisPeak))  * .0005;	% super small threshold
    m = 1;  % set counter for checking to the right of the peak
    littles = 0; negatives = 0;
    stop = 0;  % set marker for moving back to end of pause
    
    % loop until end of window is sth LOWER than the peak
    while (thisPeak + m + 3 < max(size(Qd))) & abs(Qd(thisPeak) - Qd(thisPeak + m)) < sth 
       m = m + 1;
    end;
     
    while stop == 0 
        thisSlope = Qd(thisPeak + m + 1) - Qd(thisPeak + m);  % slope of last point
        if abs(thisSlope) < ssth 
            littles = littles + 1;  % zero or not too steep slope
        else
            littles = 0;  % other - medium or steep slope
        end;
        if (Qd(thisPeak + m) - Qd(thisPeak + m + 1) < 0)
            negatives = negatives + 1;   % this point lower than last
        else
            negatives = 0;
        end;
                
        if m <= 1 
            m = 0; stop = 1;  % back to trough point - stop
            s = ['m <= 1 - Rp', int2str(ind)]
        elseif Qd(thisPeak + m) >= Qd(thisPeak)
            stop = 1;  % height is same as peak point - stop
            s = ['back to start level - Rp', int2str(ind)]    
        elseif littles > 30 & ((Qd(thisPeak) - Qd(thisPeak + m)) < mth)
            stop = 1;  % slope not changing much anymore
            m = m + 15; % move back to the start of the 3 littles
            s = ['littles > 30 - Rp', int2str(ind)]  
        elseif negatives > 10 & ((Qd(thisPeak) - Qd(thisPeak + m)) < mth)
            stop = 1;  % went downhill for 10 points
            m = m + 10;
            s = ['negatives > 10 - Rp', int2str(ind)]  
        else
            m = m - 1; % back one point
        end;
    end;
    newP = [newP, (thisPeak + m)];  % add in new point
    disp('all set');
    disp(newP);  
end;        
