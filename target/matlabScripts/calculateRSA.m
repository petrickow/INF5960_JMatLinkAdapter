function [RSA, saveDiffRwin, saveDiffNum, firstR, lastR] = calculateRSA(troughs, RR, ECG, y);

% troughs contain the time points of each valid respiration trough, 
% after adjustments/etc.
% RR contains the time of all R waves in the signal
% ECG is the raw ECG signal, Qd the raw (decimated) respiration signal
% returns RSA, containing the RSA score for each breath

stopPt = 0; startPt = 0; i = 0; j = 0; Rwindow = []; numBeats = 0;
maxBeat = 0; minBeat = 0; minPlace = 0; maxPlace = 0; RSA = [];
saveDiffRwin = []; saveDiffNum = []; firstR = 0;

numBreaths = length(troughs) - 1;  % subtract 1 since breath defined between troughs
numRs = length(RR); 

for i = 1:numBreaths
  stopPt = troughs(i + 1)  % get the start and stop time for this breath
  startPt = troughs(i)

  status = 0;
  Rwindow = [];           % fill Rwindow with the R points of the beats
  for j = 1:numRs         % that occured during this breath.  
    if RR(j) >= startPt & RR(j) < stopPt
      if status == 0        
	Rwindow = [RR(j)];      % 1st beat in window is the 1st AFTER the trough
	status = 1;
	if firstR == 0   % save this for plotting
	  firstR = RR(j);
	end;
      else
        Rwindow = [Rwindow, RR(j)];
      end;
    end;
    if RR(j) >= stopPt
      Rwindow = [Rwindow, RR(j)];   % last beat is the 1st AFTER the 2nd trough
      lastR = RR(j);  % save this for plotting also
      break;
    end;
  end;

  diffRwindow = diff(Rwindow)  % time between all beats in the window
  maxBeat = max(diffRwindow);   % find longest and shortest beats
  minBeat = min(diffRwindow);
  numBeats = length(diffRwindow);   % find order of max and min beats in the window
  saveDiffRwin = [saveDiffRwin, diffRwindow];  % save for plotting
  saveDiffNum = [saveDiffNum, (i * ones(1, numBeats))];

  for j = 1:numBeats
    if maxBeat == diffRwindow(j)
      maxPlace = j;
    end;
    if minBeat == diffRwindow(j)
      minPlace = j;
    end;
  end;
    
  if minPlace < maxPlace
    RSA = [RSA, maxBeat - minBeat];
  else
    RSA = [RSA, 0];
  end;
end;

% code to save the variables as external files for checking the calculations
% fid = fopen('RR.txt','w'); fprintf(fid,'%6.2f\n', RR); fclose(fid);
% fid = fopen('troughs.txt','w'); fprintf(fid,'%6.2f\n',troughs); fclose(fid);
% fid = fopen('RSA.txt','w'); fprintf(fid,'%6.2f\n', RSA); fclose(fid);
