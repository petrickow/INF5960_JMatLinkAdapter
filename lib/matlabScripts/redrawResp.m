function redrawResp(Qd,P,T,th,peakLabels,troughLabels,What, Which);

% What contains a flag set in puka java code indicating which peaks and troughs
  % should be shown on the graph 
  % What=0 -> show all; 1 -> show only valid; 2 -> show only invalid, 3 -> show only questionable
% Which is similiar for whether to show just peaks (0), just troughs (1), or both (2)

plot(Qd); hold on;
if Which == 0 | Which ==2
 n = max(size(P));  % n has number of peaks found
 for i = 1:(n - 1)
     if peakLabels(i) == 1 & (What == 0 | What == 1)
         plot(P(i), Qd(P(i)), 'xc');
	 text(P(i), Qd(P(i)) + th*.5, ['p', int2str(i)], 'FontSize', 8);
     elseif peakLabels(i) == 2 & (What == 0 | What == 2)
         plot(P(i), Qd(P(i)), 'xr');
	 text(P(i), Qd(P(i)) + th*.5, ['p', int2str(i)], 'FontSize', 8);
     elseif peakLabels(i) == 3 & (What == 0 | What == 3)
         plot(P(i), Qd(P(i)), 'xg');
	 text(P(i), Qd(P(i)) + th*.5, ['p', int2str(i)], 'FontSize', 8);
     end;
 end;
end;

if Which == 1 | Which == 2
 n = max(size(T)); % number of troughs
 for i = 1:(n - 1)
     if troughLabels(i) == 1 & (What == 0 | What == 1)
         plot(T(i), Qd(T(i)), '+c');
	 text(T(i), Qd(T(i)) - th*.5, ['t', int2str(i)], 'FontSize', 8);
     elseif troughLabels(i) == 2 & (What == 0 | What == 2)
         plot(T(i), Qd(T(i)), '+r');
	 text(T(i), Qd(T(i)) - th*.5, ['t', int2str(i)], 'FontSize', 8);
     elseif troughLabels(i) == 3 & (What == 0 | What == 3)
         plot(T(i), Qd(T(i)), '+k');
	 text(T(i), Qd(T(i)) - th*.5, ['t', int2str(i)], 'FontSize', 8);
     end;    
 end;
end;

hold off;
