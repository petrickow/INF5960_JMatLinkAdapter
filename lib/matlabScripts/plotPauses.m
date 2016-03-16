function [] = plotPauses(Qd, validPeaks, validTroughs, th, newP, newT);
% plots the peaks and troughs with their pauses on the respiration signal

plot(Qd, 'm'); hold on;
plot(newP, Qd(newP), 'ob', 'MarkerSize',10);  
plot(newT, Qd(newT), 'ob', 'MarkerSize',10);

n = max(size(validPeaks));  % n has number of peaks found
for i = 1:n
    plot(validPeaks(i), Qd(validPeaks(i)), 'xk', 'MarkerSize', 10);
    text(validPeaks(i), Qd(validPeaks(i)) + th*.5, ['p', int2str(i)], 'FontSize', 8);
end;

n = max(size(validTroughs));  % n has number of troughs found
for i = 1:n
    plot(validTroughs(i), Qd(validTroughs(i)), 'xk', 'MarkerSize', 10);
    text(validTroughs(i), Qd(validTroughs(i)) - th*.5, ['t', int2str(i)], 'FontSize', 8);
end;
hold off;