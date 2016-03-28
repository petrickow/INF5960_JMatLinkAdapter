function [intChange] = findOnset(raw);
% raw contains the data for the onset trigger in a column vector (n x 1) size
% returns the onset trigger point if found, or -1 if no onset trigger was found

raw = round(raw);  % round signal to nearest integer
yNew = (raw > 0) - (raw < 0); % transform to 0 (false) where the zeros were in raw and -1 (true) everywhere else
intTrue = max(size(find(yNew)));  % find(yNew) makes a new array with one entry for each nonzero element
                                 % in yNew. the size of this array is the number of true elements in yNew.
if isempty(intTrue)   % If none is found, find returns an empty matrix so should exit the function
    intChange = -1;
    return;
end;
intFalse = max(size(yNew)) - intTrue;  % intFalse is the total number of elements minus the true ones

if intTrue > intFalse  % yNew is mostly 1 or -1, so raw is mostly nonzero: zero at the trigger and nonzero everywhere else
    y = abs(yNew) - 1;  % transform yNew so that function is zero except where the onset trigger was pushed
else  % yNew is mostly 0, so raw is mostly zero: nonzero at the trigger and zero everywhere else
    y = yNew;  % already have zero except at trigger
end;
    
k = find(y);    % returns the indices of the array X that point to nonzero elements - WANT zero except at trigger
if isempty(k)   % If none is found, find returns an empty matrix so should exit the function
    intChange = -1;
    return;
end;
    
[ind] = find(abs(diff(k)) > 1);  % ind holds indexes where a trigger started or stopped, if more than one
[m n] = size(ind);  % get size of the array so can do the for loop

if m == 0 | n == 0      % only one trigger time
    intChange(1) = min(k);  % started at smallest time point
    intChange(2) = max(k);   % and stopped at biggest one
else
    intChange(1) = k(1);  % started at smallest time point
    intChange(2) = k(ind(1));  % stop at end of first trigger
end;

intChange = mean(intChange);

% since the function is defined with intChange in brackets intChange will be returned; other
% variables will not be visible outside of this function.  get them by calling >>intNew = findOnset(y);

figure(1); hold on; 
plot(raw, 'b');   % whitebg([.9 .9 .9]);    set background color to gray
plot(intChange, 0, 'or', 'MarkerSize',10); hold off;
