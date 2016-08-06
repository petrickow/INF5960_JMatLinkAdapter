function [] = plotRSA(RR, y, saveDiffRwin, saveDiffNum, firstR, lastR, troughs);
% plots the respiration signal with lines indicating the R wave
% peaks and values for the heart period and RSA.

figure(2); xSpot = 0;

plot(y, 'b'); hold on;  % plot the undecimated resp signal
minQ = min(y) - 1;
n = length(RR);  % put lines on the resp signal to show each
for m = 1:n      % R wave spot.    
  if RR(m) >= firstR & RR(m) <= lastR
    xLine = [RR(m), RR(m)];
    yLine = [minQ, y(RR(m))];
    line(xLine,yLine);  % draw line that shows where the R beat was
  end;
end;

n = length(troughs);  % show dots at each trough
for m = 1:n
  plot(troughs(m), y(troughs(m)), 'xy');
end;
  

% draw another line for the middle of the beat
n = length(saveDiffRwin);
xSpot = firstR;
for m = 1:n
  xValue = (xSpot + (saveDiffRwin(m)/2));
  xLine = [xValue, xValue];
  yLine = [minQ - .5, minQ + .5];
  xSpot = xSpot + saveDiffRwin(m);
  if mod(saveDiffNum(m), 2) == 0
    line(xLine, yLine, 'Color', 'g');
  else
    line(xLine, yLine, 'Color', 'r');
  end;
  text(xValue, (minQ - .75), int2str(saveDiffRwin(m)));
end;


% draw the text of the beat length below the line
hold off;
