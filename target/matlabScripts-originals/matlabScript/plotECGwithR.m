% plot the ECG with R peaks highlighted

plot(ECG, 'm'); hold on;
n = length(RR);  % n has number of peaks found
for m = 1:n
     plot(RR(m), ECG(RR(m)), 'xb', 'MarkerSize',10);
end;
hold off;

