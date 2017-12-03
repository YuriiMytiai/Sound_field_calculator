function [A, Psi] = calcPreasure(areaPoint, source, freq_idx, c0)
%calcSPL is function for SPL calculations in one point of area

if nargin < 4
    c0 = 340;
end

sourceData = source.SSobj.sourceData; % unpack data about source from bject

xR = areaPoint(1); % unpack vector with coordinates of reciever (area coordinate system)
yR = areaPoint(2);
zR = areaPoint(3);

xS = source.position(1); % unpack vector with coordinates of source (area coordinate system)
yS = source.position(2);
zS = source.position(3);

xRS = xR - xS; % calculate reciever coordinates in source coordinate system
yRS = yR - yS;
zRS = zR - zS;

r = sqrt(xRS*xRS + yRS*yRS + zRS*zRS); % distance from source to reciever
thetaRS = round(acosd(zRS/r)); % nearest integer
phiRS = round(atan2d(yRS, xRS)); % nearest integer in range [-180 180]
if phiRS < 0
    phiRS = 360 + phiRS; % change range to [0 360]
end

theta = thetaRS + round(source.theta0); % include rotation in two dimensionals
phi = phiRS + round(source.phi0);

alpha = 1.24e-11 * sourceData.freqs(freq_idx)^2; % air absorbtion coef.

A = sourceData.sensitivity(freq_idx) + source.K - 20*log10(r) + sourceData.amplitudeRP(phi, theta) - 20*alpha*r*log10(exp(1)); % amplitude value of preasure in dB
Psi = 2*pi*sourceData.freqs(freq_idx) * (r/c0 + source.tau) + sourceData.phaseRP(phi, theta); % phase of preasure in rad

% p = A*exp(-j*Psi)
end

