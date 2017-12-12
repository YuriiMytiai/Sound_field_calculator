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
ARP = zeros(181,91,length(f))+NaN;
PRP = ARP;

phi = 1:181;
theta = 1:91;
[Phi, Theta] = meshgrid(phi, theta);
Phi = Phi';
Theta = Theta';
for curFile = 1:length(filesList);
    
    C = strsplit(filesList(curFile).name,{' ','.'});
    thetaFile = str2double(C{2})/100;
    phiFile = str2double(C{3})/100;
    
    fileID = fopen(fullfile(filesPath,filesList(curFile).name),'r');
    Intro = textscan(fileID,'%s',1,'Delimiter','\n');
    formatSpec = '%f %f %f';
    sizeA = [3 Inf];
    A = fscanf(fileID,formatSpec, sizeA);
    fclose(fileID);
    
    indxs = findClosest(A(1,:),f);
    
    if (thetaFile == 0) && (phiFile == 0)
        MFR = A(2,indxs);
    end

    for curF = 1:length(f)
        if thetaFile == 0 
            theta = 90;
            phi = phiFile;
        elseif thetaFile == 30;
            [xx, yy, zz] = sph2cartCustom(deg2rad(phiFile), deg2rad(90), 1);
            R = rotx(30);
            CN = R*[xx; yy; zz];
            rn = sqrt(CN(1,:).^2 + CN(2,:).^2 + CN(3,:).^2);
            theta = round(acosd(CN(3,:)./rn),0);
            phi = round(atan2d(CN(2,:), CN(1,:)),0);
        elseif thetaFile == 60
            [xx, yy, zz] = sph2cartCustom(deg2rad(phiFile), deg2rad(90), 1);
            R = rotx(60);
            CN = R*[xx; yy; zz];
            rn = sqrt(CN(1,:).^2 + CN(2,:).^2 + CN(3,:).^2);
            theta = round(acosd(CN(3,:)./rn),0);
            phi = round(atan2d(CN(2,:), CN(1,:)),0);
        elseif thetaFile == 90;
            theta = 90 - phiFile;
            phi = 0;
%             if theta == 0
%                 phi = 90;
%             end
            if theta < 0
                theta = abs(theta);
                phi = 180;
            end
        end
        ARP(phi+1,theta+1,curF) = A(2,indxs(curF));
        PRP(phi+1,theta+1,curF) = deg2rad(A(3,indxs(curF)));
    end

end

for curF = 1:length(f)
    clear B;
    B = ARP(:,:,curF);

    idxgood=~(isnan(B)); 
    Bq = griddata(Phi(idxgood), Theta(idxgood), B(idxgood)', Phi, Theta);

    SPL11 = Bq(1,1);
    Bq = Bq - SPL11;
    AbsRP(1:181, 1:91, curF) = Bq;
    Bq = flipud(Bq);
    AbsRP(181:360, 1:91, curF) = Bq(1:(end-1),:);
    halfRP = AbsRP(1:360, 1:91, curF);
    halfRP = fliplr(halfRP);
    AbsRP(:,91:181, curF) = halfRP;
    
    B = PRP(:,:,curF);
    idxgood=~(isnan(B)); 
    Bq = griddata(Phi(idxgood), Theta(idxgood), B(idxgood)', Phi, Theta);
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
    