function [newP,newT,sth] = checkPT(Q,P,T,th, peakLabels, troughLabels);
% check for same-height pts around each peak and trough found in newPT
% look at undecimated signal - less distortion

% Q the undecimated respiration signal
% P  the vector of peak indexes, on the decimated-by-5 signal
% T  the vector of trough indexes, on the decimated-by-5 signal
% th the threshold used by newPT: (max(Qd) - min(Qd)) * .1
% peakLabels & troughLabels assigned by classifyPeaks, then modified
% manually if necessary:  1 = valid 2 = invalid 3 = questionable
m = 0;  % counter for pts near each peak or trough
newP = []; % matrix of new peak elements
newT = []; % matrix of new trough elements
sth = th * .3;  % the small threshold
ssth = th * .01; % the super-small threshold

[n] = max(size(peakLabels));  % # of peaks found by newPT
for i = 1:n
    if peakLabels(i) == 1  % only find pause on valid peaks
        thisP = P(i) * 5;       % set to undecimated size
        
        m = 1;  % set counter for checking to the right of the peak
        while abs(Q(thisP + m) - Q(thisP)) < th & (m < 1000)
            m = m + 5;  % loop until end point is th higher than the peak
        end;
        littles = 0;
        stop = 0;  % set marker for moving back to end of pause
        while stop == 0 
            thisSlope = Q(thisP + m + 1) - Q(thisP + m);  % slope of last point
            if thisSlope > (-1 * ssth) & thisSlope < ssth
                littles = littles + 1;  % zero or not too steep slope
            else
                littles = 0;  % other - medium or steep slope
            end;
            if m <= 1 
                stop = 1;  % back to trough point - stop
                s = 'PR m <= 1'
            elseif Q(thisP + m) >= Q(thisP)
                stop = 1;  % height is same as peak point - stop
                s = 'PR back to start level'
            elseif littles > 9
                stop = 1;  % slope not changing much anymore
                m = m + 5; % move back to middle of the 3 littles
                s = 'PR littles > 9'
            else
                m = m - 1; % back one point
            end;
        end;
        newP = [newP, (thisP + m)];  % add in new point
        
        m = -1;  % set counter for checking to the left
        while abs(Q(thisP + m) - Q(thisP)) < th & (m > -1000)
            m = m - 5;  % loop until end point is th higher than the peak
        end;
        littles = 0;
        stop = 0;  % set marker for moving back to end of pause
        while stop == 0 
            thisSlope = Q(thisP + m - 1) - Q(thisP + m);  % slope of last point
            if thisSlope > (-1 * ssth) & thisSlope < ssth
                littles = littles + 1;  % zero or not too steep
            else
                littles = 0;  % other - medium or steep slope
            end;
            if m == -1  % back to peak point - stop
                stop = 1;
                s = 'PL m <= 1'
            elseif Q(thisP) <= Q(thisP + m)
                stop = 1;  % height is same as peak point - stop
                s = 'PL back to start level'
            elseif littles > 9
                stop = 1;  % slope not changing much anymore
                m = m - 5; % move back to middle of "little" stretch
                s = 'PL littles > 9'
            else
                m = m + 1;  % back one point
            end;
        end;
        newP = [newP, (thisP + m)];
    end;
end;

[n] = max(size(troughLabels));  % # of troughs found by newPT
for i = 1:n
    if troughLabels(i) == 1  % only find pause on valid peaks
        thisT = T(i) * 5;       % set to undecimated size
        
        m = 1;  % set counter for checking to the right of the trough
        while abs(Q(thisT + m) - Q(thisT)) < th & (m < 1000)
            m = m + 5;  % loop until end point is th higher than the trough
        end;
        littles = 0;
        stop = 0;  % set marker for moving back to end of pause
        while stop == 0 
            thisSlope = Q(thisT + m + 1) - Q(thisT + m);  % slope of last point
            if thisSlope > (-1 * ssth) & thisSlope < ssth
                littles = littles + 1;  % zero or not too steep slope
            else
                littles = 0;  % other - medium or steep slope
            end;
            if m <= 1 
                stop = 1;  % back to trough point - stop
                s = 'TR m <= 1'
            elseif Q(thisT + m) <= Q(thisT)
                stop = 1;  % height is same as trough point - stop
                s = 'TR back to start level'
            elseif littles > 9
                stop = 1;  % slope not changing much anymore
                m = m + 5; % move back to middle of the 3 littles
                s = 'TR littles > 9'
            else
                m = m - 1; % back one point
            end;
        end;
        newT = [newT, (thisT + m)];  % add in new point
        
        m = -1;  % set counter for checking to the left
        while abs(Q(thisT + m) - Q(thisT)) < th & (m > -1000)
            m = m - 5;  % loop until end point is th higher than the trough
        end;
        littles = 0;
        stop = 0;  % set marker for moving back to end of pause
        while stop == 0 
            thisSlope = Q(thisT + m - 1) - Q(thisT + m);  % slope of last point
            if thisSlope > (-1 * ssth) & thisSlope < ssth
                littles = littles + 1;  % zero or not too steep
            else
                littles = 0;  % other - medium or steep slope
            end;
            if m == -1  % back to trough point - stop
                stop = 1;
                s = 'TL m <= 1'
            elseif Q(thisT + m) <= Q(thisT)
                stop = 1;  % height is same as trough point - stop
                s = 'TL back to start level'
            elseif littles > 9
                stop = 1;  % slope not changing much anymore
                m = m - 5; % move back to middle of "little" stretch
                s = 'TL littles > 9'
            else
                m = m + 1;  % back one point
            end;
        end;
        newT = [newT, (thisT + m)];
    end;
end;

plot(Q); hold on;
n = max(size(P));  % n has number of peaks found
for i = 1:(n - 1)
    if peakLabels(i) == 1 | peakLabels(i) == 3 
        plot(P(i)*5, Q(P(i)*5), 'xg');
    end;
end;
plot(newP, Q(newP), 'oy', 'MarkerSize',10);

n = max(size(T)); % number of troughs
for i = 1:(n - 1)
    if troughLabels(i) == 1 | troughLabels(i) == 3 
        plot(T(i)*5, Q(T(i)*5), '+g');
    end;
end;
plot(newT, Q(newT), 'ow', 'MarkerSize',10);
hold off;

plot(Qd); hold on;
n = max(size(P));  % n has number of peaks found
for i = 1:(n - 1)
    if peakLabels(i) == 1
        plot(P(i), Qd(P(i)), 'xg');
    elseif peakLabels(i) == 2
        plot(P(i), Qd(P(i)), 'xr');
    elseif peakLabels(i) == 3
        plot(P(i), Qd(P(i)), 'xb');
    end;
    text(P(i), Qd(P(i)) + th*.5, ['p', int2str(i)], 'FontSize', 8);
    %plot((P(i)+200), Qd(P(i)+200), 'oy');  % end of window
    %plot((P(i)-200), Qd(P(i)-200), '.y');  % start of window
end;
n = max(size(T)); % number of troughs
for i = 1:(n - 1)
    if troughLabels(i) == 1
        plot(T(i), Qd(T(i)), '+g');
    elseif troughLabels(i) == 2
        plot(T(i), Qd(T(i)), '+r');
    elseif troughLabels(i) == 3
        plot(T(i), Qd(T(i)), '+b');
    elseif troughLabels(i) == 4
        plot(T(i), Qd(T(i)), '+y');
    end;
    text(T(i), Qd(T(i)) - th*.5, ['t', int2str(i)], 'FontSize', 8);
end;
hold off;
