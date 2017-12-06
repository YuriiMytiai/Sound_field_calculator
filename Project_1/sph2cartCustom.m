function [X, Y, Z] = sph2cartCustom(Phi, Theta, R)
% all 3 input arguments must have same shapes
cosPhi = cos(Phi);
sinPhi = sin(Phi);
cosTheta = cos(Theta);
sinTheta = sin(Theta);

X = R .* cosPhi .* sinTheta;
Y = R .* sinPhi .* sinTheta;
Z = R .* cosTheta;
end