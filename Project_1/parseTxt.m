function [MFR, AbsRP, PhaseRP] = parseTxt(filesPath)
persistent f
if isempty(f)
    f = [16 20 25 31.5 40 50 63 80 100 125 ...
            160 200 250 315 400 500 630 800 1e3 1.25e3 ...
            1.6e3 2e3 2.5e3 3.15e3 4e3 5e3 6.3e3 8e3 1e4 1.25e4 ...
            1.6e4 2e4];
end

filesList = dir(fullfile(filesPath, '*.txt'));

AbsRP = zeros(360,181,length(f));
PhaseRP = zeros(360,181,length(f));

x = 1:5:181;
y = 1:30:91;
[X,Y] = meshgrid(x,y);
xq = 1:1:181;
yq = 1:1:91;
[Xq,Yq] = meshgrid(xq,yq);

for curFile = 1:length(filesList);
    
    C = strsplit(filesList(curFile).name,{' ','.'});
    theta = str2double(C{2})/100;
    phi = str2double(C{3})/100;
    
    fileID = fopen(fullfile(filesPath,filesList(curFile).name),'r');
    Intro = textscan(fileID,'%s',1,'Delimiter','\n');
    formatSpec = '%f %f %f';
    sizeA = [3 Inf];
    A = fscanf(fileID,formatSpec, sizeA);
    % A = A';
    fclose(fileID);
    
    indxs = findClosest(A(1,:),f);
    
    if (theta == 0) && (phi == 0)
        MFR = A(2,indxs);
    end
    
    for curF = 1:length(f)
        AbsRP(phi+1,theta+1,curF) = A(2,indxs(curF));
        PhaseRP(phi+1,theta+1,curF) = deg2rad(A(3,indxs(curF)));
    end

end
for curF = 1:length(f)
    SPL11 = AbsRP(1,1,curF);
    AbsRP(:,:,curF) = AbsRP(:,:,curF) - SPL11;
end

for curF = 1:length(f)
    B = AbsRP(x,y,curF);
    Bq = interp2(X, Y, B', Xq, Yq, 'cubic', 0);
    Bq = Bq';
    AbsRP(1:181, 1:91, curF) = Bq;
    Bq = flipud(Bq);
    AbsRP(181:360, 1:91, curF) = Bq(1:(end-1),:);
    halfRP = AbsRP(1:360, 1:91, curF);
    halfRP = fliplr(halfRP);
    AbsRP(:,91:181, curF) = halfRP;
    
    B = PhaseRP(x,y,curF);
    Bq = interp2(X, Y, B', Xq, Yq);
    Bq = Bq';
    PhaseRP(1:181, 1:91, curF) = Bq;
    Bq = flipud(Bq);
    PhaseRP(181:360, 1:91, curF) = Bq(1:(end-1),:);
    halfRP = PhaseRP(1:360, 1:91, curF);
    halfRP = fliplr(halfRP);
    PhaseRP(:,91:181, curF) = halfRP;
end
end
%%
    function indxs = findClosest(arr,tgt)
        indxs = zeros(size(tgt));
            for i = 1: length(tgt)
                [~, indxs(i)] = min(abs(arr - tgt(i)));
            end
    end
    