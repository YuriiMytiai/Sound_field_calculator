function [A, Psi] = calcPreasure(areaPoint, source, freq_idx, c0)
%calcSPL is function for SPL calculations in one point of area

if nargin < 4
    c0 = 340;
end

sourceData = source.SSobj; % unpack data about source from bject

xR = areaPoint(1); % unpack vector with coordinates of reciever (area coordinate system)
yR = areaPoint(2);
zR = areaPoint(3);

xS = source.position(1); % unpack vector with coordinates of source (area coordinate system)
yS = source.position(2);
zS = source.position(3);

R = roty(round(source.theta0));


xRS = xR - xS; % calculate reciever coordinates in source coordinate system
yRS = yR - yS;
zRS = zR - zS;

newC = R*[xRS; yRS; zRS]; % new coordinates accordiong to incline of source
xRS = newC(1); % unpack vector
yRS = newC(2);
zRS = newC(3);

r = sqrt(xRS*xRS + yRS*yRS + zRS*zRS); % distance from source to reciever
theta = round(acosd(zRS/r)); % nearest integer
phiRS = round(atan2d(yRS, xRS)); % nearest integer in range [-180 180]

phi = phiRS + round(source.phi0); 


if phi > 359
    phi = phi - 359;
end
if phi < 0
    phi = 359 + phi; % change range to [0 360]
end


phi = phi + 1; % + 1 cause matlab indexes start from 1;
theta = theta + 1;

alpha = 1.24e-11 * sourceData.f(freq_idx)^2; % air absorbtion coef.

A = sourceData.sensitivity(freq_idx) + source.K - 20*log10(r) + sourceData.amplitudeRP(phi, theta, freq_idx) - 20*alpha*r*log10(exp(1)); % amplitude value of preasure in dB
Psi = 2*pi*sourceData.f(freq_idx) * (r/c0 + source.tau0) + sourceData.phaseRP(phi, theta, freq_idx); % phase of preasure in rad

% p = A*exp(-j*Psi)
end

