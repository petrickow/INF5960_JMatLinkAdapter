function [newT] = markTroughPauses(Qd, validPeaks, validTroughs, th);
% check for same-height pts around each peak and trough found in newPT

% Qd the decimated respiration signal
% validPeaks  the vector of peak indexes (only valid), on the decimated-by-5 (200Hz) signal  
% validTroughs  the vector of trough indexes (only valid), on the decimated-by-5 signal
% th the threshold used by newPT
m = 0;  % counter for pts near each peak or trough
newT = []; % matrix of new trough elements

thisTrough = 0; nextPeak = 0; prevPeak = 0;

numPeaks = max(size(validPeaks));  % # of valid peaks after classifyPeaks.m
numTroughs = max(size(validTroughs));
disp('mofo!');
for i = 1:numTroughs
    thisTrough = validTroughs(i);  % going to find the pause size for this trough
    if validPeaks(1) < validTroughs(1)    % peak-first signal
        prevPeak = validPeaks(i);  % peak-first, so prev peak has same index as this trough
        %s = 'peak-first'
        if i < numPeaks
            nextPeak = validPeaks(i + 1);  % next peak has index + 1 number
        else
            nextPeak = validPeaks(numPeaks);  % if no end peak, assign to last one
        end;
    else   % trough-first signal
        %s = 'trough-first'
        if i > 1
            prevPeak = validPeaks(i - 1);  % trough-first, so prev peak has index i - 1
        else
            prevPeak = validPeaks(1); % if the first trough, can't use peak i - 1, so use peak 2
        end;
        if i < numPeaks
            nextPeak = validPeaks(i);  % trough-first, so next peak has index i
        else
            nextPeak = validPeaks(numPeaks);  % don't want to fall off the end if there is one peak too few
        end;
    end;

   
    %%%%%%%%%%%%%%%%%%%%%%% find the pause to the LEFT of this trough %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    sth = abs((Qd(prevPeak) - Qd(thisTrough))  * .30);  % small threshold = 15% of slope height
    mth = abs((Qd(prevPeak) - Qd(thisTrough))  * .08);  % medium threshold
    ssth = abs(Qd(prevPeak) - Qd(thisTrough))  * .0005;  % super-small threshold
    m = -1; littles = 0; negatives = 0; stop = 0;  % set markers 
    
    % loop until end of window is sth HIGHER than the trough
    while (Qd(thisTrough + m) - Qd(thisTrough)) < sth
        if (thisTrough + m) == 2
            m = m + 1
            break;
        else
	       m = m - 1;  % back up one more step
        end;
    end;
	
    if thisTrough == 11499
        s = ['m = ', int2str(m), ' before looping back']
    end;

    while stop == 0   % now loop back towards the trough until a stopping condition is met
        if abs(Qd(thisTrough + m) - Qd(thisTrough + m - 1)) < ssth  % slope of last point less than ssth
            littles = littles + 1;  % zero or not too steep slope
        else
            littles = 0;  % other - medium or steep slope
        end;
        if (Qd(thisTrough + m - 1) - Qd(thisTrough + m) < 0)
            negatives = negatives + 1;   % this point lower than last - should be higher
        else
            negatives = 0;
        end;
        
        if thisTrough == 11499
             s = ['m = ', int2str(m), ' looping back']
        end;
		
        if m == -1 
            m = 0;
            stop = 1;  % back to trough point - stop
            s = ['m == -1 - LT', int2str(i), ' thisTrough=', int2str(thisTrough), ' mark=', int2str(thisTrough + m)]
        elseif Qd(thisTrough + m) <= Qd(thisTrough)
            stop = 1;  % height is lower or equal to the trough - stop
            s = ['back to start level - LT', int2str(i), ' thisTrough=', int2str(thisTrough), ' mark=', int2str(thisTrough + m)]    
        elseif littles > 30 & ((Qd(thisTrough + m) - Qd(thisTrough)) < mth)
            stop = 1;  % slope not changing much anymore
            m = m - 15; % move back to middle of the 3 littles
            s = ['littles > 30 - LT', int2str(i), ' thisTrough=', int2str(thisTrough), ' mark=', int2str(thisTrough + m)]  
        elseif negatives > 10 & ((Qd(thisTrough + m) - Qd(thisTrough)) < mth)
            stop = 1;  % went the wrong direction for 10 points in a row
            m = m - 10;
            s = ['negatives > 10 - LT', int2str(i), ' thisTrough=', int2str(thisTrough), ' mark=', int2str(thisTrough + m)]  
        else
            m = m + 1; % move one point more towards the trough
        end;
    end;
    newT = [newT, (thisTrough + m)];  % add in new point

           
    %%%%%%%%%%%%%%%%%%%%%%% find the pause to the RIGHT of this trough %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    sth = abs((Qd(nextPeak) - Qd(thisTrough))  * .30);  % small threshold = 15% of slope height
    mth = abs((Qd(prevPeak) - Qd(thisTrough))  * .08);  % medium threshold
    ssth = abs(Qd(nextPeak) - Qd(thisTrough))  * .0005;
    m = 1;  % set counter for checking to the right of the trough
    littles = 0; negatives = 0; stop = 0;  % set marker for moving back to end of pause
    
    % loop until end of window is sth HIGHER than the trough
    while (thisTrough + m + 3 < max(size(Qd))) & (Qd(thisTrough + m) - Qd(thisTrough)) < sth  
       m = m + 1;
    end;
     
    while stop == 0   % now loop back towards the trough until a stopping condition is met
        thisSlope = Qd(thisTrough + m + 1) - Qd(thisTrough + m);  % slope of last point
        if abs(thisSlope) < ssth 
            littles = littles + 1;  % zero or not too steep slope
        else
            littles = 0;  % other - medium or steep slope
        end;
        if (Qd(thisTrough + m + 1) - Qd(thisTrough + m) < 0)
            negatives = negatives + 1;   % this point lower than last - should be higher
        else
            negatives = 0;
        end;
                
        if m <= 1 
            m = 0; stop = 1;  % back to trough point - stop
            s = ['m <= 1 - RT', int2str(i), ' thisTrough=', int2str(thisTrough), ' mark=', int2str(thisTrough + m)]
        elseif Qd(thisTrough + m) <= Qd(thisTrough)
            stop = 1;  % height is lower or equal to the trough - stop
            s = ['back to start level - RT', int2str(i), ' thisTrough=', int2str(thisTrough), ' mark=', int2str(thisTrough + m)]    
        elseif littles > 30 & ((Qd(thisTrough + m) - Qd(thisTrough)) < mth)
            stop = 1;  % slope not changing much anymore
            m = m + 15; % move back to the start of the 3 littles
            s = ['littles > 30 - RT', int2str(i), ' thisTrough=', int2str(thisTrough), ' mark=', int2str(thisTrough + m)]  
        elseif negatives > 10 & ((Qd(thisTrough + m) - Qd(thisTrough)) < mth)
            stop = 1;  % went the wrong direction for 10 points in a row
            m = m + 10;
            s = ['negatives > 10 - RT', int2str(i), ' thisTrough=', int2str(thisTrough), ' mark=', int2str(thisTrough + m)]  
        else
            m = m - 1; % back one point
        end;
    end;
    newT = [newT, (thisTrough + m)];  % add in new point

end;

